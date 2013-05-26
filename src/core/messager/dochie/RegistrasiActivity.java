package core.messager.dochie;
/*
 * Class Registrasi user 
 * class ini digunakan untuk melakukan registrasi email user
 * class ini nantinya akan mengirimkan sms secara background 
 * dan di terima pada server server akan melakukan proses registrasi
 * 
 * */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.DownloadManager.Request;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import core.messager.dochie.constant.Constants;
import core.messager.dochie.helper.DochieLogManager;
import core.messager.dochie.helper.DochieTelpHelper;
import core.messager.dochie.model.UserFunctionJson;
import core.messager.dochie.network.Call_DochieNetworkProbe;

public class RegistrasiActivity extends Activity {

	ProgressDialog progressDialog;
	DochieTelpHelper x;
	Call_DochieNetworkProbe clbk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//stricmode
				StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		        .detectDiskReads()
		        .detectDiskWrites()
		        .detectNetwork()   // or .detectAll() for all detectable problems
		        .penaltyLog()
		        .build());
				StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		        .detectLeakedSqlLiteObjects()
		        .penaltyLog()
		        .penaltyDeath()
		        .build());
				
		x = new DochieTelpHelper(RegistrasiActivity.this);
		clbk = new Call_DochieNetworkProbe(RegistrasiActivity.this);

		Log.i("SIM", x.gettMgr());
		if (!clbk.isOnline()) {
			Intent i = new Intent(RegistrasiActivity.this,
					ErrorMessageActivity.class);
			i.putExtra("err_message",
					"Error Connection Internet From Registration.Activity");
			startActivity(i);
			finish();
		} else if (!clbk.is_connect()) {
			Intent i = new Intent(RegistrasiActivity.this,
					ErrorMessageActivity.class);
			i.putExtra("err_message",
					"Error Connection Socket From Registration.Activity");
			startActivity(i);
			finish();
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.regitrasi_user);

	}

	public void btnRegClick(View view) {
		if (clbk.is_connect()) {
			sendSMS(core.messager.dochie.constant.Constants.PHONE_SERVER,
					x.gettMgr());
			//view.findViewById(R.id.btnYesRegis).setEnabled(false);
		} else {
			Toast.makeText(getBaseContext(), "Error Connection... ",
					Toast.LENGTH_SHORT).show();
		}
		
	}

	private void sendSMS(String phoneNumber, String message) {
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		
		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS sent",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic failure",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No service",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(), "Null PDU",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS delivered",
							Toast.LENGTH_SHORT).show();
					// runDialog(5);
					new TaskReg_dochie(RegistrasiActivity.this).execute(5);
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getBaseContext(), "SMS not delivered",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
		
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	class TaskReg_dochie extends AsyncTask<Integer, String, Boolean> {

		Context c;
		ProgressDialog pg;
		boolean b = false;
		
		
		public TaskReg_dochie(Context c) {
			this.c = c;
			pg = new ProgressDialog(c);
		}

		public boolean isB() {
			return b;
		}

		public void setB(boolean b) {
			this.b = b;
		}

		public void set_regis() {
			DochieLogManager.INFORMATION("SERIAL NUMBER", new DochieTelpHelper(RegistrasiActivity.this).gettMgr());
			try {
				JSONObject jb = new UserFunctionJson().registerSMS(new DochieTelpHelper(RegistrasiActivity.this).gettMgr());
				String jbdata = jb.getString(Constants.KEY_SUCCESS);
				if (Integer.parseInt(jbdata)==1) {
					setB(true);
				}else{
					setB(false);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
		}

		@Override
		protected void onPreExecute() {
			pg.setTitle("Registration Proccess ");
			pg.setMessage("Please Wait...");
			pg.show();
			
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
				
				try {
					Thread.sleep(10000);
					set_regis();
					return isB();
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			Log.e("Hasil Process",""+result);
			pg.dismiss();
			if (result) {
				startActivity(new Intent(RegistrasiActivity.this, Dochie.class));
			} else {
				Intent i = new Intent(RegistrasiActivity.this,
						ErrorMessageActivity.class);
				i.putExtra("err_message",
						"Error Registration regis() From Registration.Activity");
				startActivity(i);

			}
			
			super.onPostExecute(result);
		}

	}
}
