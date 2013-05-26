package core.messager.dochie.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import core.messager.dochie.R;

public class DochieNotificationHelper {
	int icon = R.drawable.mail_receive;
	long when = System.currentTimeMillis();
	
	String ns = "";
	
	NotificationManager mNotificationManager;
	Notification notification;

	CharSequence tickerText = "Dochie Messager";
	CharSequence contentTitle, contentText;
	
	Intent notificationIntent;
	PendingIntent contentIntent;

	MediaPlayer player;
	
	public DochieNotificationHelper() {
		super();
	}

	public void notifStart(Context c, Class<?> d, int totalMessage) {
		ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) c.getSystemService(ns);
		notification = new Notification(icon, tickerText, when);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		Context context = c.getApplicationContext();
		contentTitle = "Dochie Messager";
		contentText = totalMessage + " new messages";
		notificationIntent = new Intent(c, d);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		contentIntent = PendingIntent.getActivity(c, 0, notificationIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		notification.number += 1;
		mNotificationManager.notify(0, notification);
		
		
		player = MediaPlayer.create(c, R.drawable.notify);
		player.setLooping(false);
		player.start();
	}
	
	
}
