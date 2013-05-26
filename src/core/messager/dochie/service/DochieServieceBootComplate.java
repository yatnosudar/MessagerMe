package core.messager.dochie.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;

public class DochieServieceBootComplate extends BroadcastReceiver{

	
	@Override
	public void onReceive(Context context, Intent intent) {
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
		
		//Intent service = new Intent(context, DochieServiceImapIdle.class);
		//context.startService(service);
		Intent service2 = new Intent(context,DochieServiceGettingEmail.class);
		context.startService(service2); 
	}

}
