package com.cyperts.ExcellML.FileOperations;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailIntegration {
	public static void main(String[] args) {

		System.out.println("preparing to send messages....");
		String msg = "Hello dear,this message is for security check";
		String subject = "CodesArea: Confirmation";
		String to = "dhanashrighule63@gmail.com";
		String from = "dhanashrighule7@gmail.com";

		sendMail(msg, subject, to, from);
	}

	private static void sendMail(String msg, String subject, String to, String from) {
		// String host = "smtp.gmail.com";

		// get the system properties
		Properties properties = System.getProperties();
		System.out.println("Properties :" + properties);
		// setting imp info to properties obj
		// host set
		properties.put("mail.smpt.host", "smtp.gmail.com");
		properties.put("mail.smpt.port", "587");
		properties.put("mail.smpt.starttls.enable", "true");
		properties.put("mail.smpt.auth", "true");

		// 1.to get the session object
		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("techinfoo26@gmail.com", "TechInfo123");
			}

		});
		session.setDebug(true);

		// 2.compose message [text , multi media]
		MimeMessage m = new MimeMessage(session);
		try {
			m.setFrom(from);

			// adding recipient to message
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			// adding subject to message
			m.setSubject(subject);
			// adding text to message
			m.setText(msg);
			// 3.Send the message using transport class

			Transport.send(m);
			System.out.println("Sent success............");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
