package core.messager.dochie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import core.messager.dochie.constant.Constants;
import core.messager.dochie.helper.DochieAccountAuth;
import core.messager.dochie.helper.DochieAsynTaskSync;
import core.messager.dochie.helper.DochieLogManager;
import core.messager.dochie.helper.DochiePreferenceHelper;
import core.messager.dochie.helper.DochieSQLiteHelper;
import core.messager.dochie.helper.DochieTelpHelper;
import core.messager.dochie.model.UserFunctionJson;
import core.messager.dochie.model.ModelUserDochie;
import core.messager.dochie.network.Call_DochieNetworkProbe;
import core.messager.dochie.network.DochieServiceNetworkProbe;

/**
 * <p>
 * Dochie CLASS Dochie Messenger class ini berjalan pada awal program dijalankan
 * alur dari class ini
 * <p>
 * <ul>
 * <li>melakukan pengecekan apakah user sudah memakai program seelumnya</li>
 * <li>melakukan pengecekan apakah user sudah terkoneksi dengan server</li>
 * <li>melakukan pengecekan apakah user sudah terdaftar sebelumnya</li>
 * <li>redirect ke class registrasi</li>
 * <li>mendaftarkan sync adapter pada sistem</li>
 * <li>melakukan sinkronisasi kontak user</li>
 * </ul>
 * 
 * @author Yatno Sudar
 * @version Dochie Messager 0.5 Build 7
 * */
public class Dochie extends AccountAuthenticatorActivity {

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
				
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new DochieBgProcess(Dochie.this).execute("run");
		Log.e("Service Run", "run");
	}

	/**
	 * DochieBgProcess merupakan inner class yang diexecute oleh class Dochie
	 * untuk menjalanan background proccessing Class ini extends dari class
	 * AsynTask
	 * 
	 * @param String
	 *            untuk mendapatkan parameter yang akan di isi
	 * @param String
	 *            untuk mendapatkan parameter ketika proses background sedang
	 *            berjalan
	 * @param Integer
	 *            untuk melakukan execute hasil akhir dari process background
	 * */
	class DochieBgProcess extends AsyncTask<String, String, Integer> {

		Context c;
		DochieTelpHelper dtt;
		DochiePreferenceHelper dph;
		Call_DochieNetworkProbe clbk;
		String pNumber = "";

		/**
		 * getpNumber merupakan methode dengan return String dan value berupa
		 * nomor handphone
		 */
		public String getpNumber() {
			return pNumber;
		}

		/**
		 * setpNumber merupakan methode yang berfungsi mengeset nomor handphone
		 * 
		 * @param String
		 *            pNumber nomor handphone
		 * */
		public void setpNumber(String pNumber) {
			this.pNumber = pNumber;
		}

		/**
		 * Constructor dari class DochieBgProcess. Pada constructor Class
		 * DochieBgProcess memanggil bebrapa object diantaranya dari class
		 * <ul>
		 * <li><code>DochieTelpHelper</code></li>
		 * <li><code>DochiePreferenceHelper</code></li>
		 * <li><code>Call_DochieNetworkProbe</code></li>
		 * </ul>
		 * 
		 * @param Context
		 *            context dari class Dochie
		 * */
		public DochieBgProcess(Context c) {
			this.c = c;
			dtt = new DochieTelpHelper(c);
			dph = new DochiePreferenceHelper(c);
			clbk = new Call_DochieNetworkProbe(c);

		}

		/**
		 * Method yang digunakan untuk mengecek apakah user sudah pernah memakai
		 * aplikasi Dochie dengan ponsel yang sama
		 * */
		boolean cek_user_aktif() {
			if (dtt.gettMgr().equals(dph.getSerialSIM())) {
				DochieLogManager.INFORMATION("user aktif",
						"user is : " + dph.getSerialSIM());
				return true;
			}
			return false;
		}

		/**
		 * Method yang digunakan untuk memperoleh nomor handphone user jika user
		 * tersebut sudah terdaftar di mail server. Method ini mengakses JSON
		 * dari webservice
		 */
		public void set_phoneNumber() {
			try {
				JSONObject j = new UserFunctionJson()
						.cekPhoneObj(dtt.gettMgr());
				setpNumber(j.getString("phone"));
				DochieLogManager.INFORMATION("Get Phone Number",
						j.getString("phone"));
			} catch (JSONException e) {
				setpNumber("");
			}
		}

		/**
		 * Method ini digunakana untuk sinkronsisasi contact yang ada di mail
		 * server dan yang ada di contact ponsel. Untuk mendapatkan data dari
		 * web service method ini menggunakan class AsyncHttpClient yang di
		 * import dari <code>com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http</code>. <br/>
		 * Kemudian data contact yang telah sinkronisasi tersbut di masukan
		 * kedalm SQLite : <code>
							dbuser.createMessage(
											nama,
											phone,
											phone.replace(
													"@"
															+ core.messager.dochie.constant.Constants.HOST_EMAIL,
													""), null, 1);
													</code>
		 * 
		 * @param String
		 *            email dari user dochie
		 * */
		public synchronized void getContactFromServer(String email) {
			AsyncHttpClient ahc = new AsyncHttpClient();
			RequestParams rp = new RequestParams();
			final ModelUserDochie dbuser = new ModelUserDochie(c);
			dbuser.open();
			rp.put("email", email);
			ahc.post(UserFunctionJson.getfriend, rp,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String arg0) {
							try {
								JSONArray ja = new JSONArray(arg0);
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jboUser = ja.getJSONObject(i);
									String phone = jboUser.getString("t_u");
									String nama = jboUser.getString("nm_user");
									dbuser.createMessage(
											nama,
											phone,
											phone.replace(
													"@"
															+ core.messager.dochie.constant.Constants.HOST_EMAIL,
													""), null, 1);
								}
							} catch (JSONException e) {
								DochieLogManager.ERROR("error JSON", e
										.getMessage().toString());
							}
							super.onSuccess(arg0);
						}
					});
			dbuser.close();

		}

		/**
		 * Merupakan method yang di extends dari class AsynTask<br/>
		 * Alur dari isi method ini adalah sebagai berikut :
		 * 
		 * <ul>
		 * <li>Melakukan pengecekan apakah user sudah pernah memakai applikasi
		 * Dochie di divice yang sama saat dia registrasi
		 * </li>
		 * <li>Jika user belum pernah memakai device tersbut maka aplikasi
		 * Dochie akan mengecek ke webservice apakah user tersebut sudah
		 * terdaftar atau belum</li>
		 * <li>Jika user tersbut belum terdaftar maka class Dochie akan
		 * mengalihkan ke halaman registrasi</li>
		 * <li>Jika user tersbut sudah terdaftar user maka aplikasi mail client
		 * akan mendestroy database sebelum nya dan menghapus accound sync yang
		 * sudah ada</li>
		 * </ul>
		 * 
		 * @param String
		 *            parameter yang dilempar ketika memanggil class
		 *            DochieBgProcess
		 * 
		 * */
		@Override
		protected Integer doInBackground(String... params) {

			if (!cek_user_aktif()) {
				deleteDatabase(DochieSQLiteHelper.DATABASE_NAME);
				// cekconnection
				if (clbk.is_connect()) {
					// connection true
					set_phoneNumber();

					DochieLogManager.INFORMATION(
							"user dan password",
							getpNumber() + "@" + Constants.HOST_EMAIL
									+ dtt.gettMgr());

					// cek phonenumber
					if (getpNumber() != null && getpNumber() != ""
							&& !getpNumber().equals("null")) {
						// cek auth email
						DochieAccountAuth daa = new DochieAccountAuth(
								getpNumber() + "@" + Constants.HOST_EMAIL,
								dtt.gettMgr());
						if (daa.authEmail()) {
							String dphnohp = dph.getNoHp().trim();
							if (dphnohp.length() > 0) {
								Account old_acc = new Account(dph.getNoHp(),
										"core.messager.dochie.account");
								AccountManager oldAccManager = AccountManager
										.get(Dochie.this);
								oldAccManager
										.removeAccount(old_acc, null, null);
							}

							// set preference baru
							new DochiePreferenceHelper(getpNumber(), true, c);

							// add account baru
							Bundle result = null;
							Account account = new Account(getpNumber(),
									"core.messager.dochie.account");
							AccountManager am = AccountManager.get(Dochie.this);
							if (am.addAccountExplicitly(account, dtt.gettMgr(),
									null)) {
								result = new Bundle();
								result.putString(
										AccountManager.KEY_ACCOUNT_NAME,
										account.name);
								result.putString(
										AccountManager.KEY_ACCOUNT_TYPE,
										account.type);
								setAccountAuthenticatorResult(result);
								// synccontact
								getContactFromServer(getpNumber() + "@"
										+ Constants.HOST_EMAIL);

								return 0;
							}
						} else {
							return 1;
						}
					} else {
						return 1;
					}
				} else {
					return 2;
				}
			} else {
				return 0;
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {

			if (result == 0) {
				if (!isServiceRunning()) {
					startService(new Intent(Dochie.this,
							DochieServiceNetworkProbe.class));
					new DochieAsynTaskSync().execute("start");
				}
				startActivity(new Intent(Dochie.this,
						PesanContactActivity.class));

			} else if (result == 1) {
				Intent i = new Intent(Dochie.this, RegistrasiActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_up_out);
			} else {
				Intent i = new Intent(Dochie.this, ErrorMessageActivity.class);
				i.putExtra("err_message", "Error Connection From Main.Activity");
				startActivity(i);
			}
			finish();
			super.onPostExecute(result);
		}

		private boolean isServiceRunning() {
			ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			for (RunningServiceInfo service : manager
					.getRunningServices(Integer.MAX_VALUE)) {
				if ("core.messager.dochie.service.DochieServiceNetworkProbe"
						.equals(service.service.getClassName())) {
					return true;
				}
			}
			return false;
		}

	}
}
