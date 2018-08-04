package com.aws.demo.stepfunction;

import static com.amazonaws.services.stepfunctions.builder.StepFunctionBuilder.end;
import static com.amazonaws.services.stepfunctions.builder.StepFunctionBuilder.stateMachine;
import static com.amazonaws.services.stepfunctions.builder.StepFunctionBuilder.taskState;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.AWSStepFunctionsClient;
import com.amazonaws.services.stepfunctions.AWSStepFunctionsClientBuilder;
import com.amazonaws.services.stepfunctions.builder.StateMachine;
import com.amazonaws.services.stepfunctions.model.CreateStateMachineRequest;
import com.amazonaws.services.stepfunctions.model.StartExecutionRequest;
import com.amazonaws.services.stepfunctions.model.StartExecutionResult;
import com.google.gson.JsonObject;

public class StateEngine {

	public static void main(String[] args) {

		// creates a json for state machine

		// createStateMachine();
		executeStateMachine();
	}

	public static void createStateMachine() {
		final StateMachine stateMachine = stateMachine()
				.comment("A Hello World example of the Amazon States Language using an AWS Lambda Function")
				.startAt("HelloWorld").state("HelloWorld", taskState()
						.resource("arn:aws:states:us-west-2:587615792110:activity:getGreeting").transition(end()))
				.build();
		System.out.println(stateMachine.toPrettyJson());

		AWSCredentials credentials = new BasicAWSCredentials("AKIAIDKUZPM5OMYLXFVA",
				"XITjUnboU3hThgyuR32sAJ3vnBibuVEXDEjB/Nda");
		final AWSStepFunctions client = new AWSStepFunctionsClient(credentials).withRegion(Regions.US_WEST_2);

		client.createStateMachine(new CreateStateMachineRequest().withName("HelloWorldStateMachine")
				.withRoleArn("arn:aws:iam::587615792110:role/Full").withDefinition(stateMachine));
	}

	public static void executeStateMachine() {

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("who", "piyush mittal");
		
		AWSCredentials credentials = new BasicAWSCredentials("AKIAIDKUZPM5OMYLXFVA",
				"XITjUnboU3hThgyuR32sAJ3vnBibuVEXDEjB/Nda");
		final AWSStepFunctions client = new AWSStepFunctionsClient(credentials).withRegion(Regions.US_WEST_2);

		StartExecutionResult startExecutionResult= client.startExecution(new StartExecutionRequest().withInput(jsonObject.toString())
				.withStateMachineArn("arn:aws:states:us-west-2:587615792110:stateMachine:getGreeting"));

		System.out.println(startExecutionResult);
		
	}

}