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

	// Replace sender@example.com with your "From" address.
	// This address must be verified with Amazon SES.
	static String FROM = "piyush.mittal@ituple.com";

	// Replace recipient@example.com with a "To" address. If your account
	// is still in the sandbox, this address must be verified.
	static final String TO = "aman.saini@ituple.com";

	// The configuration set to use for this email. If you do not want to use a
	// configuration set, comment the next line and line 60.
	// static final String CONFIGSET = "ConfigSet";

	// The subject line for the email.
	static final String SUBJECT = "Amazon SES test (AWS SDK for Java)";

	// The HTML body for the email.
	static final String HTMLBODY = "<h1>Amazon SES test (AWS SDK for Java)</h1>"
			+ "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
			+ "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>" + "AWS SDK for Java</a>";

	// The email body for recipients with non-HTML email clients.
	static final String TEXTBODY = "This email was sent through Amazon SES " + "using the AWS SDK for Java.";

	// The subject line for the email.
	// static final String SUBJECT = "Amazon SES test (AWS SDK for Java)";

	public static void main(String[] args) throws IOException {

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
			TO.add("ankur.huria@ituple.com");
//			sendTextMail(credentials, TO, FROM,"rishi.mishra@ituple.com","rishi.mishra@ituple.com", HTMLBODY, TEXTBODY, SUBJECT);
		} catch (Exception ex) {
			System.out.println("The email was not sent. Error message: " + ex.getMessage());
		}
	}

	public static void sendTextMail(AWSCredentials credentials, List<String> to, String FROM,List<String> cc,List<String> bcc, String HTMLBODY,String TEXTBODY, String SUBJECT) {
		//AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);
		AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient();
		client.withRegion(Regions.US_WEST_2);
		
		Destination destiation=new Destination().withToAddresses(to);
		if(cc!=null && !cc.isEmpty())
		{
			destiation.withCcAddresses(cc);
		}
		if(bcc!=null && !bcc.isEmpty())
		{
			destiation.withBccAddresses(cc);
		}
//		new Destination().withToAddresses(to).withCcAddresses(cc).withBccAddresses(bcc)
		
		SendEmailRequest request = new SendEmailRequest().withDestination(destiation)
				.withMessage(new Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY))
								.withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);
		client.sendEmail(request);
		System.out.println("Email sent!");
	}
}