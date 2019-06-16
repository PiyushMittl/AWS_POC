/*
 * 
 */
package com.piyush.product.aws.core.sns.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.piyush.product.aws.core.sns.builder.Email;
import com.piyush.product.aws.core.sns.builder.NotificationClient;
import com.piyush.product.aws.core.sns.exception.SNSException;
import com.piyush.product.aws.core.sns.factory.SNSFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class AmazonSESEmailSender.
 */
public class AmazonSESEmailSender {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		// Replace sender@example.com with your "From" address.
		// This address must be verified with Amazon SES.
		String from = "piyush.mittal@ituple.com";
		// The email body for recipients with non-HTML email clients.
		final String textBody = "This email was sent through Amazon SES " + "using the AWS SDK for Java.";
		// The subject line for the email.
		final String subject = "Amazon SES test (AWS SDK for Java)";

		// The HTML body for the email.
		final String HTMLBODY = "<h1>html body</a>";

		AWSCredentials credentials = null;
		try {
			credentials = credentials = SNSFactory.getCredentials();
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

			AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);
			client.withRegion(Regions.US_WEST_2);

			sendTextMail(client, TO, from, cc, bcc, HTMLBODY, textBody, subject);
		} catch (Exception ex) {
			System.out.println("The email was not sent. Error message: " + ex.getMessage());
		}
	}

	/**
	 * Send.
	 *
	 * @param o
	 *            the o
	 * @return
	 * @throws SNSException
	 *             the SNS exception
	 */
	public static SendEmailResult send(Email o) throws SNSException {

		AmazonSimpleEmailServiceClient client = null;
		NotificationClient nc = o.getNotificationClient();
		if (nc.getRegions() == null) {
			throw new SNSException("region can not be null");

		} else if (nc.getAwsCredential() == null) {

			client = SNSFactory.getAmazonSimpleEmailServiceClient(nc.getRegions());

		} else {

			client = SNSFactory.getAmazonSimpleEmailServiceClient(nc.getAwsCredential(), nc.getRegions());
		}
		return sendTextMail(client, o.getTo(), o.getFrom(), o.getCc(), o.getBcc(), o.getHtmlBody(), o.getTextBody(),
				o.getSub());

	}

	/**
	 * *
	 * <p>
	 * If you are sending an email message to a large number of recipients, then
	 * it makes sense to send it in both HTML and text. Some recipients will
	 * have HTML-enabled email clients, so that they can click embedded
	 * hyperlinks in the message. Recipients using text-based email clients will
	 * need you to include URLs that they can copy and open using a web browser.
	 * </p>
	 * http://docs.aws.amazon.com/ses/latest/DeveloperGuide/email-format.html
	 * 
	 * 
	 * Send text mail.
	 *
	 * @param client
	 *            the client
	 * @param to
	 *            the to
	 * @param FROM
	 *            the from
	 * @param cc
	 *            the cc
	 * @param bcc
	 *            the bcc
	 * @param htmlBody
	 *            the html body
	 * @param textBody
	 *            the text body
	 * @param subject
	 *            the subject
	 * @return
	 */
	public static SendEmailResult sendTextMail(AmazonSimpleEmailServiceClient client, List<String> to, String FROM,
			List<String> cc, List<String> bcc, String htmlBody, String textBody, String subject) {
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
						new Message().withBody(body).withSubject(new Content().withCharset("UTF-8").withData(subject)))
				.withSource(FROM);

		return client.sendEmail(request);
	}
}