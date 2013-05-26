package core.messager.dochie;

import core.messager.dochie.helper.DochiePreferenceHelper;
import core.messager.dochie.helper.DochieTelpHelper;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

public class LoginSyncAdapter extends AccountAuthenticatorActivity {
	DochiePreferenceHelper dph;
	DochieTelpHelper dth;
	ProgressDialog pg;

	@Override
	protected void onCreate(Bundle icicle) {
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
		
		super.onCreate(icicle);
		setContentView(R.layout.activity_main);
		dph = new DochiePreferenceHelper(this);
		dth = new DochieTelpHelper(this);
		pg = new ProgressDialog(LoginSyncAdapter.this);
		new ProcessLoginDochie(LoginSyncAdapter.this).execute(true);
	}

	public class ProcessLoginDochie extends AsyncTask<Boolean, Void, String> {

		Context c;

		public ProcessLoginDochie(Context c) {
			this.c = c;
		}

		@Override
		protected void onPreExecute() {
			pg.setTitle("Process Add Account");
			pg.setMessage("Please wait");
			pg.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Boolean... params) {

			Bundle result;
			Account acc = new Account(dph.getNoHp(),
					c.getString(R.string.ACCOUNT_TYPE));
			AccountManager am = AccountManager.get(c);
			if (am.addAccountExplicitly(acc, dth.gettMgr(), null)) {
				result = new Bundle();
				result.putString(AccountManager.KEY_ACCOUNT_NAME, acc.name);
				result.putString(AccountManager.KEY_ACCOUNT_TYPE, acc.type);
				setAccountAuthenticatorResult(result);
				Log.i("account explicitly", "yes");
				pg.dismiss();
			} else {
				Log.i("account explicitly", "no");
				pg.dismiss();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			finish();
			super.onPostExecute(result);
		}
	}
}
