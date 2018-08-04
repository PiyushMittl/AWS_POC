package com.amazonaws.demo.sns.mail;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;

public class MailSNS {

	private static final String SAMPLE = "sample msg";

	static AWSCredentials credentials = new BasicAWSCredentials("AKIAJLJP7L62ZLAAMARA",
			"Tul8iMnfHla8k9N4N3ZSX1z+edsw3ojutF/a5oHO");

	public static void main(String args[]) throws Exception {

		// create a new SNS client and set endpoint
		AmazonSNSClient snsClient = new AmazonSNSClient(credentials);
		snsClient.setRegion(Region.getRegion(Regions.US_WEST_2));

		// create a new SNS topic
		CreateTopicRequest createTopicRequest = new CreateTopicRequest("MyNewTopic");
		CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
		// print TopicArn
		System.out.println(createTopicResult);
		// get request id for CreateTopicRequest from SNS metadata
		System.out.println("CreateTopicRequest - " + snsClient.getCachedResponseMetadata(createTopicRequest));

		
		
		//subscribe to an SNS topic
		SubscribeRequest subRequest = new SubscribeRequest(createTopicResult.getTopicArn(), "email", "rishi.mishra@ituple.com");
		
		snsClient.subscribe(subRequest);
		//get request id for SubscribeRequest from SNS metadata
		System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequest));
		System.out.println("Check your email and confirm subscription.");
		
		
		
		
		//publish to an SNS topic
		String msg = "My text published to SNS topic with email endpoint";
		PublishRequest publishRequest = new PublishRequest(createTopicResult.getTopicArn(), msg);
		PublishResult publishResult = snsClient.publish(publishRequest);
		//print MessageId of message published to SNS topic
		System.out.println("MessageId - " + publishResult.getMessageId());
		
		
	}
}