package com.gw.utils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendmailUtil {
	
	// ���÷�����
	private static String KEY_SMTP = "mail.smtp.host";
	private static String VALUE_SMTP = "smtp.qq.com";
	// ��������֤
	private static String KEY_PROPS = "mail.smtp.auth";
	private static boolean VALUE_PROPS = true;
	// �������û���������
	private String SEND_USER = "2569000943@qq.com";
	private String SEND_UNAME = "2569000943";
	private String SEND_PWD = "********";
	// �����Ự
	private MimeMessage message;
	private Session s;

	/*
	 * ��ʼ������
	 */
	public SendmailUtil() {
		Properties props = System.getProperties();
		props.setProperty(KEY_SMTP, VALUE_SMTP);
		props.put(KEY_PROPS, "true");
		//props.put("mail.smtp.auth", "true");
		s =  Session.getDefaultInstance(props, new Authenticator(){
		      protected PasswordAuthentication getPasswordAuthentication() {
		          return new PasswordAuthentication(SEND_UNAME, SEND_PWD);
		      }});
		s.setDebug(true);
		message = new MimeMessage(s);
	}

	/**
	 * �����ʼ�
	 * 
	 * @param headName
	 *            �ʼ�ͷ�ļ���
	 * @param sendHtml
	 *            �ʼ�����
	 * @param receiveUser
	 *            �ռ��˵�ַ
	 */
	public void doSendHtmlEmail(String headName, String sendHtml,
			String receiveUser) {
		try {
			// ������
			InternetAddress from = new InternetAddress(SEND_USER);
			message.setFrom(from);
			// �ռ���
			InternetAddress to = new InternetAddress(receiveUser);
			message.setRecipient(Message.RecipientType.TO, to);
			// �ʼ�����
			message.setSubject(headName);
			String content = sendHtml.toString();
			// �ʼ�����,Ҳ����ʹ���ı�"text/plain"
			message.setContent(content, "text/html;charset=GBK");
			message.saveChanges();
			Transport transport = s.getTransport("smtp");
			// smtp��֤���������������ʼ��������û�������
			transport.connect(VALUE_SMTP, SEND_UNAME, SEND_PWD);
			// ����
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			System.out.println("send success!");
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SendmailUtil se = new SendmailUtil();
		se.doSendHtmlEmail("�ʼ�ͷ�ļ���", "�ʼ�����", "zhangyijin@gw.com.cn");
	}
}//Դ����Ƭ�������ƴ���http://yuncode.net
			