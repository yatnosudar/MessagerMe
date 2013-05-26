package core.messager.dochie.network;

/**
 * abstract class yang digunakan untuk melakan aktifitas koneksi antara server
 * dan client
 * 
 * @author Yatno Sudar
 * @version Dochie Messager 0.5 Build 7
 */
public abstract class Abs_DochieNetworkProbe {

	/**
	 * Method ini digunakan jika network dalam keadaaan Down
	 * */
	public void ifNetworkDown() {
	}

	/** Method ini digunanakan jika network dalam keadaan up */
	public void ifNetworkUp() {
	}

	/**
	 * Method ini digunakan jika network dalam keadaan down menampilkan perintah
	 * error
	 * 
	 * @param Stering exeption error
	 */
	public void ifNetworkDown(String e) {
	}
}
