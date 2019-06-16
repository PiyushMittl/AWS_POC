/*
 * 
 */
package com.piyush.product.aws.core.sns.factory;

import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.GetSMSAttributesRequest;
import com.amazonaws.services.sns.model.SetSMSAttributesRequest;
import com.piyush.product.aws.core.sns.exception.SNSException;
import com.piyush.product.aws.core.sns.utils.SNSUtils;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating SNS objects.
 */
public class SNSFactory {

	/// ***** S3 Bucket *****///

	/**
	 * Gets the amazon simple email service client.
	 *
	 * @param regions
	 *            the regions
	 * @return the amazon simple email service client
	 */
	// AmazonSimpleEmailServiceClient factory for email without attachment
	public static AmazonS3Client getAmazonS3Client(Regions regions) {
		return new AmazonS3Client().withRegion(regions);
	}

	/**
	 * Gets the amazon SNS client.
	 *
	 * @param credentials
	 *            the credentials
	 * @param regions
	 *            the regions
	 * @param setRequest
	 *            the set request
	 * @return the amazon SNS client
	 */
	public static AmazonS3Client getAmazonS3Client(AWSCredentials credentials, Regions regions) {
		AmazonS3Client s3Client = new AmazonS3Client(credentials).withRegion(regions);
		// snsClient = setDefaultSmsAttributes(snsClient, setRequest);
		return s3Client;
	}

	/**
	 * Gets the amazon SNS client.
	 *
	 * @param accessKey
	 *            the access key
	 * @param secretKey
	 *            the secret key
	 * @param regions
	 *            the regions
	 * @param setRequest
	 *            the set request
	 * @return the amazon SNS client
	 * @throws SNSException
	 *             the SNS exception
	 */
	public static AmazonS3Client getAmazonS3Client(String accessKey, String secretKey, Regions regions,
			SetSMSAttributesRequest setRequest) throws SNSException {
		if (SNSUtils.isEmptyString(accessKey) || SNSUtils.isEmptyString(secretKey)) {
			throw new SNSException("Acceess Key or Secret Key is empty");
		}
		AWSCredentials credentials = credentials = new BasicAWSCredentials(accessKey, secretKey);
		AmazonS3Client s3Client = new AmazonS3Client(credentials).withRegion(regions);
		return s3Client;
	}

	/// ***** Messaging *****///

	// factory for message
	/**
	 * Gets the amazon SNS client.
	 *
	 * @param regions
	 *            the regions
	 * @param setRequest
	 *            the set request
	 * @return the amazon SNS client
	 */
	public static AmazonSNSClient getAmazonSNSClient(Regions regions) {

		AmazonSNSClient snsClient = new AmazonSNSClient().withRegion(regions);
		// snsClient = setDefaultSmsAttributes(snsClient, setRequest);
		return snsClient;
	}

	/**
	 * Gets the amazon SNS client.
	 *
	 * @param credentials
	 *            the credentials
	 * @param regions
	 *            the regions
	 * @param setRequest
	 *            the set request
	 * @return the amazon SNS client
	 */
	public static AmazonSNSClient getAmazonSNSClient(AWSCredentials credentials, Regions regions) {
		AmazonSNSClient snsClient = new AmazonSNSClient(credentials).withRegion(regions);
		// snsClient = setDefaultSmsAttributes(snsClient, setRequest);
		return snsClient;
	}

	/**
	 * Gets the amazon SNS client.
	 *
	 * @param accessKey
	 *            the access key
	 * @param secretKey
	 *            the secret key
	 * @param regions
	 *            the regions
	 * @param setRequest
	 *            the set request
	 * @return the amazon SNS client
	 * @throws SNSException
	 *             the SNS exception
	 */
	public static AmazonSNSClient getAmazonSNSClient(String accessKey, String secretKey, Regions regions,
			SetSMSAttributesRequest setRequest) throws SNSException {
		if (SNSUtils.isEmptyString(accessKey) || SNSUtils.isEmptyString(secretKey)) {
			throw new SNSException("Acceess Key or Secret Key is empty");
		}
		AWSCredentials credentials = credentials = new BasicAWSCredentials(accessKey, secretKey);
		AmazonSNSClient snsClient = new AmazonSNSClient(credentials).withRegion(regions);
		snsClient = setDefaultSmsAttributes(snsClient, setRequest);
		return snsClient;
	}

	/**
	 * Sets the default sms attributes.
	 *
	 * @param snsClient
	 *            the sns client
	 * @param setRequest
	 *            the set request
	 * @return the amazon SNS client
	 */
	private static AmazonSNSClient setDefaultSmsAttributes(AmazonSNSClient snsClient,
			SetSMSAttributesRequest setRequest) {

		if (SNSUtils.isNullObject(setRequest)) {

			setRequest = new SetSMSAttributesRequest().addAttributesEntry("DefaultSenderID", "mySenderID")
					.addAttributesEntry("MonthlySpendLimit", "1")
					.addAttributesEntry("DeliveryStatusIAMRole", "arn:aws:iam::123456789012:role/mySnsRole")
					.addAttributesEntry("DeliveryStatusSuccessSamplingRate", "10")
					.addAttributesEntry("DefaultSMSType", "Transactional")
					.addAttributesEntry("UsageReportS3Bucket", "sns-sms-daily-usage");
			snsClient.setSMSAttributes(setRequest);

			Map<String, String> myAttributes = snsClient.getSMSAttributes(new GetSMSAttributesRequest())
					.getAttributes();
			System.out.println("My SMS attributes:");
			for (String key : myAttributes.keySet()) {
				System.out.println(key + " = " + myAttributes.get(key));
			}
		} else {
			snsClient.setSMSAttributes(setRequest);
		}
		return snsClient;
	}

	/// ***** Mailing *****///

	/**
	 * Gets the amazon simple email service client.
	 *
	 * @param regions
	 *            the regions
	 * @return the amazon simple email service client
	 */
	// AmazonSimpleEmailServiceClient factory for email without attachment
	public static AmazonSimpleEmailServiceClient getAmazonSimpleEmailServiceClient(Regions regions) {
		return new AmazonSimpleEmailServiceClient().withRegion(regions);
	}

	/**
	 * Gets the amazon simple email service client.
	 *
	 * @param credentials
	 *            the credentials
	 * @param regions
	 *            the regions
	 * @return the amazon simple email service client
	 */
	public static AmazonSimpleEmailServiceClient getAmazonSimpleEmailServiceClient(AWSCredentials credentials,
			Regions regions) {
		return new AmazonSimpleEmailServiceClient(credentials).withRegion(regions);
	}

	/**
	 * Gets the amazon simple email service client.
	 *
	 * @param accessKey
	 *            the access key
	 * @param secretKey
	 *            the secret key
	 * @param regions
	 *            the regions
	 * @return the amazon simple email service client
	 * @throws SNSException
	 *             the SNS exception
	 */
	public static AmazonSimpleEmailServiceClient getAmazonSimpleEmailServiceClient(String accessKey, String secretKey,
			Regions regions) throws SNSException {
		if (SNSUtils.isEmptyString(accessKey) || SNSUtils.isEmptyString(secretKey)) {
			throw new SNSException("Acceess Key or Secret Key is empty");
		}
		AWSCredentials credentials = credentials = new BasicAWSCredentials(accessKey, secretKey);
		return new AmazonSimpleEmailServiceClient(credentials).withRegion(regions);
	}

	// factory for email with attachment

	/// ***** for local only ***** ///

	/**
	 * Gets the credentials.
	 *
	 * @return the credentials
	 */
	public static BasicAWSCredentials getCredentials() {
		return new BasicAWSCredentials("xxx", "xxx");
	}

}
