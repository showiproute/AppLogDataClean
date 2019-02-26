package DataCleanMR;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;



public class AppLogDataClean {

	public static class AppLogDataCleanMapper extends Mapper<LongWritable, Text, Text, NullWritable>{
		
		Text k=new Text();
		NullWritable v=NullWritable.get();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MultipleOutputs<Text,NullWritable> mos=null;
		
		@Override
		protected void setup(Mapper<LongWritable, Text, Text, NullWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			mos=new MultipleOutputs(context); //多路输出器
		}
		
		
		@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, NullWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			JSONObject jsonObj = JSON.parseObject(value.toString());	
			JSONObject headerObj = jsonObj.getJSONObject(GlobalConstant.HEADER);
			
			/*
			 * 过滤缺失必选字段的json
			 */
			
			if(null == headerObj.getString("sdk_ver") || "".equals(headerObj.getString("sdk_ver").trim()) ) {
				return;
			}
			if(null == headerObj.getString("time_zone") || "".equals(headerObj.getString("time_zone").trim()) ) {
				return;
			}
			if(null == headerObj.getString("commit_id") || "".equals(headerObj.getString("commit_id").trim()) ) {
				return;
			}
			if(null == headerObj.getString("commit_time") || "".equals(headerObj.getString("commit_time").trim()) ) {
				return;
			}else {
				//练习时追加的逻辑，替换掉原始数据中的时间戳
				String commit_time = headerObj.getString("commit_time");
				String format = sdf.format(new Date(Long.parseLong(commit_time)+377*24*60*60*1000L));
				headerObj.put("commit_time",format);
				
			}
			if(null == headerObj.getString("pid") || "".equals(headerObj.getString("pid").trim()) ) {
				return;
			}
			if(null == headerObj.getString("app_token") || "".equals(headerObj.getString("app_token").trim()) ) {
				return;
			}
			if(null == headerObj.getString("app_id") || "".equals(headerObj.getString("app_id").trim()) ) {
				return;
			}
			if(null == headerObj.getString("device_id") || headerObj.getString("device_id").length()<17 ) {
				return;
			}
			if(null == headerObj.getString("device_id_type") || "".equals(headerObj.getString("device_id_type").trim()) ) {
				return;
			}
			if(null == headerObj.getString("release_channel") || "".equals(headerObj.getString("release_channel").trim()) ) {
				return;
			}
			if(null == headerObj.getString("app_ver_name") || "".equals(headerObj.getString("app_ver_name").trim()) ) {
				return;
			}
			if(null == headerObj.getString("app_ver_code") || "".equals(headerObj.getString("app_ver_code").trim()) ) {
				return;
			}
			if(null == headerObj.getString("os_name") || "".equals(headerObj.getString("os_name").trim()) ) {
				return;
			}
			if(null == headerObj.getString("os_ver") || "".equals(headerObj.getString("os_ver").trim()) ) {
				return;
			}
			if(null == headerObj.getString("language") || "".equals(headerObj.getString("language").trim()) ) {
				return;
			}
			if(null == headerObj.getString("country") || "".equals(headerObj.getString("country").trim()) ) {
				return;
			}
			if(null == headerObj.getString("manufacture") || "".equals(headerObj.getString("manufacture").trim()) ) {
				return;
			}
			if(null == headerObj.getString("device_model") || "".equals(headerObj.getString("device_model").trim()) ) {
				return;
			}
			if(null == headerObj.getString("resolution") || "".equals(headerObj.getString("resolution").trim()) ) {
				return;
			}
			if(null == headerObj.getString("net_type") || "".equals(headerObj.getString("net_type").trim()) ) {
				return;
			}
			
		    /*
		     * 生成user_id
		     */
			String user_id="";
			if("android".equals(headerObj.getString("os_name").trim())) {
				user_id=StringUtils.isNotBlank(headerObj.getString("android_id"))?headerObj.getString("android_id"):
					headerObj.getString("device_id");
			}else {
				user_id=headerObj.getString("device_id");
				
			}
			
			/*
			 * 输出结果
			 */
			headerObj.put("user_id", user_id);
			k.set(JsonToStringUtil.toString(headerObj));
			
			if("android".equals(headerObj.getString("os_name"))) {
				mos.write(k,v, "android/android");
			}else {
				mos.write(k, v, "ios/ios");
			}
			
		}
		
		@Override
		protected void cleanup(Mapper<LongWritable, Text, Text, NullWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			mos.close();
		}
		
	}
	
	
	
	public static void main(String[] args) throws Exception{
		
		Configuration conf = new Configuration();
		
		Job job = Job.getInstance();
		job.setJarByClass(AppLogDataClean.class);
		job.setMapperClass(AppLogDataCleanMapper.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		
		job.setNumReduceTasks(0);
		
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));	
		
		
	
		boolean res = job.waitForCompletion(true);
		System.exit(res?0:-1);

	}
}
