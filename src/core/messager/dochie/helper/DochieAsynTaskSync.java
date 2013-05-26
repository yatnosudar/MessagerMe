package core.messager.dochie.helper;

import android.content.ContentResolver;
import android.os.AsyncTask;
import android.os.Bundle;

public class DochieAsynTaskSync extends AsyncTask<String, Void, Boolean>{

	@Override
	protected Boolean doInBackground(String... params) {
			Bundle bundle = new Bundle();
			bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
			bundle.putBoolean(ContentResolver.SYNC_EXTRAS_FORCE, true);
			bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
			ContentResolver.requestSync(null, "com.android.contacts", bundle);
		return null;
	}

}
