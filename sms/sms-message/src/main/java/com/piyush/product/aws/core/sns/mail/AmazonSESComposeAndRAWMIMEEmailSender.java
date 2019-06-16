/*
 * 
 */
package com.piyush.product.aws.core.sns.mail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

// These are from the JavaMail API, which you can download at https://java.net/projects/javamail/pages/Home. 
// Be sure to include the mail.jar library in your project. In the build order, mail.jar should precede the AWS SDK for Java library.
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.amazonaws.services.simpleemail.model.SendRawEmailResult;
import com.piyush.product.aws.core.sns.builder.Email;
import com.piyush.product.aws.core.sns.builder.NotificationClient;
import com.piyush.product.aws.core.sns.exception.SNSException;
import com.piyush.product.aws.core.sns.factory.SNSFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class AmazonSESComposeAndSendRAWMIMEEmail.
 */
public class AmazonSESComposeAndRAWMIMEEmailSender {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws AddressException
	 *             the address exception
	 * @throws MessagingException
	 *             the messaging exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws AddressException, MessagingException, IOException {
		String from = "piyush.mittal@ituple.com";
		String replyTo = "piyush.mittal@ituple.com";
		String bcc = "piyush.mittal@ituple.com";
		String cc = "piyush.mittal@ituple.com";
		String subject = "Amazon SES email test";
		String textBody = "This MIME email was sent through Amazon SES for testing Multi file attachment from s3";
		String htmlBody = "<h1>This MIME email was sent through Amazon SES for testing Multi file attachment from s3</h1>";

		Map<String, String> attachment1 = new HashMap();
		attachment1.put("file_name", "test.txt");
		attachment1.put("bucket_name", "pm311");

		Map<String, String> attachment2 = new HashMap();
		attachment2.put("file_name", "test.txt");
		attachment2.put("bucket_name", "pm311");

		Map<String, String> attachment3 = new HashMap();
		attachment3.put("file_name", "test.txt");
		attachment3.put("bucket_name", "pm311");

		List<Map<String, String>> jsonList = new ArrayList();
		jsonList.add(attachment1);
		jsonList.add(attachment2);
		jsonList.add(attachment3);

		AmazonSimpleEmailServiceClient client = SNSFactory
				.getAmazonSimpleEmailServiceClient(SNSFactory.getCredentials(), Regions.US_WEST_2);
		AmazonS3 s3Client = SNSFactory.getAmazonS3Client(SNSFactory.getCredentials(), Regions.US_WEST_2);

		sendRAWMail(client, s3Client, replyTo, bcc, cc, from, htmlBody, textBody, subject, jsonList);

	}

	/**
	 * Send.
	 *
	 * @param o
	 *            the o
	 * @throws SNSException
	 *             the SNS exception
	 * @throws AddressException
	 *             the address exception
	 * @throws MessagingException
	 *             the messaging exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static SendRawEmailResult send(Email o)
			throws SNSException, AddressException, MessagingException, IOException {

		AmazonSimpleEmailServiceClient client = null;
		AmazonS3 s3Client = null;
		NotificationClient nc = o.getNotificationClient();
		if (nc.getRegions() == null) {
			throw new SNSException("region can not be null");

		} else if (nc.getAwsCredential() == null) {

			client = SNSFactory.getAmazonSimpleEmailServiceClient(nc.getRegions());
			s3Client = SNSFactory.getAmazonS3Client(nc.getRegions());
		} else {

			client = SNSFactory.getAmazonSimpleEmailServiceClient(nc.getAwsCredential(), nc.getRegions());
			s3Client = SNSFactory.getAmazonS3Client(nc.getAwsCredential(), nc.getRegions());

		}

		return sendRAWMail(client, s3Client, String.join(",", o.getTo()),
				((o.getBcc() == null) ? null : String.join(",", o.getBcc())),
				((o.getCc() == null) ? null : String.join(",", o.getCc())), o.getFrom(), o.getHtmlBody(),
				o.getTextBody(), o.getSub(), o.getAttachments());
	}

	/**
	 * Send RAW mail.
	 *
	 * @param client
	 *            the client
	 * @param replyTo
	 *            the reply to
	 * @param bcc
	 *            the bcc
	 * @param cc
	 *            the cc
	 * @param from
	 *            the from
	 * @param htmlBody
	 *            the html body
	 * @param textBody
	 *            the text body
	 * @param subject
	 *            the subject
	 * @param attachment
	 *            the attachment
	 * @return
	 * @throws MessagingException
	 *             the messaging exception
	 * @throws AddressException
	 *             the address exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static SendRawEmailResult sendRAWMail(AmazonSimpleEmailServiceClient client, AmazonS3 s3Client,
			String replyTo, String bcc, String cc, String from, String htmlBody, String textBody, String subject,
			List<Map<String, String>> attachment) throws MessagingException, AddressException, IOException {

		Session session = Session.getDefaultInstance(new Properties());
		MimeMessage message = new MimeMessage(session);
		message.setSubject(subject, "UTF-8");
		message.setFrom(new InternetAddress(from));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(replyTo));

		if (cc != null && !cc.isEmpty()) {
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
		}
		if (bcc != null && !bcc.isEmpty()) {
			message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
		}
		MimeBodyPart wrap = new MimeBodyPart();
		MimeMultipart cover = new MimeMultipart("alternative");

		if (textBody == null) {
			textBody = "";
		}
		if (htmlBody == null) {
			htmlBody = "";
		}

		MimeBodyPart html = null;
		MimeBodyPart txt = null;
		if ((textBody.isEmpty() && htmlBody.isEmpty()) || (!textBody.isEmpty() && !htmlBody.isEmpty())) {
			html = new MimeBodyPart();
			txt = new MimeBodyPart();
			html.setContent(htmlBody, "text/html");
			txt.setContent(textBody, "text/plain");
			cover.addBodyPart(html);
			cover.addBodyPart(txt);
		} else if (textBody.isEmpty() && !htmlBody.isEmpty()) {
			html = new MimeBodyPart();
			html.setContent(htmlBody, "text/html");
			cover.addBodyPart(html);
		} else if (!textBody.isEmpty() && htmlBody.isEmpty()) {
			txt = new MimeBodyPart();
			txt.setContent(textBody, "text/plain");
			cover.addBodyPart(txt);
		}

		wrap.setContent(cover);

		MimeMultipart content = new MimeMultipart("related");
		message.setContent(content);
		content.addBodyPart(wrap);

		attach(s3Client, content, attachment);

		try {
			System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");
			PrintStream out = System.out;
			message.writeTo(out);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			message.writeTo(outputStream);
			RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));
			SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);
			System.out.println("Email sent!");
			return client.sendRawEmail(rawEmailRequest);

		} catch (Exception ex) {
			System.out.println("Email Failed");
			System.err.println("Error message: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Attach.
	 *
	 * @param credentials
	 *            the credentials
	 * @param content
	 *            the content
	 * @param attachmentList
	 *            the attachment list
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws MessagingException
	 *             the messaging exception
	 */
	private static void attach(AmazonS3 s3Client, MimeMultipart content, List<Map<String, String>> attachmentList)
			throws IOException, MessagingException {
		StringBuilder sb = new StringBuilder();

		for (Map<String, String> eachAttachmentMap : attachmentList) {

			String id = UUID.randomUUID().toString();
			sb.append("<img src=\"cid:");
			sb.append(id);
			sb.append("\" alt=\"ATTACHMENT\"/>\n");

			MimeBodyPart attachment = new MimeBodyPart();

			S3Object s3Obj = getFileFromS3(s3Client, eachAttachmentMap.get("bucket_name"),
					eachAttachmentMap.get("file_name"));

			DataSource fds = new ByteArrayDataSource(s3Obj.getObjectContent(),
					s3Obj.getObjectMetadata().getContentType());

			attachment.setDataHandler(new DataHandler(fds));
			attachment.setHeader("Content-ID", "<" + id + ">");
			attachment.setFileName(eachAttachmentMap.get("file_name"));

			content.addBodyPart(attachment);

		}

	}

	/**
	 * Gets the file from S 3.
	 *
	 * @param credentials
	 *            the credentials
	 * @param bucketName
	 *            the bucket name
	 * @param fileName
	 *            the file name
	 * @return the file from S 3
	 */
	private static S3Object getFileFromS3(AmazonS3 s3client, String bucketName, String fileName) {
		// AmazonS3 s3client =
		// AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_2).build();
		S3Object object = s3client.getObject(new GetObjectRequest(bucketName, fileName));
		return object;
	}

}