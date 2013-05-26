package core.messager.dochie.helper.callback;

import core.messager.dochie.helper.DochieLogManager;
import android.util.Log;

/**
 * Merupakan Abstract class yang digunakan untuk mekanisme pengiriman email.
 * 
 * @author Yatno Sudar
 * @version Dochie Messager 0.5 Build 7
 * 
 */
public abstract class Abstract_DochieSendEmail {

	/**
	 * Method yang menangani jika pengiriman email berhasil. Method ini
	 * digunakan untuk mengirimkan pesan secara broadcast
	 * 
	 * @param String
	 *            array dari tujuan pengiriman email
	 * @param String
	 *            isi dari email
	 */
	public void s_sendEmail(String array[], String body) {
		DochieLogManager.INFORMATION("Success Send Email", "Message Broadcast");
	}

	/**
	 * Method yang menangani jika pengiriman email gagal karena koneksi. Method
	 * ini digunakan untuk mengirimkan pesan secara broadcast
	 * 
	 * @param String
	 *            array dari tujuan pengiriman email
	 * @param String
	 *            isi dari email
	 */
	public void f_sendEmail(String array[], String body) {
		DochieLogManager.ERROR("Network Problem ", "Message Broadcast");
	}

	/**
	 * Method yang menangani jika pengiriman email gagal karena exception dari
	 * sistem. Method ini digunakan untuk mengirimkan pesan secara broadcast
	 * 
	 * @param String
	 *            array dari tujuan pengiriman email
	 * @param String
	 *            isi dari email
	 * @param String
	 *            informasi error
	 */
	public void b_sendEmail(String array[], String body, String e) {
		DochieLogManager.ERROR("Sistem", "Message Broadcast" + e);
	}

	/**
	 * Method yang menangani jika pengiriman email berhasil.
	 * 
	 * @param String
	 *            array dari tujuan pengiriman email
	 * @param String
	 *            isi dari email
	 */
	public void s_sendEmail(String toMessage, String bodyMessage) {
		DochieLogManager.INFORMATION("Success Send Email", "Message Broadcast");
	}

	/**
	 * Method yang menangani jika pengiriman email gagal karena koneksi.
	 * 
	 * @param String
	 *            array dari tujuan pengiriman email
	 * @param String
	 *            isi dari email
	 */
	public void f_sendEmail(String toMessage, String bodyMessage) {
		DochieLogManager.ERROR("Network Problem ", "Message Broadcast");
	}

	/**
	 * Method yang menangani jika pengiriman email gagal karena exception dari
	 * sistem.
	 * 
	 * @param String
	 *            array dari tujuan pengiriman email
	 * @param String
	 *            isi dari email
	 * @param String
	 *            informasi error
	 */
	public void b_sendEmail(String toMessage, String bodyMessage, String e) {
		DochieLogManager.ERROR("Sistem", "Message Broadcast" + e);
	}

}
