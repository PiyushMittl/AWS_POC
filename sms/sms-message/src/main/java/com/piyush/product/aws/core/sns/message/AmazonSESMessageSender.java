/*
 * 
 */
package com.piyush.product.aws.core.sns.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.GetSMSAttributesRequest;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SetSMSAttributesRequest;
import com.piyush.product.aws.core.sns.builder.Message;
import com.piyush.product.aws.core.sns.builder.NotificationClient;
import com.piyush.product.aws.core.sns.exception.SNSException;
import com.piyush.product.aws.core.sns.factory.SNSFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class AmazonSESMessageSender.
 *
 * @author PiyushMittal
 */
public class AmazonSESMessageSender {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
	public static void main(String args[]) throws Exception {

		// create a new SNS client and set endpoint
		AmazonSNSClient snsClient = new AmazonSNSClient(SNSFactory.getCredentials());
		snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));

		setDefaultSmsAttributes(snsClient);

		String message = "test message";
		String phoneNumber = "+919358909659,+919358909659";
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
		// <set SMS attributes>

		List<String> nos = null;
		if (phoneNumber.contains(",")) {
			nos = new ArrayList<String>(Arrays.asList(phoneNumber.split(",")));
		} else {
			nos.add(phoneNumber);
		}

		for (String eachNos : nos) {
			AmazonSESMessageSender.sendSMSMessage(snsClient, message, eachNos,
					new HashMap<String, MessageAttributeValue>());
		}

	}

	/**
	 * Send.
	 *
	 * @param o
	 *            the o
	 * @throws SNSException
	 *             the SNS exception
	 */
	public static List<PublishResult> send(Message o) throws SNSException {

		AmazonSNSClient client = null;
		List<PublishResult> publishResult = new ArrayList();
		NotificationClient nc = o.getNotificationClient();
		if (nc.getRegions() == null) {
			throw new SNSException("region can not be null");

		} else if (nc.getAwsCredential() == null) {

			client = SNSFactory.getAmazonSNSClient(nc.getRegions());

		} else {
			client = SNSFactory.getAmazonSNSClient(nc.getAwsCredential(), nc.getRegions());
		}
		List<String> nos = o.getMobileNumber();
		for (String eachNos : nos) {
			publishResult.add(sendSMSMessage(client, o.getMessage(), eachNos, o.getSmsAttribute()));
		}
		return publishResult;
	}

	/**
	 * Send SMS message.
	 *
	 * @param snsClient
	 *            the sns client
	 * @param message
	 *            the message
	 * @param phoneNumber
	 *            the phone number
	 * @param smsAttributes
	 *            the sms attributes
	 * @return the publish result
	 */
	public static PublishResult sendSMSMessage(AmazonSNSClient snsClient, String message, String phoneNumber,
			Map<String, MessageAttributeValue> smsAttributes) {
		PublishResult result = snsClient.publish(new PublishRequest().withMessage(message).withPhoneNumber(phoneNumber)
				.withMessageAttributes(smsAttributes));
		return result;
	}

	/**
	 * Sets the default sms attributes.
	 *
	 * @param snsClient
	 *            the new default sms attributes
	 */
	public static void setDefaultSmsAttributes(AmazonSNSClient snsClient) {
		SetSMSAttributesRequest setRequest = new SetSMSAttributesRequest()
				.addAttributesEntry("DefaultSenderID", "mySenderID").addAttributesEntry("MonthlySpendLimit", "1")
				.addAttributesEntry("DeliveryStatusIAMRole", "arn:aws:iam::123456789012:role/mySnsRole")
				.addAttributesEntry("DeliveryStatusSuccessSamplingRate", "10")
				.addAttributesEntry("DefaultSMSType", "Transactional")
				.addAttributesEntry("UsageReportS3Bucket", "sns-sms-daily-usage");
		snsClient.setSMSAttributes(setRequest);

		Map<String, String> myAttributes = snsClient.getSMSAttributes(new GetSMSAttributesRequest()).getAttributes();
		System.out.println("My SMS attributes:");
		for (String key : myAttributes.keySet()) {
			System.out.println(key + " = " + myAttributes.get(key));
		}
	}

}