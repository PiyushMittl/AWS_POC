/*
 * 
 */
package com.piyush.product.aws.core.sns.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.ListVerifiedEmailAddressesResult;
import com.amazonaws.services.simpleemail.model.VerifyEmailAddressRequest;
import com.piyush.product.aws.core.sns.builder.NotificationClient;
import com.piyush.product.aws.core.sns.exception.SNSException;
import com.piyush.product.aws.core.sns.factory.SNSFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class AmazonSESSenderVarification.
 */
public class AmazonSESSenderVarification {

	/** The credentials. */
	static AWSCredentials credentials = SNSFactory.getCredentials();

	// Replace with your "From" address. This address must be verified.

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws SNSException
	 */
	public static void main(String[] args) throws IOException, SNSException {
		final String FROM = "rishi.mishra@ituple.com";

		// Replace with a "To" address. If you have not yet requested production
		// access, this address must be verified.
		List<String> TO = new ArrayList();
		TO.add("piyush.mittal@ituple.com");
		AmazonSimpleEmailService service = new AmazonSimpleEmailServiceClient(credentials);

		// Verified
		if (verify(service, FROM)) {
		} else {
			// not Verified
			List<String> TOAtin = new ArrayList();
			TOAtin.add("piyush.piyush@ituple.com");
		}
	}

	public static boolean verifyEmailAddress(NotificationClient nc, String address) throws SNSException {
		AmazonSimpleEmailServiceClient client = null;
		if (nc.getRegions() == null) {
			throw new SNSException("region can not be null");

		} else if (nc.getAwsCredential() == null) {

			client = SNSFactory.getAmazonSimpleEmailServiceClient(nc.getRegions());

		} else {

			client = SNSFactory.getAmazonSimpleEmailServiceClient(nc.getAwsCredential(), nc.getRegions());
		}

		return verify(client, address);
	}

	/**
	 * Verify email address.
	 *
	 * @param ses
	 *            the ses
	 * @param address
	 *            the address
	 * @return true, if successful
	 * @throws SNSException
	 */
	public static boolean verify(AmazonSimpleEmailService ses, String address) throws SNSException {

		ListVerifiedEmailAddressesResult verifiedEmails = ses.listVerifiedEmailAddresses();
		if (verifiedEmails.getVerifiedEmailAddresses().contains(address)) {
			return true;
		} else {
			ses.verifyEmailAddress(new VerifyEmailAddressRequest().withEmailAddress(address));
			System.out.println("Please check the email address " + address + " to verify it");
			return false;
		}

	}

	/**
	 * Email verification link sender.
	 *
	 * @param ses
	 *            the ses
	 * @param address
	 *            the address
	 * @return the string
	 */
	public static String emailVerificationLinkSender(AmazonSimpleEmailService ses, String address) {
		ListVerifiedEmailAddressesResult verifiedEmails = ses.listVerifiedEmailAddresses();
		if (verifiedEmails.getVerifiedEmailAddresses().contains(address))
			return "ALREADY_VARIFIED";

		ses.verifyEmailAddress(new VerifyEmailAddressRequest().withEmailAddress(address));
		return "LINK_SEND";
	}

}