package core.messager.dochie.network;

import core.messager.dochie.constant.StaticVariable;
import core.messager.dochie.helper.DochieAsynTaskSendingEmail;
import core.messager.dochie.service.DochieServiceGettingEmail;
import core.messager.dochie.service.DochieServiceImapIdle;
import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

/**
 * DochieServiceNetworkProbe merupakan service yang digunakan untuk melakukan
 * cek koneksi. Cek Koneksi terdiri dari dua tahap yaitu cek koneksi internet
 * dan cek koneksi ke mail server dengan menggunakan class Socket. Class ini
 * extends dari class Service android
 * 
 * @author Yatno Sudar
 * @version Dochie Messager 0.5 Build 7
 * */
public class DochieServiceNetworkProbe extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method override dari class Service. Method digunakan untuk menjalankan
	 * service. Method ini memanggil inner class Task_DochieServiceNetwork
	 */
	@Override
	public void onCreate() {
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
				
		new Task_DochieServiceNetwork().execute("runservice");
		super.onCreate();
	}

	/**
	 * Method yang digunakan untuk mengecek apakah service IMAPIDLE dalam
	 * keadaan running atau stop
	 * 
	 * @return boolean jika true Service IMAPIDLE dalam keadaan running jika
	 *         false service dalam keadaan stop
	 */
	public boolean cekIMAPIDLE() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("core.messager.dochie.service.DochieServiceImapIdle"
					.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Inner Class dari class DochieServiceNetworkProbe. Class ini digunakan
	 * untuk melakukan cek koneksi internet dan koneksi ke mail server. class
	 * ini memanggil class Call_DochieNetworkProbe yang bersifat callback. Class
	 * ini exteds dari class AsynTask
	 * 
	 * @param String
	 *            parameter yang digunakan saat memanggil class
	 * @param String
	 *            paramater yang digunakan saat Background proccessing serdang
	 *            berjalan
	 * @param Boolean
	 *            parameter result dari hasil background procecessing
	 * */
	class Task_DochieServiceNetwork extends AsyncTask<String, String, Boolean> {

		/**
		 * Method override dari class AsynTask. Method ini dipanggil ketika
		 * menjalankan class Task_DochieServiceNetwork. Method ini memanggil
		 * class Call_DochieNetworkProbe yang merupakan callback untuk mengecek
		 * status network. Isi dari method ini sebagai berikut 
		 * <pre><blockquote>
		 * 
		 * Call_DochieNetworkProbe clbk = new Call_DochieNetworkProbe(
					DochieServiceNetworkProbe.this);
			clbk.cek_network(new Abs_DochieNetworkProbe() {
				<b>@Override</b>
				public void ifNetworkDown() {
					<b>//Melakukan cek jika service IMAPIDLE dalam keadaan running maka proses akan dimatikan</b>
					if (cekIMAPIDLE()) {
						stopService(new Intent(DochieServiceNetworkProbe.this,
								DochieServiceImapIdle.class));
						stopService(new Intent(DochieServiceNetworkProbe.this,
								DochieServiceGettingEmail.class));
					}
					
					StaticVariable.NETWORK_UP = false;
					StaticVariable.NETWORK_DOWN = true;
					super.ifNetworkDown();
				}

				<b>@Override</b>
				public void ifNetworkUp() {
				<b>//Melakukan cek jika service IMAPIDLE dalam keadaan stop maka proses akan dijalankan</b>
					if (!cekIMAPIDLE()) {
						new DochieAsynTaskSendingEmail(
								DochieServiceNetworkProbe.this)
								.execute("execute");
						startService(new Intent(DochieServiceNetworkProbe.this,
								DochieServiceGettingEmail.class));
						startService(new Intent(DochieServiceNetworkProbe.this,
								DochieServiceImapIdle.class));
					}
					StaticVariable.NETWORK_UP = true;
					StaticVariable.NETWORK_DOWN = false;
					super.ifNetworkUp();
				}
			});
			</blockquote></pre>
		 * 
		 * 
		 */
		@Override
		protected Boolean doInBackground(String... params) {
			Call_DochieNetworkProbe clbk = new Call_DochieNetworkProbe(
					DochieServiceNetworkProbe.this);
			clbk.cek_network(new Abs_DochieNetworkProbe() {
				@Override
				public void ifNetworkDown() {
					if (cekIMAPIDLE()) {
						stopService(new Intent(DochieServiceNetworkProbe.this,
								DochieServiceImapIdle.class));
						stopService(new Intent(DochieServiceNetworkProbe.this,
								DochieServiceGettingEmail.class));
					}
					StaticVariable.NETWORK_UP = false;
					StaticVariable.NETWORK_DOWN = true;
					super.ifNetworkDown();
				}

				@Override
				public void ifNetworkUp() {
					if (!cekIMAPIDLE()) {
						new DochieAsynTaskSendingEmail(
								DochieServiceNetworkProbe.this)
								.execute("execute");
						startService(new Intent(DochieServiceNetworkProbe.this,
								DochieServiceGettingEmail.class));
						startService(new Intent(DochieServiceNetworkProbe.this,
								DochieServiceImapIdle.class));
					}
					StaticVariable.NETWORK_UP = true;
					StaticVariable.NETWORK_DOWN = false;
					super.ifNetworkUp();
				}

				@Override
				public void ifNetworkDown(String e) {
					if (cekIMAPIDLE()) {
						stopService(new Intent(DochieServiceNetworkProbe.this,
								DochieServiceImapIdle.class));
						stopService(new Intent(DochieServiceNetworkProbe.this,
								DochieServiceGettingEmail.class));
					}
					StaticVariable.NETWORK_UP = false;
					StaticVariable.NETWORK_DOWN = true;
					super.ifNetworkDown(e);
				}
			});
			return null;
		}

	}
}
