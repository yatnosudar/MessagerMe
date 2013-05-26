package core.messager.dochie.helper;

import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.content.Context;
import android.util.Log;

public class DochieSendMessageHelper {

	private Message message_mail;
	private Session session_mail;
	private DochiePreferenceHelper pref;

	public DochieSendMessageHelper(Context c) {

		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtp.host",
				core.messager.dochie.constant.Constants.IP_EMAIL);
		props.put("mail.smtp.port", "7538");
		props.put("mail.smtp.auth", "false");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.quitwait", "false");
		session_mail = Session.getDefaultInstance(props, null);
		pref = new DochiePreferenceHelper(c);
		// session_mail.setDebug(true);

	}

	public void sendMessage(String message_recip, String subject, String body) {

		try {
			MailcapCommandMap mc = (MailcapCommandMap) CommandMap
					.getDefaultCommandMap();
			mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
			mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
			mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
			mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
			mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
			CommandMap.setDefaultCommandMap(mc);

			// create a message
			message_mail = new MimeMessage(session_mail);
			message_mail.setFrom(new InternetAddress(pref.getNoHp() + "@"
					+ core.messager.dochie.constant.Constants.HOST_EMAIL));

			InternetAddress toAddress = new InternetAddress(message_recip);
			message_mail.addRecipient(Message.RecipientType.TO, toAddress);
			message_mail.setSubject(subject);

			Multipart mp = new MimeMultipart();

			BodyPart pixPart = new MimeBodyPart();
			pixPart.setContent(body, "text/html");

			mp.addBodyPart(pixPart);
			message_mail.setContent(mp);
			Transport.send(message_mail);
			

		} catch (MessagingException ex) {
			Log.e(DochieSendMessageHelper.this + "", ex.getMessage());
			System.err.println(ex);
			ex.printStackTrace(System.err);
		}
	}

	public void sendMessage_clone(String message_recip, String subject, String body) throws MessagingException {
			MailcapCommandMap mc = (MailcapCommandMap) CommandMap
					.getDefaultCommandMap();
			mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
			mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
			mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
			mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
			mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
			CommandMap.setDefaultCommandMap(mc);

			// create a message
			message_mail = new MimeMessage(session_mail);
			message_mail.setFrom(new InternetAddress(pref.getNoHp() + "@"
					+ core.messager.dochie.constant.Constants.HOST_EMAIL));

			InternetAddress toAddress = new InternetAddress(message_recip);
			message_mail.addRecipient(Message.RecipientType.TO, toAddress);
			message_mail.setSubject(subject);

			Multipart mp = new MimeMultipart();

			BodyPart pixPart = new MimeBodyPart();
			pixPart.setContent(body, "text/html");

			mp.addBodyPart(pixPart);
			message_mail.setContent(mp);
			Transport.send(message_mail);
	}
	
	public void sendMessageBroadcast(String[] message_recip, String subject,
			String body) {

		try {
			MailcapCommandMap mc = (MailcapCommandMap) CommandMap
					.getDefaultCommandMap();
			mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
			mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
			mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
			mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
			mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
			CommandMap.setDefaultCommandMap(mc);

			// create a message
			MimeMessage multiReceive = new MimeMessage(session_mail);
			multiReceive.setFrom(new InternetAddress(pref.getNoHp() + "@"
					+ core.messager.dochie.constant.Constants.HOST_EMAIL));

			
			String currentDateTimeString = DateFormat.getDateTimeInstance()
					.format(new Date());
			String pesanData = pref.getNoHp()+"|"+body+"|"+currentDateTimeString;
			
			StringBuffer receiver = new StringBuffer();
			for (int i = 0; i < message_recip.length; i++) {
				receiver.append(message_recip[i]+"@"+core.messager.dochie.constant.Constants.HOST_EMAIL);
				if (i < message_recip.length - 1) {
					receiver.append(", ");
				}
			}
            
			String re = receiver.toString();
			Log.i("user for email ", re);
			
			multiReceive.setRecipients(Message.RecipientType.CC, re);
			multiReceive.setSubject(subject);

			Multipart mp = new MimeMultipart();

			BodyPart pixPart = new MimeBodyPart();
			pixPart.setContent(pesanData, "text/html");

			mp.addBodyPart(pixPart);
			multiReceive.setContent(mp);
			Transport.send(multiReceive);

		} catch (MessagingException ex) {
			Log.e(DochieSendMessageHelper.this + "", ex.getMessage());
			System.err.println(ex);
			ex.printStackTrace(System.err);
		}
	}

}
