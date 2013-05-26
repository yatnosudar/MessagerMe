package core.messager.dochie.helper;

import java.util.Properties;

import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * Digunakan untuk melakukan pengecekan otentikasi emal ke mail server
 */
public class DochieAccountAuth {
	private Properties props_email;
	private Session sesi_email;
	private Store store_email;
	//private boolean authSuccess = false;

	String username = "";
	String password = "";

	/**Construtor digunakna untuk mendeklarasikan variable String username dan password.
	 * 
	 * @param username 
	 * @param password
	 */
	public DochieAccountAuth(String username, String password) {
		super();
		Log.w(username, password);
		this.username = username;
		this.password = password;
	}

	/**Method dengan return boolean untuk pengecekan otentikasi email
	 * 
	 * @return boolean jika true otentikasi berhasil jika false otentikasi gagal
	 */
	public boolean authEmail() {
		try {
			props_email = System.getProperties();
			sesi_email = Session.getInstance(props_email);
			store_email = sesi_email.getStore("imap");
			store_email.connect(
					core.messager.dochie.constant.Constants.IP_EMAIL, username,
					password);
			//setAuthSuccess(store_email.isConnected());
			return true;
		} catch (Exception e) {
			Log.w(DochieAccountAuth.this + "", e.getMessage() + "");
			return false;
		}

	}

//	
//	public boolean isAuthSuccess() {
//		return authSuccess;
//	}
//
//	public void setAuthSuccess(boolean authSuccess) {
//		this.authSuccess = authSuccess;
//	}
}
