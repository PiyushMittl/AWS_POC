package com.aws.demo.stepfunction;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.AWSStepFunctionsClient;
import com.amazonaws.services.stepfunctions.AWSStepFunctionsClientBuilder;
import com.amazonaws.services.stepfunctions.model.GetActivityTaskRequest;
import com.amazonaws.services.stepfunctions.model.GetActivityTaskResult;
import com.amazonaws.services.stepfunctions.model.SendTaskFailureRequest;
import com.amazonaws.services.stepfunctions.model.SendTaskSuccessRequest;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.concurrent.TimeUnit;

public class GreeterActivities {

	public String getGreeting(String who) throws Exception {
		return "{\"Hello\": \"" + who + "\"}";
	}

	static AWSCredentials credentials = new BasicAWSCredentials("AKIAIDKUZPM5OMYLXFVA",
			"XITjUnboU3hThgyuR32sAJ3vnBibuVEXDEjB/Nda");

	public static void main(final String[] args) throws Exception {

		GreeterActivities greeterActivities = new GreeterActivities();
		ClientConfiguration clientConfiguration = new ClientConfiguration();
		clientConfiguration.setSocketTimeout((int) TimeUnit.SECONDS.toMillis(70));

		AWSStepFunctions client = new AWSStepFunctionsClient(credentials).withRegion(Regions.US_WEST_2);

		while (true) {
			GetActivityTaskResult getActivityTaskResult = client.getActivityTask(new GetActivityTaskRequest()
					.withActivityArn("arn:aws:states:us-west-2:587615792110:activity:getGreeting"));

			if (getActivityTaskResult.getTaskToken() != null) {
				try {
					JsonNode json = Jackson.jsonNodeOf(getActivityTaskResult.getInput());
					String greetingResult = greeterActivities.getGreeting(json.get("who").textValue());
					client.sendTaskSuccess(new SendTaskSuccessRequest().withOutput(greetingResult)
							.withTaskToken(getActivityTaskResult.getTaskToken()));
					System.out.println(greetingResult);
					
				} catch (Exception e) {
					client.sendTaskFailure(
							new SendTaskFailureRequest().withTaskToken(getActivityTaskResult.getTaskToken()));
				}
			} else {
				Thread.sleep(1000);
			}
		}
	}
}