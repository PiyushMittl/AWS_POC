package com.amazonaws.demo.sns.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.ListVerifiedEmailAddressesResult;
import com.amazonaws.services.simpleemail.model.Message;
//import com.amazonaws.services.simpleemail.model.SendBounceRequest;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.amazonaws.services.simpleemail.model.VerifyEmailAddressRequest;

public class AmazonSESSenderVarification {

	static AWSCredentials credentials = new BasicAWSCredentials("AKIAJLJP7L62ZLAAMARA",
			"Tul8iMnfHla8k9N4N3ZSX1z+edsw3ojutF/a5oHO");

	// Replace with your "From" address. This address must be verified.

	public static void main(String[] args) throws IOException {
		final String FROM = "rishi.mishra@ituple.com";

		// Replace with a "To" address. If you have not yet requested production
		// access, this address must be verified.
		List<String> TO = new ArrayList();
		TO.add("ankur.huria@ituple.com");
		// List<String> BCC = new ArrayList();
		// BCC.add("rishi.mishra@ituple.com");

		List<String> BCC = null;

		final String BODY = "this is a test email";
		final String SUBJECT = "region updated test mail";

		AmazonSimpleEmailService service = new AmazonSimpleEmailServiceClient(credentials);

		// Verified
		if (verifyEmailAddress(service, FROM)) {
			//send(FROM, TO, BCC, BODY, SUBJECT);
		} else {
			// not Verified
			List<String> TOAtin = new ArrayList();
			TOAtin.add("atin.agarwal@ituple.com");
//			send("atin.agarwal@ituple.com", TOAtin, null, FROM + "email address not varified",
//					"sending verification mail to " + FROM + " for verification");
		}
	}

//	private static void send(String FROM, List<String> TO, List<String> BCC, String BODY, String SUBJECT) {
//		AmazonSimpleEmailService service = new AmazonSimpleEmailServiceClient(credentials);
//
//		// Construct an object to contain the recipient address.
//		// add to bcc
//		Destination destination = new Destination().withToAddresses(TO).withBccAddresses(BCC);
//
//		// Create the subject and body of the message.
//		Content subject = new Content().withData(SUBJECT);
//		Content textBody = new Content().withData(BODY);
//		Body body = new Body().withText(textBody);
//
//		// Create a message with the specified subject and body.
//		Message message = new Message().withSubject(subject).withBody(body);
//
//		// Assemble the email.
//		SendEmailRequest request = new SendEmailRequest().withSource(FROM).withDestination(destination)
//				.withMessage(message);
//
//		try {
//			System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");
//
//			// Instantiate an Amazon SES client, which will make the service
//			// call with the supplied AWS credentials.
//			AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);
//
//			Region REGION = Region.getRegion(Regions.US_EAST_1);
//			client.setRegion(REGION);
//
//			// Send the email.
//			SendEmailResult sendEmailResult = client.sendEmail(request);
//			sendEmailResult.getMessageId();
//
//			// client.sendBounce(new
//			// SendBounceRequest().withBounceSender(FROM));
//
//			System.out.println("Email sent!");
//
//		} catch (Exception ex) {
//			System.out.println("The email was not sent.");
//			System.out.println("Error message: " + ex.getMessage());
//		}
//	}

	public static boolean verifyEmailAddress(AmazonSimpleEmailService ses, String address) {
		ListVerifiedEmailAddressesResult verifiedEmails = ses.listVerifiedEmailAddresses();
		if (verifiedEmails.getVerifiedEmailAddresses().contains(address))
			return true;

		ses.verifyEmailAddress(new VerifyEmailAddressRequest().withEmailAddress(address));
		System.out.println("Please check the email address " + address + " to verify it");
		return false;
	}

	public static String emailVerificationLinkSender(AmazonSimpleEmailService ses, String address) {
		ListVerifiedEmailAddressesResult verifiedEmails = ses.listVerifiedEmailAddresses();
		if (verifiedEmails.getVerifiedEmailAddresses().contains(address))
			return "ALREADY_VARIFIED";

		ses.verifyEmailAddress(new VerifyEmailAddressRequest().withEmailAddress(address));
		return "LINK_SEND";
	}

}