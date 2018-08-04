package com.amazonaws.demo.sns.mail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

// These are from the JavaMail API, which you can download at https://java.net/projects/javamail/pages/Home. 
// Be sure to include the mail.jar library in your project. In the build order, mail.jar should precede the AWS SDK for Java library.
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;

public class AmazonSESComposeAndSendRAWMIMEEmail {

	// IMPORTANT: To successfully send an email, you must replace the values of
	// the strings below with your own values.
	private static String FROM = "piyush.mittal@ituple.com";

	private static String EMAIL_REPLY_TO = "ankur.huria@ituple.com";

	private static String EMAIL_RECIPIENT = "piyush.mittal@ituple.com";

	private static String EMAIL_ATTACHMENTS = "https:\\s3-us-west-2.amazonaws.com\\asdasddasd\\test.txt";

	// IMPORTANT: Ensure that the region selected below is the one in which your
	// identities are verified.
	private static Regions AWS_REGION = Regions.US_WEST_2;

	private static String SUBJECT = "Amazon SES email test";
	private static String EMAIL_BODY_TEXT = "This MIME email was sent through Amazon SES using SendRawEmail.";

	public static void main(String[] args) throws AddressException, MessagingException, IOException {

		List<String> TO = new ArrayList<String>();
		TO.add("ankur.huria@ituple.com");
		AWSCredentials credentials = new BasicAWSCredentials("AKIAJLJP7L62ZLAAMARA",
				"Tul8iMnfHla8k9N4N3ZSX1z+edsw3ojutF/a5oHO");
		AmazonSimpleEmailService service = new AmazonSimpleEmailServiceClient(credentials);

		sendRAWMail(credentials, "piyush.mittal@ituple.com", FROM, "HTMLBODY", EMAIL_BODY_TEXT, SUBJECT,EMAIL_ATTACHMENTS);
	}

	public static void sendRAWMail(AWSCredentials credentials, String TO, String FROM, String HTMLBODY,
			String TEXTBODY, String SUBJECT, String attachmentLocation) throws MessagingException, AddressException {
		Session session = Session.getDefaultInstance(new Properties());
		MimeMessage message = new MimeMessage(session);
		message.setSubject(SUBJECT, "UTF-8");

		message.setFrom(new InternetAddress(FROM));
		message.setReplyTo(new Address[] { new InternetAddress(EMAIL_REPLY_TO) });

		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_RECIPIENT));

		// Cover wrap
		MimeBodyPart wrap = new MimeBodyPart();

		// Alternative TEXT/HTML content
		MimeMultipart cover = new MimeMultipart("alternative");
		MimeBodyPart html = new MimeBodyPart();
		cover.addBodyPart(html);

		wrap.setContent(cover);

		MimeMultipart content = new MimeMultipart("related");
		message.setContent(content);
		content.addBodyPart(wrap);

		String[] attachmentsFiles = new String[] { EMAIL_ATTACHMENTS };

		// This is just for testing HTML embedding of different type of
		// attachments.
		StringBuilder sb = new StringBuilder();

//		for (String attachmentFileName : attachmentsFiles) {
//			String id = UUID.randomUUID().toString();
//			sb.append("<img src=\"cid:");
//			sb.append(id);
//			sb.append("\" alt=\"ATTACHMENT\"/>\n");
//
//			MimeBodyPart attachment = new MimeBodyPart();
//
//			
////			AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_2)
////					.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
////			S3Object object = s3client.getObject(new GetObjectRequest("bucket", "file"));
//			
//			DataSource fds = new FileDataSource(attachmentFileName);
//			
//			
//			attachment.setDataHandler(new DataHandler(fds));
//			attachment.setHeader("Content-ID", "<" + id + ">");
//			attachment.setFileName(fds.getName());
//
//			content.addBodyPart(attachment);
//		}

		html.setContent("<html><body><h1>HTML</h1>\n" + EMAIL_BODY_TEXT + "</body></html>", "text/html");

		try {
			System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

			// AWSCredentials credentials = null;
			// try {
			// credentials = credentials = new
			// BasicAWSCredentials("AKIAIDKUZPM5OMYLXFVA",
			// "XITjUnboU3hThgyuR32sAJ3vnBibuVEXDEjB/Nda");
			// } catch (Exception e) {
			// throw new AmazonClientException("Cannot load the credentials from
			// the credential profiles file. "
			// + "Please make sure that your credentials file is at the correct
			// "
			// + "location (~/.aws/credentials), and is in valid format.", e);
			// }

			// Instantiate an Amazon SES client, which will make the service
			// call with the supplied AWS credentials.
			AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);
			Region REGION = Region.getRegion(AWS_REGION);
			client.setRegion(REGION);

			
			AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_2)
					.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
			S3Object object = s3client.getObject(new GetObjectRequest("asdasddasd", "test.txt"));
		
			S3ObjectInputStream s3ois=  object.getObjectContent();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//			message.writeTo(outputStream);
//			
			byte[] buffer = new byte[8 * 1024];
			try {
				int bytesRead;
				while ((bytesRead = s3ois.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
			} finally {
				outputStream.close();
			}
			
			
			// Print the raw email content on the console
			PrintStream out = System.out;
			message.writeTo(out);

			// Send the email.
//			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//			message.writeTo(outputStream);
//			
			
			
			
			
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
}