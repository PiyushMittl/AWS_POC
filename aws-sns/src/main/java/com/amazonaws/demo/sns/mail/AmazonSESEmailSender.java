package com.amazonaws.demo.sns.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

public class AmazonSESEmailSender {

	public static void main(String[] args) throws IOException {
		// Replace sender@example.com with your "From" address.
		// This address must be verified with Amazon SES.
		String FROM = "piyush.mittal@ituple.com";
		// The email body for recipients with non-HTML email clients.
		final String TEXTBODY = "This email was sent through Amazon SES " + "using the AWS SDK for Java.";
		// The subject line for the email.
		final String SUBJECT = "Amazon SES test (AWS SDK for Java)";

		// The HTML body for the email.
		final String HTMLBODY = "<h1>Amazon SES test (AWS SDK for		 Java)</h1>"
				+ "<p>This email was sent with <a		 href='https://aws.amazon.com/ses/'>"
				+ "Amazon SES</a> using the <a		 href='https://aws.amazon.com/sdk-for-java/'>"
				+ "AWS SDK for		 Java</a>";

		AWSCredentials credentials = null;
		try {
			credentials = credentials = new BasicAWSCredentials("AKIAIDKUZPM5OMYLXFVA",
					"XITjUnboU3hThgyuR32sAJ3vnBibuVEXDEjB/Nda");
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (~/.aws/credentials), and is in valid format.", e);
		}

		try {
			List<String> TO = new ArrayList<String>();
			TO.add("piyush.mittal@ituple.com");
			List<String> cc = new ArrayList<String>();
			cc.add("mr.piyushmittal@rediffmail.com");
			List<String> bcc = new ArrayList<String>();
			bcc.add("piyush.mittal@ituple.com");

			sendTextMail(credentials, TO, FROM, cc, bcc, HTMLBODY, TEXTBODY, SUBJECT);
		} catch (Exception ex) {
			System.out.println("The email was not sent. Error message: " + ex.getMessage());
		}
	}

	/**
	 * 
	 * <p>
	 * If you are sending an email message to a large number of recipients, then
	 * it makes sense to send it in both HTML and text. Some recipients will
	 * have HTML-enabled email clients, so that they can click embedded
	 * hyperlinks in the message. Recipients using text-based email clients will
	 * need you to include URLs that they can copy and open using a web browser.
	 * </p>
	 * http://docs.aws.amazon.com/ses/latest/DeveloperGuide/email-format.html
	 * 
	 * @param credentials
	 * @param to
	 * @param FROM
	 * @param cc
	 * @param bcc
	 * @param HTMLBODY
	 * @param TEXTBODY
	 * @param SUBJECT
	 */
	public static void sendTextMail(AWSCredentials credentials, List<String> to, String FROM, List<String> cc,
			List<String> bcc, String htmlBody, String textBody, String SUBJECT) {
		// AmazonSimpleEmailServiceClient client = new
		// AmazonSimpleEmailServiceClient(credentials);
		AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient();
		client.withRegion(Regions.US_WEST_2);

		Destination destiation = new Destination().withToAddresses(to);
		if (cc != null && !cc.isEmpty()) {
			destiation.withCcAddresses(cc);
		}
		if (bcc != null && !bcc.isEmpty()) {
			destiation.withBccAddresses(bcc);
		}

		Body body = new Body();

		if (textBody == null) {
			textBody = "";
		}
		if (htmlBody == null) {
			htmlBody = "";
		}

		if ((textBody.isEmpty() && htmlBody.isEmpty()) || (!textBody.isEmpty() && !htmlBody.isEmpty())) {
			body.withText(new Content().withCharset("UTF-8").withData(textBody));
			body.withHtml(new Content().withCharset("UTF-8").withData(htmlBody));
		} else if (textBody.isEmpty() && !htmlBody.isEmpty()) {
			body.withHtml(new Content().withCharset("UTF-8").withData(htmlBody));

		} else if (!textBody.isEmpty() && htmlBody.isEmpty()) {
			body.withText(new Content().withCharset("UTF-8").withData(textBody));
		}

		SendEmailRequest request = new SendEmailRequest().withDestination(destiation)
				.withMessage(
						new Message().withBody(body).withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);

		client.sendEmail(request);
		System.out.println("Email sent!");
	}
}