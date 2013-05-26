package core.messager.dochie.network;


import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

import org.apache.http.protocol.HTTP;

import android.util.Log;

/***/
public class DochieNetworkProbe {

	private boolean connectSocket = false;

	public DochieNetworkProbe() {
		super();

	}
	//cek http connection json
	public boolean isConnectedToServer(String url, int timeout) {
		//Log.i("host server", url);
	    try{
	    	URL urli = new URL(url);
	    	HttpURLConnection urlConnection = 
	    			(HttpURLConnection) urli.openConnection();
	    	urlConnection.setConnectTimeout(timeout);
	        int getRepsn = urlConnection.getResponseCode();
	        if (getRepsn==HttpURLConnection.HTTP_OK) {
				return true;
			}else{
				urlConnection.disconnect();
				return false;
			}
	    } catch (Exception e) {
	    	return false;
	    }
	}
	//cek socket server imap
	public boolean cekSocket() {
		try {
			Socket s = new Socket(
					core.messager.dochie.constant.Constants.IP_EMAIL, 143);
			s.setSoTimeout(1000);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}

	public void cekSocket80() {
		try {
			Socket s = new Socket(
					core.messager.dochie.constant.Constants.IP_EMAIL, 8080);
			s.setSoTimeout(1000);
			setConnectSocket(true);
		} catch (Exception e) {
			setConnectSocket(false);
		}
	}

	public boolean isConnectSocket() {
		return connectSocket;
	}

	public void setConnectSocket(boolean connectSocket) {
		this.connectSocket = connectSocket;
	}

}
