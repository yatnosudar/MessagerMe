package core.messager.dochie.sync;

import core.messager.dochie.R;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

public class LoginSyncActivity extends AccountAuthenticatorActivity {
	
	ProgressDialog pg ;
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.actionbar_dochie);
		pg = new ProgressDialog(LoginSyncActivity.this);
	}
	
	public class ProcessLogin extends AsyncTask<String, Integer, Boolean>{

		@Override
		protected void onPreExecute() {
			pg.setTitle("Prosses Login");
			pg.setMessage("Please Wait...");
			pg.show();
			super.onPreExecute();
		}
		@Override
		protected Boolean doInBackground(String... params) {
			//AccountManager am = new 
			return null;
		}}
}
