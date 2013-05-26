package core.messager.dochie.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

public class ContactSyncService extends Service {
	private static final Object sSyncAdapterLock = new Object();
	private static ContactSyncAdapter sSyncAdapter = null;
	
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
		
		synchronized (sSyncAdapterLock) {
			if (sSyncAdapter == null) {
                sSyncAdapter = new ContactSyncAdapter(getApplicationContext(), true);
            }
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		 return sSyncAdapter.getSyncAdapterBinder();
	}

}
