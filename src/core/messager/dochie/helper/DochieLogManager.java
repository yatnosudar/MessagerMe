package core.messager.dochie.helper;

import android.util.Log;

public class DochieLogManager {

	public static boolean STATUSLOG = true;
	
	public static void INFORMATION(String tag, String msg){
		if (STATUSLOG) {
			Log.i(tag, msg);
		}
	}
	public static void WARNING(String tag, String msg){
		if (STATUSLOG) {
			Log.w(tag, msg);
		}
	}
	public static void ERROR(String tag, String msg){
		if (STATUSLOG) {
			Log.e(tag, msg);
		}
	}public static void DEBUG(String tag, String msg){
		if (STATUSLOG) {
			Log.d(tag, msg);
		}
	}
}
