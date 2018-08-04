package com.amazonaws.demo.sns.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.GetSMSAttributesRequest;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SetSMSAttributesRequest;

public class MessageSNS {

	static AWSCredentials credentials = new BasicAWSCredentials("AKIAIDKUZPM5OMYLXFVA",
			"XITjUnboU3hThgyuR32sAJ3vnBibuVEXDEjB/Nda");

	public static void main(String args[]) throws Exception {

		// create a new SNS client and set endpoint
		AmazonSNSClient snsClient = new AmazonSNSClient(credentials);
		snsClient.setRegion(Region.getRegion(Regions.US_WEST_2));
		setDefaultSmsAttributes(snsClient);

		String message = "test message";
		String phoneNumber = "+919630272693";
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
		// <set SMS attributes>

		List<String> nos=new ArrayList();
		if(phoneNumber.contains(","))
		{
			 nos = new ArrayList<String>(Arrays.asList(phoneNumber.split(",")));
		}
		else{
			nos.add(phoneNumber);
		}
		
		for(String eachNos:nos){
			MessageSNS.sendSMSMessage(snsClient, message, eachNos, new HashMap<String, MessageAttributeValue>());
		}
		
//		sendSMSMessage(snsClient, message, phoneNumber, smsAttributes);

	}

	public static void sendSMSMessage(AmazonSNSClient snsClient, String message, String phoneNumber,
			Map<String, MessageAttributeValue> smsAttributes) {
//		AmazonSNSClient snsClient = new AmazonSNSClient();
//		setDefaultSmsAttributes(snsClient);
		PublishResult result = snsClient.publish(new PublishRequest().withMessage(message).withPhoneNumber(phoneNumber)
				.withMessageAttributes(smsAttributes));
		// System.out.println(result); // Prints the message ID.
	}

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