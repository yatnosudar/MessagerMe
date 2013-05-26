package core.messager.dochie.service;

import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

import com.sun.mail.imap.IMAPFolder;

import core.messager.dochie.PesanContactActivity;
import core.messager.dochie.helper.DochieAsynTaskGettingEmail;
import core.messager.dochie.helper.DochieNotificationHelper;
import core.messager.dochie.helper.DochiePreferenceHelper;

public class DochieServiceImapIdle extends Service {
	DochieNotificationHelper notifMessage;
	DochiePreferenceHelper dph;
	bgprocesspush p ;
	
	public DochieServiceImapIdle() {
		p = new  bgprocesspush();
	}
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
		
		dph = new DochiePreferenceHelper(DochieServiceImapIdle.this);
		p.execute("imap",
				core.messager.dochie.constant.Constants.IP_EMAIL, dph.getNoHp()
						+ '@'
						+ core.messager.dochie.constant.Constants.HOST_EMAIL,
				dph.getSerialSIM(), "INBOX", "1000");
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private class bgprocesspush extends AsyncTask<String, String, String> {
		
		Store store_email = null;
		IMAPFolder f = null;
		
		@Override
		protected String doInBackground(String... params) {
			Log.i("Imap IDLE running Service", params[0]);

			try {
                
				MailcapCommandMap mc = (MailcapCommandMap) CommandMap
						.getDefaultCommandMap();
				mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
				mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
				mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
				mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
				mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
				CommandMap.setDefaultCommandMap(mc);

				Properties props_email = System.getProperties();
				Session sesi_email = Session.getInstance(props_email, null);
				store_email = sesi_email.getStore(params[0]);
				store_email.connect(params[1], params[2], params[3]);
				Folder folder_email = store_email.getFolder(params[4]);
				folder_email.open(Folder.READ_WRITE);
				
				folder_email.addMessageCountListener(new MessageCountAdapter() {
					public void messagesAdded(MessageCountEvent ev) {

						Message[] message = ev.getMessages();
						DochieAsynTaskGettingEmail datg = new DochieAsynTaskGettingEmail(
								DochieServiceImapIdle.this);
						datg.execute(message);

						DochieNotificationHelper notif = new DochieNotificationHelper();
						notif.notifStart(DochieServiceImapIdle.this,
								PesanContactActivity.class, message.length);
					}
				});

				boolean supportsIdle = true;
				try {

					if (folder_email instanceof IMAPFolder) {
						f = (IMAPFolder) folder_email;
						f.idle();
						supportsIdle = true;
					}
				} catch (FolderClosedException fex) {
					Log.d("error fex", fex.getMessage().toString());
					throw fex;

				} catch (MessagingException mex) {
					Log.d("error mex", mex.getMessage().toString());
					supportsIdle = false;
				}

				for (;;) {
					int ix = 0;

					if (supportsIdle && folder_email instanceof IMAPFolder) {
						f = (IMAPFolder) folder_email;
						f.idle();
						Log.d("Thread IDLE", "IDLE DONE");
						ix = 1;

					} else {
						Log.d("Thread Sleep", "IDLE SLEEP");
						ix = 2;
					}
					Log.i("Info Thread Idle", "" + ix);
				}
				
				
				

			} catch (Exception e) {
				Log.e("error imap idle", "Error Imap IDLE");
				//stopSelf();
			}
			return "finish";
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.equals("finish")) {
				Log.i("IMAP IDLE", "finish");
			}
			super.onPostExecute(result);
		}

	}
}
