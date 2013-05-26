package core.messager.dochie.helper;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

public class DochieTelpHelper {
	private String tMgr = "";
	private String simCountryISO = "";
	private String simOperator = "";
	private String simOperatorName = "";
	private int simState;
	
	private TelephonyManager telephoneManager;
	
	public DochieTelpHelper(Context c) {
		super();
		telephoneManager =(TelephonyManager) c.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		Log.i(DochieTelpHelper.class.toString(), "Get Data SIM");
		settMgr(telephoneManager.getSimSerialNumber());
		setSimCountryISO(telephoneManager.getSimCountryIso());
		setSimOperator(telephoneManager.getSubscriberId());
		setSimOperatorName(telephoneManager.getNetworkOperatorName());
		setSimState(telephoneManager.getSimState());
		
		
	}

	public String gettMgr() {
		return tMgr;
	}

	public void settMgr(String tMgr) {
		this.tMgr = tMgr;
	}

	public String getSimCountryISO() {
		return simCountryISO;
	}

	public void setSimCountryISO(String simCountryISO) {
		this.simCountryISO = simCountryISO;
	}

	public String getSimOperator() {
		return simOperator;
	}

	public void setSimOperator(String simOperator) {
		this.simOperator = simOperator;
	}

	public String getSimOperatorName() {
		return simOperatorName;
	}

	public void setSimOperatorName(String simOperatorName) {
		this.simOperatorName = simOperatorName;
	}

	public int getSimState() {
		return simState;
	}

	public void setSimState(int simState) {
		this.simState = simState;
	}

	public TelephonyManager getTelephoneManager() {
		return telephoneManager;
	}

	public void setTelephoneManager(TelephonyManager telephoneManager) {
		this.telephoneManager = telephoneManager;
	} 
	
	

}
