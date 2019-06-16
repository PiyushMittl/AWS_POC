package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.piyush.product.aws.core.sns.SNSOperations;
import com.piyush.product.aws.core.sns.builder.Message;
import com.piyush.product.aws.core.sns.builder.NotificationClient;
import com.piyush.product.aws.core.sns.factory.SNSFactory;

public class Main implements RequestStreamHandler {
	private static Connection connection = null;

	public Object customHandleRequest(Object inputStream, Context context) throws IOException {
		System.out.println("inside customHandleRequest");
		String request = inputStream.toString();
		System.out.println(request);
		
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

			System.out.println("before sending message");
			// create client
			NotificationClient nc = new NotificationClient.NotificationClientBuilder().withCredential(SNSFactory.getCredentials()).withRegion(Regions.US_WEST_2)
					.build();
			System.out.println("Client created");
			Message m = new Message.MessageBuilder().notificationClient(nc).mobileNumber(mobileNumber)
					.message("test message").build();
			System.out.println("Message Object created");
			SNSOperations.sendMessage(m);
			System.out.println("Message Sent!");
		} catch (

		Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

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
			NotificationClient nc = new NotificationClient.NotificationClientBuilder().withRegion(Regions.US_WEST_2)
					.build();
			Message m = new Message.MessageBuilder().notificationClient(nc).mobileNumber(mobileNumber)
					.message("test message").build();
			SNSOperations.sendMessage(m);
			System.out.println("Message Sent!");
		} catch (

		Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("inside handleRequest");
		String request = input.toString();
		System.out.println(request);
	}
}
