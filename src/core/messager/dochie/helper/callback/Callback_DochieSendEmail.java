package core.messager.dochie.helper.callback;

import core.messager.dochie.constant.StaticVariable;
import core.messager.dochie.helper.DochieSendMessageHelper;
import android.content.Context;

/**
 * Digunakan sebagai callback pengiriman email
 * 
 * @author Yatno Sudar
 * @version Dochie Messager 0.5 Build 7
 */
public class Callback_DochieSendEmail {
	Context c;
	private DochieSendMessageHelper dsmh;

	/**
	 * Constructor yang digunakan untuk mendeklarasikan variable Context dan
	 * <b>DochieSendMessageHelper</b>
	 * 
	 * @param Context
	 *            context dari mana class ini dipanggil
	 */
	public Callback_DochieSendEmail(Context c) {
		this.c = c;
		dsmh = new DochieSendMessageHelper(c);
	}

	/**
	 * Method yang digunakan untuk mengirim email
	 * 
	 * <pre>
	 * <blockquote>if (StaticVariable.NETWORK_UP) {
	 * 			try {
	 * 				dsmh.sendMessage_clone(message_recip, "From Dochie", body);
	 * 				cs.s_sendEmail(message_recip, body);
	 * 			} catch (Exception e) {
	 * 				cs.b_sendEmail(message_recip, body, e.getMessage().toString());
	 * 			}
	 * 		} else {
	 * 			cs.f_sendEmail(message_recip, body);
	 * 		}</blockquote>
	 * </pre>
	 * 
	 * @param String
	 *            penerima email
	 * @param String
	 *            body email
	 * @param Abstract_DochieSendEmail
	 *            abstract class yang berfungsi sebagai callback
	 */
    
	public void kirimEmailAbstract(String message_recip, String body,
			Abstract_DochieSendEmail cs) {
		if (StaticVariable.NETWORK_UP) {
			try {
				dsmh.sendMessage_clone(message_recip, "From Dochie", body);
				cs.s_sendEmail(message_recip, body);
			} catch (Exception e) {
				cs.b_sendEmail(message_recip, body, e.getMessage().toString());
			}
		} else {
			cs.f_sendEmail(message_recip, body);
		}
	}

	/**
	 * Method yang digunakan untuk mengirim email secara broadcast
	 * 
	 * <pre>
	 * <blockquote>if (StaticVariable.NETWORK_UP) {
	 * 			try {
	 * 				dsmh.sendMessageBroadcast(toEmail, "Broadcast Message",
	 * 						bodyEmail);
	 * 				cs.s_sendEmail(toEmail, bodyEmail);
	 * 			} catch (Exception e) {
	 * 				cs.b_sendEmail(toEmail, bodyEmail, e.getMessage().toString());
	 * 			}
	 * 		} else {
	 * 			cs.f_sendEmail(toEmail, bodyEmail);
	 * 		}</blockquote>
	 * </pre>
	 * 
	 * @param String
	 *            [] penerima email
	 * @param String
	 *            [] body email
	 * @param Abstract_DochieSendEmail
	 *            abstract class yang berfungsi sebagai callback
	 */
	public void kirimEmailAbstractGroub(String[] toEmail, String bodyEmail,
			Abstract_DochieSendEmail cs) {
		if (StaticVariable.NETWORK_UP) {
			try {
				dsmh.sendMessageBroadcast(toEmail, "Broadcast Message",
						bodyEmail);
				cs.s_sendEmail(toEmail, bodyEmail);
			} catch (Exception e) {
				cs.b_sendEmail(toEmail, bodyEmail, e.getMessage().toString());
			}
		} else {
			cs.f_sendEmail(toEmail, bodyEmail);
		}

	}

}
