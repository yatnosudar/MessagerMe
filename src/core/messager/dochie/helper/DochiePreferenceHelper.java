package core.messager.dochie.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class DochiePreferenceHelper {
	private String noHp = "1234";
	private String serialSIM = "1234";
	private String idDetector = "1234";
	private String operatorname = "1234";
	private boolean haveLogin = false;

	public DochiePreferenceHelper(String noHp, boolean haveLogin, Context c) {
		DochieTelpHelper x = new DochieTelpHelper(c);
		SharedPreferences settings = c.getSharedPreferences(
				core.messager.dochie.constant.Constants.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(core.messager.dochie.constant.Constants.PHONE_NUMBER,
				noHp);
		
		editor.putString(core.messager.dochie.constant.Constants.PREFS_SIN,
				x.gettMgr());
		
		editor.putBoolean(core.messager.dochie.constant.Constants.HAVE_LOGIN,
				haveLogin);
		editor.putString(
				core.messager.dochie.constant.Constants.ID_DETECTOR,
				x.getSimCountryISO() + x.getSimOperator()
						+ x.getSimOperatorName());
		editor.putString(
				core.messager.dochie.constant.Constants.PREFS_OPERATORNAME,
				x.getSimOperatorName());
		editor.commit();
	}

	public DochiePreferenceHelper(boolean haveLogin, Context c) {
		SharedPreferences settings = c.getSharedPreferences(
				core.messager.dochie.constant.Constants.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(core.messager.dochie.constant.Constants.HAVE_LOGIN,
				haveLogin);
		editor.commit();
	}

	public DochiePreferenceHelper(Context c) {
		SharedPreferences settings = c.getSharedPreferences(
				core.messager.dochie.constant.Constants.PREFS_NAME, 0);
		setHaveLogin(settings.getBoolean(
				core.messager.dochie.constant.Constants.HAVE_LOGIN, false));
		setNoHp(settings.getString(
				core.messager.dochie.constant.Constants.PHONE_NUMBER, ""));
		setSerialSIM(settings.getString(
				core.messager.dochie.constant.Constants.PREFS_SIN, ""));
		setIdDetector(settings.getString(
				core.messager.dochie.constant.Constants.ID_DETECTOR, ""));
		setOperatorname(settings.getString(
				core.messager.dochie.constant.Constants.PREFS_OPERATORNAME, ""));

	}

	public String getOperatorname() {
		return operatorname;
	}

	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}

	public String getIdDetector() {
		return idDetector;
	}

	public void setIdDetector(String idDetector) {
		this.idDetector = idDetector;
	}

	public boolean isHaveLogin() {
		return haveLogin;
	}

	public void setHaveLogin(boolean haveLogin) {
		this.haveLogin = haveLogin;
	}

	public String getNoHp() {
		return noHp;
	}

	public void setNoHp(String noHp) {
		this.noHp = noHp;
	}

	public String getSerialSIM() {
		return serialSIM;
	}

	public void setSerialSIM(String serialSIM) {
		this.serialSIM = serialSIM;
	}

}
