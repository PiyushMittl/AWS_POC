package com.amazonaws.demo.sns.lambda;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.demo.sns.entity.NotificationEntity;
import com.amazonaws.demo.sns.mail.AmazonSESComposeAndSendRAWMIMEEmail3;
import com.amazonaws.demo.sns.mail.AmazonSESEmailSender;
import com.amazonaws.demo.sns.mail.AmazonSESSenderVarification;
import com.amazonaws.demo.sns.message.MessageSNS;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LambdaSNS implements RequestHandler<KinesisEvent, String> {

	AWSCredentials credentials = new BasicAWSCredentials("AKIAIDKUZPM5OMYLXFVA",
			"XITjUnboU3hThgyuR32sAJ3vnBibuVEXDEjB/Nda");

	public static void main(String[] args) throws IOException {
		JsonObject jsonObject = new JsonObject();
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

	public void send(JsonObject jsonObject) throws IOException {
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
				if (AmazonSESSenderVarification.verifyEmailAddress(service, from)) {
					System.out.println("varification done");
					if (hasAttachement.equals("true")) {
						// AWSCredentials credentials, TO, FROM, HTMLBODY,
						// TEXTBODY, SUBJECT, attachmentLocation

						// AmazonSESComposeAndSendRAWMIMEEmail.sendRAWMail(credentials,
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
						AmazonSESEmailSender.sendTextMail(credentials, TO, from, CC, BCC, htmlBody, textBody, sub);
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
					MessageSNS.sendSMSMessage(snsClient, textBody, eachNos,
							new HashMap<String, MessageAttributeValue>());
				}
			}
			// if send email

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sendEntity(NotificationEntity notificationEntity) throws IOException {

		System.out.println("--------------------------> inside handleRequest starts");
		try {

			System.out.println("before varification");

			AmazonSimpleEmailService service = new AmazonSimpleEmailServiceClient();
			service.setRegion(Region.getRegion(Regions.US_WEST_2));
			System.out.println(service);

			List<String> TO = new ArrayList();
			List<String> BCC = new ArrayList();
			List<String> CC = new ArrayList();
			String from = "";
			String textBody = "";
			String sub = "";
			String htmlBody = "";
			String hasAttachement;
			String attachmentLocation = null;

			if (notificationEntity.getTo() != null && !notificationEntity.getTo().isEmpty()) {
				if (notificationEntity.getTo().contains(",")) {
					TO = new ArrayList<String>(Arrays.asList(notificationEntity.getTo().split(",")));
				} else {
					TO.add(notificationEntity.getTo());
				}
			} else {
				throw new Exception();
			}
			if (notificationEntity.getBcc() != null && !notificationEntity.getBcc().isEmpty()) {
				if (notificationEntity.getBcc().contains(",")) {
					BCC = new ArrayList<String>(Arrays.asList(notificationEntity.getBcc().split(",")));
				} else {
					BCC.add(notificationEntity.getBcc());
				}
			}
			if (notificationEntity.getCc() != null && !notificationEntity.getCc().isEmpty()) {
				if (notificationEntity.getCc().contains(",")) {
					CC = new ArrayList<String>(Arrays.asList(notificationEntity.getCc().split(",")));
				} else {
					CC.add(notificationEntity.getCc());
				}
			}
			if (notificationEntity.getFrom() != null && !notificationEntity.getFrom().isEmpty()) {
				from = notificationEntity.getFrom();
			}

			// message and mail text body
			if (notificationEntity.getText_body() != null && !notificationEntity.getText_body().isEmpty()) {
				textBody = notificationEntity.getText_body();
			}

			// Verification

			if (notificationEntity.getSns_type().equals("email")) {
				// send email
				System.out.println("before varification");
				if (AmazonSESSenderVarification.verifyEmailAddress(service, from)) {
					System.out.println("varification done");
					if (notificationEntity.getHas_attachment().equals("true")) {
						//////////////////// attachment mail
						JsonArray jarr = new JsonArray();
						if (notificationEntity.getAttachment() != null) {

							String json = new ObjectMapper().writeValueAsString(notificationEntity.getAttachment());
							System.out.println(
									"ttachement==================================================================>"
											+ json);
							jarr = JSONArrayToJsonArray(new JSONArray(json));

						}
						if (notificationEntity.getHtml_body() != null && !notificationEntity.getHtml_body().isEmpty()) {
							htmlBody = notificationEntity.getHtml_body();
						} else {

						}
						AmazonSESComposeAndSendRAWMIMEEmail3.sendRAWMail(notificationEntity.getTo(),
								notificationEntity.getBcc(), notificationEntity.getCc(), from, htmlBody, textBody, sub,
								"email attachment", jarr);

					} else {
						//////////////////// text mail
						System.out.println("before sending mail");

						if (notificationEntity.getHtml_body() != null && !notificationEntity.getHtml_body().isEmpty()) {
							htmlBody = notificationEntity.getHtml_body();
						} else {

						}
						if (notificationEntity.getSub() != null && !notificationEntity.getSub().isEmpty()) {
							sub = notificationEntity.getSub();
						} else {
							// throw new Exception();
						}
						AmazonSESEmailSender.sendTextMail(credentials, TO, from, CC, BCC, htmlBody, textBody, sub);
					}
				}
			} else if (notificationEntity.getSns_type().equals("message")) {//
				// if send mobile message
				AmazonSNSClient snsClient = new AmazonSNSClient(credentials);
				snsClient.setRegion(Region.getRegion(Regions.US_WEST_2));
				setDefaultSmsAttributes(snsClient);
				List<String> nos = new ArrayList();
				if (notificationEntity.getPhone_number().contains(",")) {
					nos = new ArrayList<String>(Arrays.asList(notificationEntity.getPhone_number().split(",")));
				} else {
					nos.add(notificationEntity.getPhone_number());
				}

				for (String eachNos : nos) {
					MessageSNS.sendSMSMessage(snsClient, textBody, eachNos,
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
						// send(gsonObject);
						NotificationEntity notificationEntity = new ObjectMapper().readValue(jsonObj.toString(),
								NotificationEntity.class);
						sendEntity(notificationEntity);

					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return "";

	}

	public JsonArray JSONArrayToJsonArray(JSONArray arrobj) throws Exception {

		JsonArray jar = new JsonArray();

		for (int i = 0; i < arrobj.length(); i++) {
			JSONObject jsonObj = arrobj.getJSONObject(i);
			JsonParser jsonParser = new JsonParser();
			JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObj.toString());
			jar.add(gsonObject);
		}
		return jar;
	}

}
