/*
 * 
 */
package com.piyush.product.aws.core.sns.lambda;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent.KinesisEventRecord;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.GetSMSAttributesRequest;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.SetSMSAttributesRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.piyush.product.aws.core.sns.entity.NotificationEntity;
import com.piyush.product.aws.core.sns.factory.SNSFactory;
import com.piyush.product.aws.core.sns.mail.AmazonSESEmailSender;
import com.piyush.product.aws.core.sns.mail.AmazonSESSenderVarification;
import com.piyush.product.aws.core.sns.message.AmazonSESMessageSender;

/**
 * 
 * @author PiyushMittal
 *
 */
public class LambdaSNS implements RequestHandler<KinesisEvent, String> {

	AWSCredentials credentials = SNSFactory.getCredentials();

	String to;
	String from;
	String textBody;
	String cc;
	String bcc;
	String sub;
	String snsType;
	String htmlBody;
	String hasAttachement;
	String attachmentLocation;
	String phoneNumber;

	public static void main(String[] args) throws IOException {
		JsonObject jsonObject = new JsonObject();
		// jsonObject.addProperty("name", "piyush");
	}

	/**
	 * 
	 * @param snsClient
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

	/**
	 * 
	 * @param jsonObject
	 * @throws IOException
	 */
	public void send(JsonObject jsonObject) throws IOException {

		System.out.println("--------------------------> inside handleRequest starts");
		try {
			System.out.println("--------------------------> jsonObject = " + jsonObject);

			to = jsonObject.get("to").getAsString();
			System.out.println("to---" + to);

			from = jsonObject.get("from").getAsString();
			System.out.println("from---" + from);

			textBody = jsonObject.get("text_body").getAsString();
			System.out.println("textBody---" + textBody);

			cc = jsonObject.get("cc").getAsString();
			System.out.println("cc---" + cc);

			bcc = jsonObject.get("bcc").getAsString();
			System.out.println("bcc---" + bcc);

			sub = jsonObject.get("sub").getAsString();
			System.out.println("sub---" + sub);

			snsType = jsonObject.get("sns_type").getAsString();
			System.out.println("sns_type---" + snsType);

			htmlBody = jsonObject.get("html_body").getAsString();
			System.out.println("htmlBody---" + htmlBody);

			hasAttachement = jsonObject.get("has_attachment").getAsString();
			System.out.println("hasAttachement---" + hasAttachement);

			attachmentLocation = jsonObject.get("attachment_location").getAsString();
			System.out.println("attachmentLocation---" + attachmentLocation);

			phoneNumber = jsonObject.get("phone_number").getAsString();
			System.out.println("phoneNumber---" + phoneNumber);

			System.out.println("before varification");

			AmazonSimpleEmailService service = new AmazonSimpleEmailServiceClient();
			service.setRegion(Region.getRegion(Regions.US_WEST_2));
			System.out.println(service);

			// Verification

			if (snsType.equals("email")) {
				// send email
				System.out.println("before varification");
				if (AmazonSESSenderVarification.verify(service, from)) {
					System.out.println("varification done");
					if (hasAttachement.equals("true")) {

						// AmazonSESComposeAndSendRAWMIMEEmail.sendRAWMail(SNSFactory.getAmazonSimpleEmailServiceClient(Regions.US_WEST_2),
						// to, from, htmlBody, textBody, sub,
						// attachmentLocation);

					} else {
						System.out.println("before sending mail");
						List<String> TO = new ArrayList();
						List<String> BCC = new ArrayList();
						List<String> CC = new ArrayList();

						if (to.contains(",")) {
							TO = new ArrayList<String>(Arrays.asList(to.split(",")));
						} else {
							TO.add(to);
						}
						if (bcc.contains(",")) {
							BCC = new ArrayList<String>(Arrays.asList(bcc.split(",")));
						} else {
							BCC.add(to);
						}
						if (cc.contains(",")) {
							CC = new ArrayList<String>(Arrays.asList(cc.split(",")));
						} else {
							CC.add(cc);
						}
						AmazonSESEmailSender.sendTextMail(
								SNSFactory.getAmazonSimpleEmailServiceClient(Regions.US_WEST_2), TO, from, CC, BCC,
								htmlBody, textBody, sub);
					}
				}
			} else if (snsType.equals("message")) {
				// if send mobile message
				AmazonSNSClient snsClient = new AmazonSNSClient(credentials);
				snsClient.setRegion(Region.getRegion(Regions.US_WEST_2));
				setDefaultSmsAttributes(snsClient);
				List<String> nos = new ArrayList();
				if (phoneNumber.contains(",")) {
					nos = new ArrayList<String>(Arrays.asList(phoneNumber.split(",")));
				} else {
					nos.add(phoneNumber);
				}

				for (String eachNos : nos) {
					AmazonSESMessageSender.sendSMSMessage(snsClient, textBody, eachNos,
							new HashMap<String, MessageAttributeValue>());
				}
			}
			// if send email

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String handleRequest(KinesisEvent event, Context context) {

		System.out.println("--------------------------> inside handleRequest starts");
		try {

			if (event.getRecords() != null && event.getRecords().size() > 0) {

				System.out.println("record ----" + event.getRecords());

				for (KinesisEventRecord record : event.getRecords()) {
					// String payload = new
					// String(record.getKinesis().getData().array());
					String payload = new String(record.getKinesis().getData().array());
					System.out.println(payload);
					context.getLogger().log("payload of stream: " + payload);
					JSONArray arrobj = new JSONArray(payload);
					System.out.println("arrobj-" + arrobj.get(0).toString());
					// arrobj.getJSONObject(1);

					for (int i = 0; i < arrobj.length(); i++) {
						JSONObject jsonObj = arrobj.getJSONObject(i);
						JsonParser jsonParser = new JsonParser();
						JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObj.toString());
						send(gsonObject);
						NotificationEntity notificationEntity = new ObjectMapper().readValue(jsonObj.toString(),
								NotificationEntity.class);

					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return "";

	}

}
