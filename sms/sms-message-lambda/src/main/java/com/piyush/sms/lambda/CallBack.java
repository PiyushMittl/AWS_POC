package com.piyush.sms.lambda;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piyush.product.aws.core.sns.SNSOperations;
import com.piyush.product.aws.core.sns.builder.Message;
import com.piyush.product.aws.core.sns.builder.NotificationClient;
import com.piyush.product.aws.core.sns.exception.SNSException;
import com.piyush.product.aws.core.sns.factory.SNSFactory;

public class CallBack implements RequestHandler<Object, String> {
	private static Connection connection = null;

	@Override
	public String handleRequest(Object arg0, Context arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public String messagingRequest(InputStream inputStream, Context arg1) throws IOException {

		String payload = IOUtils.toString(inputStream, "UTF-8");
		List<String> mobileNumber = new ArrayList<>();
		String message = null;

		Map<String, Object> payloadObject = new ObjectMapper().readValue(payload, Map.class);

		Map<String, String> payloadData = (LinkedHashMap) payloadObject.get("data");
		for (Map.Entry<String, String> e : payloadData.entrySet()) {

			System.out.println("Key " + e.getKey());
			System.out.println("value " + e.getValue());

			if (e.getKey().equals("number")) {
				mobileNumber.add("" + e.getValue());
			}
			if (e.getKey().equals("message")) {
				message = "" + e.getValue();
			}

		}
		
		NotificationClient nc;
		if(message!=null && !message.isEmpty() && mobileNumber!=null && !mobileNumber.isEmpty())
		try {
			nc = new NotificationClient.NotificationClientBuilder()
					.withCredential(SNSFactory.getCredentials()).withRegion(Regions.US_EAST_1).build();
			Message m = new Message.MessageBuilder().notificationClient(nc).mobileNumber(mobileNumber).message(message)
					.build();
			SNSOperations.sendMessage(m);
		} catch (SNSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("Message Sent!");

		return null;
	}

}
