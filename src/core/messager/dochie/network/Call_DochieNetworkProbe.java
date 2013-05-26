package core.messager.dochie.network;

import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import core.messager.dochie.helper.DochieLogManager;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

/**
 * Class Call_DochieNetworkProbe merupakan callback yang nantinya digunakan
 * untuk mengecek apakah network dalam keadaan down atau up dengan mekanisme
 * callback
 * 
 * @author Yatno Sudar
 * @version Dochie Messager 0.5 Build 7
 * */
public class Call_DochieNetworkProbe {

	private Socket s = null;
	private int delay = 5000;
	private Context c;

	/**
	 * Constructor dari class Call_DochieNetworkProbe. Construktor
	 * mendeklarasikan Class Socket dengan port 143
	 * 
	 * @param Context
	 *            context dari dimana class <code>Call_DochieNetworkProbe</code>
	 *            dipanggil
	 * */
	public Call_DochieNetworkProbe(Context c) {
		this.c = c;
		try {
			s = new Socket(core.messager.dochie.constant.Constants.IP_EMAIL,
					143);
		} catch (IOException e) {
			DochieLogManager.ERROR("Call DochieNetwork", "" + e.getMessage().toString());
		}
	}

	/**
	 * Method ini bertugas cek koneksi server dengan port 143 dan timeout.
	 * pengecekan 1 detik
	 * 
	 * @return boolean <b>true</b> jika koneksi tersedia <b>false</b> jika
	 *         koneksi tidak ada
	 */
    
	public boolean is_connect() {
		boolean connected = false;
		try {
			s.setSoTimeout(1000);
			connected = s.isConnected();
		} catch (Exception e) {
			connected = false;
		}
		return connected;
	}
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	/**
	 * method ini bertugas untuk melakukan cek koneksi dengan prinsip callback.
	 * Method ini memiliki parameter Abs_DochieNetworkProbe yang digunaakan
	 * untuk prosesi callbak.<br/> Isi dari methode cek_network adalah sebagai berikut : 
	 * <blockquote><pre>
	 *'//perulangan 
	 * while (true) {
	 * '//execption jika terjadi interup
	 *		try {
	 *'//cek apakah 
	 *			if (isOnline()) {
	 *				if (is_connect()) {
	 *					abs.ifNetworkUp();
	 *				} else {
	 *					abs.ifNetworkDown();
	 *				}
	 *			} else {
	 *				abs.ifNetworkDown("Internet Down");
	 *			}
	 *
	 *			Thread.sleep(delay);
	 *		} catch (InterruptedException e) {
	 *			DochieLogManager.ERROR("Call Dochie Network", e.getMessage().toString());
	 *		}
	 *	}
	 * </pre></blockquote>
	 * 
	 * @param Abs_DochieNetworkProbe
	 *            abstrac class yang digunakan untuk menjalankan fungsi callback
	 * */
	public void cek_network(final Abs_DochieNetworkProbe abs) {
		while (true) {
			try {
				if (isOnline()) {
					if (is_connect()) {
						abs.ifNetworkUp();
					} else {
						abs.ifNetworkDown();
					}
				} else {
					abs.ifNetworkDown("Internet Down");
				}

				Thread.sleep(delay);
			} catch (InterruptedException e) {
				DochieLogManager.ERROR("Call Dochie Network", e.getMessage().toString());
			}
		}
	}

}
