/*
 * 
 */
package com.piyush.product.aws.core.sns;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.amazonaws.services.sns.model.PublishResult;
import com.piyush.product.aws.core.sns.builder.Email;
import com.piyush.product.aws.core.sns.builder.Message;
import com.piyush.product.aws.core.sns.exception.SNSException;
import com.piyush.product.aws.core.sns.mail.AmazonSESComposeAndRAWMIMEEmailSender;
import com.piyush.product.aws.core.sns.mail.AmazonSESEmailSender;
import com.piyush.product.aws.core.sns.message.AmazonSESMessageSender;

// TODO: Auto-generated Javadoc
/**
 * The Class SNSOperations.
 */
public class SNSOperations {

	/**
	 * Validate email.
	 */
	public static void validateEmail() {

	}

	/**
	 * Send email.
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
	@SuppressWarnings("static-access")
	public static Object sendEmail(Object o) throws SNSException, AddressException, MessagingException, IOException {

		if (o != null && o instanceof Email) {
			if (((Email) o).getHasAttachment().equals("true")) {

				return new AmazonSESComposeAndRAWMIMEEmailSender().send((Email) o);
			} else {
				return new AmazonSESEmailSender().send((Email) o);
			}

		} else {
			throw new SNSException("Object is not an instance of Email or Object is null");
		}

	}

	/**
	 * Send message.
	 *
	 * @param o
	 *            the o
	 * @throws SNSException
	 *             the SNS exception
	 */
	public static List<PublishResult> sendMessage(Object o) throws SNSException {

		if (o != null && o instanceof Message) {
			return new AmazonSESMessageSender().send((Message) o);
		} else {
			throw new SNSException("Object is not an instance of Message or Object is null");
		}

	}

}
