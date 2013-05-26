package core.messager.dochie.constant;

import core.messager.dochie.bean.PesanDochie;
import core.messager.dochie.bean.UserDochie;
import core.messager.dochie.network.DochieNetworkProbe;

public class StaticVariable {
	public static PesanDochie pd;
	public static UserDochie uc;
	
	
	public static boolean NETWORK_UP = false;
	public static boolean NETWORK_DOWN = false;
	
	public static int STAT_TIMEOUT = 1000;
	
	public static PesanDochie getPd() {
		return pd;
	}
	public static void setPd(PesanDochie pd) {
		StaticVariable.pd = pd;
	}
	
	public static UserDochie getUc() {
		return uc;
	}
	public static void setUc(UserDochie uc) {
		StaticVariable.uc = uc;
	}
	
}
