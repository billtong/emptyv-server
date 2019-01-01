package com.empty.util;

import com.empty.entity.UserEntity;

import java.util.Date;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class MailUtil {

	// 网站官方邮箱
	public static final String myEmailAccount = "emptyvideo@outlook.com";
	public static final String myEmailPassword = "$$tzy$$990226";
	public static final String myEmailSMTPHost = "smtp.office365.com";
	public static final int myEmailConectionMaxTimeout =  1000*60*1; 
	public static final int myEmailSMTPPort = 587; 
	
	
	public static final String serverUrl = "http://localhost:8080/empty-server/api/user/activated?code=";
	

	//Encryption method: STARTTLS
			
	public static String generateRegisterMailBody(UserEntity userEntity) {
	
		String code = DataTools.encode(userEntity.getUserEmail());
		
		String body = "<div>please click the link to finish your final step!</div>"+"<div align='center'> <a href='"+ serverUrl+code+"'>ENTER</a></div>"; 
		return body;
	}

	public static void sendTo(String body, String receiveMailAccount) throws Exception {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", myEmailSMTPHost);
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.starttls.enable", "true");
		//如果网络连接超过三分钟 断掉
		props.setProperty("mail.smtp.connectiontimeout", String.valueOf(myEmailConectionMaxTimeout));

		props.setProperty("mail.smtp.port", String.valueOf(myEmailSMTPPort));
		
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true);

		Transport transport = session.getTransport();
		MimeMessage message = createMimeMessage(session, myEmailAccount, receiveMailAccount, body);

		transport = session.getTransport();
		transport.connect(myEmailAccount, myEmailPassword);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}

	public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail, String body)
			throws Exception {

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(sendMail, "LB", "UTF-8"));
		message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "My Friend", "UTF-8"));
		message.setSubject("EmptyVideo Start Your New Account - 自站长 B.T 的邀请", "UTF-8");
		message.setContent(body, "text/html;charset=UTF-8");
		message.setSentDate(new Date());
		message.saveChanges();

		return message;
	}
}
