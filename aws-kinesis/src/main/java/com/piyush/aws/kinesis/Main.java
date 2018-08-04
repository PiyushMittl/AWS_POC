package com.piyush.aws.kinesis;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.PutRecordResult;
import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
import com.google.gson.JsonObject;

public class Main {

	public static void main(String a[]) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("to", "piyush.mittal@ituple.com");
		jsonObject.addProperty("from", "piyush.mittal@ituple.com");
		jsonObject.addProperty("text_body",
				"This is a test mail from AWS SES for testing Multi file attachment		 from s3");
		 jsonObject.addProperty("cc", "");
		 jsonObject.addProperty("bcc", "");
		 jsonObject.addProperty("sub", "");
		 jsonObject.addProperty("sns_type","message");
		jsonObject.addProperty("sns_type", "email");
		 jsonObject.addProperty("html_body", "<h1>this is a test mail</h1>");
		jsonObject.addProperty("phone_number", "+919358909659");

		jsonObject.addProperty("has_attachment", "true");
//		 jsonObject.addProperty("attachment_location", "true");

		JsonObject attachment1 = new JsonObject();
		attachment1.addProperty("file_name", "test.txt");
		attachment1.addProperty("bucket_name", "pm311");

		JsonObject attachment2 = new JsonObject();
		attachment2.addProperty("file_name", "test.txt");
		attachment2.addProperty("bucket_name", "pm311");

		JsonObject attachment3 = new JsonObject();
		attachment3.addProperty("file_name", "test.txt");
		attachment3.addProperty("bucket_name", "pm311");

		JsonArray jarr = new JsonArray();
		jarr.add(attachment1);
		jarr.add(attachment2);
		jarr.add(attachment3);

		jsonObject.add("attachment", jarr);

		List<JsonObject> jsonObjList = new ArrayList();
		jsonObjList.add(jsonObject);

		AWSCredentials credentials = new BasicAWSCredentials("AKIAIDKUZPM5OMYLXFVA",
				"XITjUnboU3hThgyuR32sAJ3vnBibuVEXDEjB/Nda");
		AmazonKinesisClient amazonKinesisClient = new AmazonKinesisClient(credentials);
		amazonKinesisClient.setRegion(Region.getRegion(Regions.US_WEST_2));

		PutRecordRequest putRecordsRequest = new PutRecordRequest();
		putRecordsRequest.setStreamName("notification_stream");
		putRecordsRequest.setPartitionKey("samplekey1");

		putRecordsRequest.withData(ByteBuffer.wrap(jsonObjList.toString().getBytes()));

		PutRecordResult putRecordsResult = amazonKinesisClient.putRecord(putRecordsRequest);
		System.out.println("Put Result" + putRecordsResult);

	}

}
