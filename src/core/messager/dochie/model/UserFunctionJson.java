package core.messager.dochie.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import core.messager.dochie.helper.DochieJASONHelper;

public class UserFunctionJson {
	private DochieJASONHelper jsonParser;
	private static String loginURL = core.messager.dochie.constant.Constants.HOST_JSON
			+ "/actLogin/";
	private static String registerURL = core.messager.dochie.constant.Constants.HOST_JSON
			+ "/actRegister/";
	public static String registerSMSURL = core.messager.dochie.constant.Constants.HOST_JSON
			+ "/queryregister/";
	public static String getPhoneURL = core.messager.dochie.constant.Constants.HOST_JSON
			+ "/cekPhone/";
	private static String cekCon = core.messager.dochie.constant.Constants.HOST_JSON
			+ "/cekContactPhone/";
	public static String getfriend = core.messager.dochie.constant.Constants.HOST_JSON
			+ "/ffriend/";
	private static String addfriend = core.messager.dochie.constant.Constants.HOST_JSON
			+ "/ffriend_add/";
	private static String login_tag = "login";
	private static String register_tag = "register";
	private static String cekUser_tag = "cekUser";
	private static String cek_tag = "cekcontact";

	// constructor
	public UserFunctionJson() {
		jsonParser = new DochieJASONHelper();
	}

	/**
	 * function make Login Request
	 * 
	 * @param email
	 * @param password
	 * */
	
	public JSONObject add_contact(String email, String email_teman,String nm_teman) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email + "@"
				+ core.messager.dochie.constant.Constants.HOST_EMAIL));
		params.add(new BasicNameValuePair("email_teman", email_teman));
		params.add(new BasicNameValuePair("nm_user", nm_teman));
		JSONObject json = jsonParser.getJSONFromUrl(addfriend, params);
		return json;
	}
	
	public JSONObject loginUser(String email, String password) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email + "@"
				+ core.messager.dochie.constant.Constants.HOST_EMAIL));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	public JSONArray getContact(String email) throws JSONException {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", email));
		JSONArray json = new JSONArray(jsonParser.getJSONFromUrlString(
				getfriend, params));
		return json;
	}
	public JSONArray cekPhone(String sin) throws JSONException {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("sin", sin));
		JSONArray json = new JSONArray(jsonParser.getJSONFromUrlString(
				getPhoneURL, params));
		return json;
	}

	public JSONObject cekPhoneObj(String ssin) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("sin", ssin));
		JSONObject json = jsonParser.getJSONFromUrl(getPhoneURL, params);
		return json;
	}

	/**
	 * function make Login Request
	 * 
	 * @param name
	 * @param email
	 * @param password
	 * */

	public JSONObject registerUser(String email, String password, String phone) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("email", email + "@"
				+ core.messager.dochie.constant.Constants.HOST_EMAIL));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("phone", phone));

		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		// return json
		return json;
	}

	public JSONObject cekUsers(String email) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", cekUser_tag));
		params.add(new BasicNameValuePair("email", email));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	public JSONObject registerSMS(String SSIM) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("sin", SSIM));
		JSONObject json = jsonParser.getJSONFromUrl(registerSMSURL, params);
		return json;
	}

	public JSONObject cekContact(String email) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", cek_tag));
		params.add(new BasicNameValuePair("username", email));
		JSONObject json = jsonParser.getJSONFromUrl(cekCon, params);
		return json;
	}

	/**
	 * Function get Login status
	 * */
	// public boolean isUserLoggedIn(Context context){
	// DatabaseHandler db = new DatabaseHandler(context);
	// int count = db.getRowCount();
	// if(count > 0){
	// // user logged in
	// return true;
	// }
	// return false;
	// }
	//
	// /**
	// * Function to logout user
	// * Reset Database
	// * */
	// public boolean logoutUser(Context context){
	// DatabaseHandler db = new DatabaseHandler(context);
	// db.resetTables();
	// return true;
	// }
}
