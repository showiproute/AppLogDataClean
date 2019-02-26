package DataCleanMR;

import com.alibaba.fastjson.JSONObject;

public class JsonToStringUtil {

	public static String toString(JSONObject headerObj) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(headerObj.get("sdk_ver")).append("\001")
		.append(headerObj.get("time_zone")).append("\001")
		.append(headerObj.get("commit_id")).append("\001")
		.append(headerObj.get("commit_time")).append("\001")
		.append(headerObj.get("pid")).append("\001")
		.append(headerObj.get("app_token")).append("\001")
		.append(headerObj.get("app_id")).append("\001")
		.append(headerObj.get("device_id")).append("\001")
		.append(headerObj.get("device_id_type")).append("\001")
		.append(headerObj.get("release_channel")).append("\001")
		.append(headerObj.get("app_ver_name")).append("\001")
		.append(headerObj.get("app_ver_code")).append("\001")
		.append(headerObj.get("os_name")).append("\001")
		.append(headerObj.get("os_ver")).append("\001")
		.append(headerObj.get("language")).append("\001")
		.append(headerObj.get("country")).append("\001")
		.append(headerObj.get("manufacture")).append("\001")
		.append(headerObj.get("device_model")).append("\001")
		.append(headerObj.get("resolution")).append("\001")
		.append(headerObj.get("net_type")).append("\001")
		.append(headerObj.get("account")).append("\001")
		.append(headerObj.get("app_device_id")).append("\001")
		.append(headerObj.get("mac")).append("\001")
		.append(headerObj.get("android_id")).append("\001")
		.append(headerObj.get("imei")).append("\001")
		.append(headerObj.get("cid_sn")).append("\001")
		.append(headerObj.get("build_num")).append("\001")
		.append(headerObj.get("mobile_data_type")).append("\001")
		.append(headerObj.get("promotion_channel")).append("\001")
		.append(headerObj.get("carrier")).append("\001")
		.append(headerObj.get("city")).append("\001")
		.append(headerObj.get("user_id"));
		
		return sb.toString();
		
	}
}
