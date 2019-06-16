/*
 * 
 */
package com.piyush.product.aws.core.sns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.piyush.product.aws.core.sns.builder.Email;
import com.piyush.product.aws.core.sns.builder.Message;
import com.piyush.product.aws.core.sns.builder.NotificationClient;
import com.piyush.product.aws.core.sns.factory.SNSFactory;
import com.piyush.product.aws.core.sns.mail.AmazonSESSenderVarification;

/**
 * Hello world!
 *
 */
public class App {
	@SuppressWarnings("unused")
	public static void main(String[] args) {

		List<String> mobileNumber = new ArrayList<String>();
		mobileNumber.add("+919358909659");

		List<String> to = new ArrayList<String>();
		to.add("piyush.mittal@ituple.com");

		String from = "piyush.mittal@ituple.com";

		List<String> cc = new ArrayList();
		cc.add("piyush.mittal@ituple.com");
		cc.add("piyush.mittal@ituple.com");

		List<String> bcc = new ArrayList();
		bcc.add("piyush.mittal@ituple.com");
		bcc.add("piyush.mittal@ituple.com");

		try {

			// create client
			NotificationClient nc = new NotificationClient.NotificationClientBuilder()
					.withCredential(SNSFactory.getCredentials()).withRegion(Regions.US_WEST_2).build();
			if (true) {
				// verify from
				if (AmazonSESSenderVarification.verifyEmailAddress(nc, from)) {
					// send email sample without attachment
					{
						Email e = new Email.EmailBuilder().notificationClient(nc).from(from).to(to).bcc(bcc)
								.hasAttachment("false").build();
						SNSOperations.sendEmail(e);
					}
					// send email sample with attachment
					{
						List<Map<String, String>> attachments = new ArrayList<Map<String, String>>();
						Map<String, String> attachment = new HashMap<String, String>();
						attachment.put("file_name", "test.txt");
						attachment.put("bucket_name", "pm311");
						attachments.add(attachment);

						Email e = new Email.EmailBuilder().notificationClient(nc).from(from).to(to)
								.attachments(attachments).hasAttachment("true").build();
						SNSOperations.sendEmail(e);
					}

				} else {
					// email not verified handling
					System.out.println("Email id not varified and verification code send");

				}
			}
			// send sms sample
			{
				Message m = new Message.MessageBuilder().notificationClient(nc).mobileNumber(mobileNumber)
						.message("test message").build();
				SNSOperations.sendMessage(m);

				System.out.println("Message Sent!");
			}

		} catch (

		Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
