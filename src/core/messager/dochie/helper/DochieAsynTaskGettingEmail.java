package core.messager.dochie.helper;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

import org.jsoup.Jsoup;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import core.messager.dochie.PesanContactActivity;
import core.messager.dochie.PesanActivity;
import core.messager.dochie.bean.PesanDochie;
import core.messager.dochie.bean.UserDochie;
import core.messager.dochie.constant.StaticVariable;
import core.messager.dochie.model.ModelPesanDochie;
import core.messager.dochie.model.ModelUserDochie;
import core.messager.dochie.model.ModelUserPesanDochie;

/**
 * Class background proccessing untuk mengunduh email baru dari mail server.
 * Class ini dijalankan pada saat service IMAPIdle menerima email baru. Class
 * ini extends dari class AsynTask running
 * 
 * @author Yatno Sudar
 * 
 */
public class DochieAsynTaskGettingEmail extends
		AsyncTask<Message[], Integer, Boolean> {

	PesanDochie parseObject;
	Context c;
	int totalMessage = 0;

	/**
	 * Constructor dari DochieAsynTaskGettingEmail. 
	 * 
	 * @param c
	 *            Context dari class yang memanggil DochieAsynTaskGettingEmail
	 */
	public DochieAsynTaskGettingEmail(Context c) {
		super();
		this.c = c;
	}

	/**
	 * Method override dari class AsynTask. Method ini berfungsi sebagai
	 * pengolahan pesan dari mail server yang sudah di unduh dari mail server,
	 * untuk disimpan dalam presisten sqlite dan melakukan broadcast message ke
	 * activity jika ada pesan baru. Mekanisme dari class ini adalah sebagai
	 * berikut
	 * <ul>
	 * <li>Mendapatkan Message baru dari mail server</li>
	 * <li>Melakukan perulangan berdasarkan banyaknya pesan yang di unduh untuk
	 * mendapatkan Multipart dari pesan tersebut</li>
	 * <li>Melakukan pengecekan jika pesan tersebut terdapat attachment atau
	 * tidak</li>
	 * <li>Jika tidak maka pesan tersebut di olah dan di masukan kedalam sqlite</li>
	 * <li>Memberikan notifikasi ke activity yang di tuju dengan secara
	 * broadcast</li>
	 * <li>Destroy class dan koneksi sqlite jika proses telah selesai</li>
	 * </ul>
	 * 
	 * 
	 */
	@Override
	protected Boolean doInBackground(Message[]... params) {

		Message[] message = params[0];
		totalMessage = message.length;
		ModelPesanDochie mpesan = new ModelPesanDochie(c);
		Flags seen = new Flags(Flags.Flag.SEEN);
		mpesan.open();

		try {
			for (int i = 0; i < message.length; i++) {
				try {
					Multipart multipart = (Multipart) message[i].getContent();

					for (int x = 0; x < multipart.getCount(); x++) {
						BodyPart bodyPart = multipart.getBodyPart(x);

						String disposition = bodyPart.getDisposition();

						if (disposition != null
								&& (disposition.equals(BodyPart.ATTACHMENT))) {
							System.out.println("Mail have some attachment : ");

							DataHandler handler = bodyPart.getDataHandler();
							System.out.println("file name : "
									+ handler.getName());
						} else {

							String dataEmail = Jsoup.parse(
									bodyPart.getContent().toString()).text();
							StringTokenizer t = new StringTokenizer(dataEmail,
									"|");
							String nohp = t.nextToken();
							String isiPesan = t.nextToken();
							String timePesan = t.nextToken();

							Log.d("report iniUser", ":" + mpesan.isUser(nohp));

							// cek user sebelum menambah pesan
							if (mpesan.isUser(nohp)) {
								parseObject = mpesan.createMessage2(nohp,
										isiPesan, "", timePesan, 1, 0, 0, 1);
							} else {
								ModelUserDochie md = new ModelUserDochie(c);
								md.open();
								md.createMessage(
										nohp,
										nohp
												+ "@"
												+ core.messager.dochie.constant.Constants.HOST_EMAIL,
										nohp, null, 1);
								md.close();
								parseObject = mpesan.createMessage2(nohp,
										isiPesan, "", timePesan, 1, 0, 0, 1);
							}

							StaticVariable.setPd(parseObject);
							Intent iz = new Intent(
									"android.intent.action.PESAN").putExtra(
									"some_msg", parseObject.get_idUser());
							c.sendBroadcast(iz);
							break;
						}
					}

				} catch (MessagingException mex) {
					Log.e(DochieAsynTaskGettingEmail.class
							+ "MessagingExeption", mex.getMessage());
				} catch (IOException e) {
					Log.e(DochieAsynTaskGettingEmail.class + "IOException",
							e.getMessage());
				}

				message[i].setFlags(seen, true);
			}
		} catch (Exception e) {
			Log.e(DochieAsynTaskGettingEmail.class + "Exception",
					"Error Getting Email");
		} finally {
			mpesan.close();
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);

	}

}