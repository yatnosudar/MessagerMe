package core.messager.dochie.service;

import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import org.jsoup.Jsoup;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import core.messager.dochie.PesanContactActivity;
import core.messager.dochie.PesanActivity;
import core.messager.dochie.bean.PesanDochie;
import core.messager.dochie.constant.StaticVariable;
import core.messager.dochie.helper.DochieNotificationHelper;
import core.messager.dochie.helper.DochiePreferenceHelper;
import core.messager.dochie.model.ModelPesanDochie;
import core.messager.dochie.model.ModelUserDochie;

public class DochieServiceGettingEmail extends Service {
	DochieNotificationHelper notif;
	PesanDochie parseObject;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

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
		
		new prossGetting().execute("berjalan");
		Log.d("DochieServiceGettingEmail", "Getting Email");

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	private class prossGetting extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			DochiePreferenceHelper dph = new DochiePreferenceHelper(
					DochieServiceGettingEmail.this);
			ModelPesanDochie mpesan = new ModelPesanDochie(
					DochieServiceGettingEmail.this);

			Log.e("ini korban " + dph.getNoHp() + '@'
					+ core.messager.dochie.constant.Constants.HOST_EMAIL,
					dph.getSerialSIM());

			mpesan.open();
			try {
				MailcapCommandMap mc = (MailcapCommandMap) CommandMap
						.getDefaultCommandMap();
				mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
				mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
				mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
				mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
				mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
				CommandMap.setDefaultCommandMap(mc);

				Properties properties = System.getProperties();
				Session session = Session.getInstance(properties);

				Flags seen = new Flags(Flags.Flag.SEEN);
				FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
				Store store = session.getStore("imap");
				store.connect(
						core.messager.dochie.constant.Constants.IP_EMAIL,
						dph.getNoHp()
								+ '@'
								+ core.messager.dochie.constant.Constants.HOST_EMAIL,
						dph.getSerialSIM());

				Folder folder = store.getFolder("INBOX");
				folder.open(Folder.READ_WRITE);
				Message[] message = folder.search(unseenFlagTerm);

				for (int i = 0; i < message.length; i++) {
					try {

						Multipart multipart = (Multipart) message[i]
								.getContent();

						for (int x = 0; x < multipart.getCount(); x++) {
							BodyPart bodyPart = multipart.getBodyPart(x);

							String disposition = bodyPart.getDisposition();

							if (disposition != null
									&& (disposition.equals(BodyPart.ATTACHMENT))) {
								System.out
										.println("Mail have some attachment : ");

								DataHandler handler = bodyPart.getDataHandler();
								System.out.println("file name : "
										+ handler.getName());
							} else {

								String dataEmail = Jsoup.parse(
										bodyPart.getContent().toString())
										.text();
								StringTokenizer t = new StringTokenizer(
										dataEmail, "|");

								String nohp = t.nextToken();
								String isiPesan = t.nextToken();
								String timePesan = t.nextToken();

								Log.d("report iniUser",
										":" + mpesan.isUser(nohp));

								if (mpesan.isUser(nohp)) {
									parseObject = mpesan
											.createMessage2(nohp, isiPesan, "",
													timePesan, 1, 0, 0, 1);
								} else {
									ModelUserDochie md = new ModelUserDochie(
											DochieServiceGettingEmail.this);
									md.open();
									md.createMessage(
											nohp,
											nohp
													+ "@"
													+ core.messager.dochie.constant.Constants.HOST_EMAIL,
											nohp, null, 1);
									md.close();
									parseObject = mpesan
											.createMessage2(nohp, isiPesan, "",
													timePesan, 1, 0, 0, 1);
								}

								notif = new DochieNotificationHelper();
								notif.notifStart(
										DochieServiceGettingEmail.this,
										PesanContactActivity.class,
										message.length);

								StaticVariable.setPd(parseObject);
								Intent iz = new Intent(
										"android.intent.action.PESAN")
										.putExtra("some_msg", "zea");
								sendBroadcast(iz);
								break;
							}
						}

					} catch (MessagingException mex) {
						Log.e(DochieServiceGettingEmail.class
								+ "MessagingException ", mex.getMessage());
					} catch (IOException e) {
						Log.e(DochieServiceGettingEmail.class + "IOException ",
								e.getMessage());
					}

					message[i].setFlags(seen, true);
				}

				folder.close(true);
				store.close();
			} catch (Exception e) {
				Log.e("ini korban " + dph.getNoHp() + '@'
						+ core.messager.dochie.constant.Constants.HOST_EMAIL,
						dph.getSerialSIM());
				Log.e(DochieServiceGettingEmail.class + "Exception ",
						e.getMessage());
				mpesan.close();
			}
			mpesan.close();
			return null;
		}

		@Override
		protected void onPreExecute() {
			stopSelf();
			super.onPreExecute();
		}

	}
}
