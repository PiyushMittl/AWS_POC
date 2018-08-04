package com.piyush.poc.texttomp3;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LambdaForTextToMp3 implements RequestStreamHandler {

	AWSCredentials credentials = new BasicAWSCredentials("AKIAJLJP7L62ZLAAMARA",
			"Tul8iMnfHla8k9N4N3ZSX1z+edsw3ojutF/a5oHO");

	public static void main(String[] args) throws IOException {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", "piyush");
		//new LambdaForTextToMp3().handleRequest(new InputStream(jsonObject), new OutputStream(), new Context());
	}


	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
		System.out.println("--------------------------> inside handleRequest starts");
		String request = null;
		JsonObject jsonObject = null;
		try {
			System.out.println("--------------------------> before request = IOUtils.toString(inputStream, \"UTF-8\");");
			request = IOUtils.toString(inputStream, "UTF-8");
			System.out.println("--------------------------> before request = "+request);
			
			jsonObject = new JsonParser().parse(request).getAsJsonObject();
			System.out.println("--------------------------> jsonObject = "+jsonObject);
			
			
			System.out.println("--------------------------> text = "+jsonObject.get("text").getAsString());
			
			outputStream = convert(jsonObject.get("text").getAsString());
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			outputStream.write(e.getMessage().getBytes());
		}
	}

	public OutputStream convert(String textToConvert) throws Exception {
		
		System.out.println("--------------------------> convert method starts");
		
		
		PollyForTextToMp3 helloWorld = new PollyForTextToMp3(Region.getRegion(Regions.US_EAST_1));
		
		System.out.println("--------------------------> helloWorld= "+helloWorld);
		
		
		InputStream speechStream = helloWorld.synthesize(textToConvert, OutputFormat.Mp3);
		
		System.out.println("--------------------------> speechStream= "+speechStream);
		
		
		
//		BufferedInputStream bis = new BufferedInputStream(speechStream);
//		
//		System.out.println("--------------------------> BufferedInputStream= "+bis);
//		
//		
//		OutputStream out = new BufferedOutputStream(new ByteArrayOutputStream());
//
//		byte[] buffer = new byte[8 * 1024];
//		try {
//			int bytesRead;
//			while ((bytesRead = bis.read(buffer)) != -1) {
//				out.write(buffer, 0, bytesRead);
//				System.out.println("--------------------------> BufferedInputStream= "+bis);
//				
//			}
//		} finally {
//			out.close();
//		}
//		return out;
		
		System.out.println("calling uploadToS3");
		uploadToS3("asdasddasd", "test.mp3", speechStream, new ObjectMetadata());
		
		return null;
	}
	
	
	
	public <ObjectMatadata> boolean uploadToS3(String bucketName,String keyName,InputStream is,ObjectMetadata objMetaData){
		System.out.println("inside uploadToS3");
		AWSCredentials credentials;
		try {
			credentials = new BasicAWSCredentials("AKIAJLJP7L62ZLAAMARA", "Tul8iMnfHla8k9N4N3ZSX1z+edsw3ojutF/a5oHO");
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (/Users/userid/.aws/credentials), and is in a valid format.", e);
		}

		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_2)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
		try {
			System.out.println("Uploading a new object to S3 from a file .....\n");
			//File file = new File(uploadFileName);
			//s3client.putObject(new PutObjectRequest(bucketName, keyName, file));
			System.out.println("putting object tos3");
			s3client.putObject(new PutObjectRequest(bucketName, keyName, is,objMetaData));
			System.out.println("putting object tos3 done");
			System.out.println("Uploading done");
			return true;
		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which " + "means your request made it "
					+ "to Amazon S3, but was rejected with an error response" + " for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which " + "means the client encountered "
					+ "an internal error while trying to " + "communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
		return false;
	
	}
	

}
