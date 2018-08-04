package com.amazonaws.demo.sns.mail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
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

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AmazonSESComposeAndSendRAWMIMEEmail3 {

	public static void main(String[] args) throws AddressException, MessagingException, IOException {
		String EMAIL_FROM = "piyush.mittal@ituple.com";

		String EMAIL_REPLY_TO = "piyush.mittal@ituple.com";
		String BCC = "piyush.mittal@ituple.com";
		String CC = "piyush.mittal@ituple.com";

		String EMAIL_RECIPIENT = "piyush.mittal@ituple.com";

		String EMAIL_ATTACHMENTS = "test.txt";

		Regions AWS_REGION = Regions.US_EAST_1;

		String EMAIL_SUBJECT = "Amazon SES email test";
		String EMAIL_BODY_TEXT = "This MIME email was sent through Amazon SES for testing Multi file attachment from s3";

		JsonObject attachment1 = new JsonObject();
		attachment1.addProperty("file_name", "test.txt");
		attachment1.addProperty("bucket_name", "pm311");

		JsonObject attachment2 = new JsonObject();
		attachment2.addProperty("file_name", "test.txt");
		attachment2.addProperty("bucket_name", "pm311");

		JsonObject attachment3 = new JsonObject();
		attachment3.addProperty("file_name", "test.txt");
		attachment3.addProperty("bucket_name", "pm311");

		JsonArray jarr = new JsonArray();
		jarr.add(attachment1);
		jarr.add(attachment2);
		jarr.add(attachment3);

		// sendRAWMail(EMAIL_FROM, EMAIL_REPLY_TO, EMAIL_RECIPIENT,
		// EMAIL_ATTACHMENTS, EMAIL_SUBJECT, EMAIL_BODY_TEXT,new JsonArray());
		sendRAWMail(EMAIL_REPLY_TO, BCC, CC, EMAIL_FROM, "html body", EMAIL_BODY_TEXT, EMAIL_SUBJECT, EMAIL_ATTACHMENTS,
				jarr);

	}

	public static void sendRAWMail(String EMAIL_REPLY_TO, String bcc, String cc, String EMAIL_FROM, String htmlBody,
			String textBody, String EMAIL_SUBJECT, String EMAIL_ATTACHMENTS, JsonArray jarr)
			throws MessagingException, AddressException, IOException {
		AWSCredentials credentials = null;
		try {
			credentials = new BasicAWSCredentials("AKIAIDKUZPM5OMYLXFVA", "XITjUnboU3hThgyuR32sAJ3vnBibuVEXDEjB/Nda");

		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (~/.aws/credentials), and is in valid format.", e);
		}

		Session session = Session.getDefaultInstance(new Properties());
		MimeMessage message = new MimeMessage(session);
		message.setSubject(EMAIL_SUBJECT, "UTF-8");

		message.setFrom(new InternetAddress(EMAIL_FROM));
		// message.setReplyTo(new Address[] { new
		// InternetAddress(EMAIL_REPLY_TO) });

		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_REPLY_TO));

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

		// if (!htmlBody.isEmpty() && !htmlBody.equals("")) {
		// MimeBodyPart html = new MimeBodyPart();
		// cover.addBodyPart(html);
		// html.setContent(htmlBody, "text/html");
		// }
		// if (!textBody.isEmpty() && !textBody.equals("")) {
		// MimeBodyPart txt = new MimeBodyPart();
		// cover.addBodyPart(txt);
		// txt.setContent(textBody, "text/plain");
		// }

		wrap.setContent(cover);

		MimeMultipart content = new MimeMultipart("related");
		message.setContent(content);
		content.addBodyPart(wrap);

		attach(EMAIL_ATTACHMENTS, credentials, content, jarr);

		// html.setContent("<html><body><h1>HTML</h1>\n" + HtmlBody +
		// "</body></html>", "text/html");

		try {
			System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

			// AmazonSimpleEmailServiceClient client = new
			// AmazonSimpleEmailServiceClient(credentials)
			// .withRegion(Regions.US_WEST_2);
			AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient().withRegion(Regions.US_WEST_2);

			PrintStream out = System.out;
			message.writeTo(out);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			message.writeTo(outputStream);
			RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

			SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);
			client.sendRawEmail(rawEmailRequest);

			System.out.println("Email sent!");

		} catch (Exception ex) {
			System.out.println("Email Failed");
			System.err.println("Error message: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private static void attach(String EMAIL_ATTACHMENTS, AWSCredentials credentials, MimeMultipart content,
			JsonArray jsonArray) throws IOException, MessagingException {
		String[] attachmentsFiles = new String[] { EMAIL_ATTACHMENTS };

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject jObj = jsonArray.get(i).getAsJsonObject();

			String id = UUID.randomUUID().toString();
			sb.append("<img src=\"cid:");
			sb.append(id);
			sb.append("\" alt=\"ATTACHMENT\"/>\n");

			MimeBodyPart attachment = new MimeBodyPart();

			S3Object s3Obj = getFileFromS3(credentials, jObj.get("bucket_name").getAsString(),
					jObj.get("file_name").getAsString());

			DataSource fds = new ByteArrayDataSource(s3Obj.getObjectContent(),
					s3Obj.getObjectMetadata().getContentType());

			attachment.setDataHandler(new DataHandler(fds));
			attachment.setHeader("Content-ID", "<" + id + ">");
			attachment.setFileName(jObj.get("file_name").getAsString());

			content.addBodyPart(attachment);

		}

		//
		// for (String attachmentFileName : attachmentsFiles) {
		// String id = UUID.randomUUID().toString();
		// sb.append("<img src=\"cid:");
		// sb.append(id);
		// sb.append("\" alt=\"ATTACHMENT\"/>\n");
		//
		// MimeBodyPart attachment = new MimeBodyPart();
		//
		// S3Object s3Obj = getFileFromS3(credentials, "bucketName",
		// "fileName");
		//
		// DataSource fds = new ByteArrayDataSource(s3Obj.getObjectContent(),
		// s3Obj.getObjectMetadata().getContentType());
		//
		// attachment.setDataHandler(new DataHandler(fds));
		// attachment.setHeader("Content-ID", "<" + id + ">");
		// attachment.setFileName("sample.txt");
		//
		// content.addBodyPart(attachment);
		// }
	}

	private static S3Object getFileFromS3(AWSCredentials credentials, String bucketName, String fileName) {

		// AmazonS3 s3client =
		// AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_2)
		// .withCredentials(new
		// AWSStaticCredentialsProvider(credentials)).build();

		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_2).build();

		S3Object object = s3client.getObject(new GetObjectRequest(bucketName, fileName));

		// InputStream s3ois = object.getObjectContent();
		// object.getObjectMetadata().getContentType();
		// return s3ois;
		return object;
	}

}