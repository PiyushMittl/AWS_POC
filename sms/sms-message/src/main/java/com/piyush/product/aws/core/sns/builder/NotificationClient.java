/*
 * 
 */
package com.piyush.product.aws.core.sns.builder;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Regions;
import com.piyush.product.aws.core.sns.exception.SNSException;

// TODO: Auto-generated Javadoc
/**
 * The Class NotificationClient.
 */
public class NotificationClient {

	/** The regions. */
	private Regions regions;

	/** The aws credential. */
	private AWSCredentials awsCredential;

	/**
	 * Instantiates a new notification client.
	 *
	 * @param builder
	 *            the builder
	 */
	private NotificationClient(NotificationClientBuilder builder) {
		this.regions = builder.regions;
		this.awsCredential = builder.awsCredential;
	}

	// All getter, and NO setter to provde immutability

	/**
	 * Gets the regions.
	 *
	 * @return the regions
	 */
	public Regions getRegions() {
		return regions;
	}

	/**
	 * Sets the regions.
	 *
	 * @param regions
	 *            the new regions
	 */
	public void setRegions(Regions regions) {
		this.regions = regions;
	}

	/**
	 * Gets the aws credential.
	 *
	 * @return the aws credential
	 */
	public AWSCredentials getAwsCredential() {
		return awsCredential;
	}

	/**
	 * Sets the aws credential.
	 *
	 * @param awsCredential
	 *            the new aws credential
	 */
	public void setAwsCredential(AWSCredentials awsCredential) {
		this.awsCredential = awsCredential;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Client: " + this.regions + ", " + this.awsCredential;
	}

	/**
	 * The Class NotificationClientBuilder.
	 */
	public static class NotificationClientBuilder {

		/** The regions. */
		private Regions regions;

		/** The aws credential. */
		private AWSCredentials awsCredential;

		/**
		 * With region.
		 *
		 * @param regions
		 *            the regions
		 * @return the notification client builder
		 */
		public NotificationClientBuilder withRegion(Regions regions) {
			this.regions = regions;
			return this;
		}

		/**
		 * With credential.
		 *
		 * @param awsCredential
		 *            the aws credential
		 * @return the notification client builder
		 */
		public NotificationClientBuilder withCredential(AWSCredentials awsCredential) {
			this.awsCredential = awsCredential;
			return this;
		}

		/**
		 * Builds the.
		 *
		 * @return the notification client
		 * @throws SNSException
		 *             the SNS exception
		 */
		// Return the finally constrcuted Notification object
		public NotificationClient build() throws SNSException {
			NotificationClient notification = new NotificationClient(this);
			validateNotificationObject(notification);
			return notification;
		}

		/**
		 * Validate notification object.
		 *
		 * @param notification
		 *            the notification
		 * @throws SNSException
		 *             the SNS exception
		 */
		private void validateNotificationObject(NotificationClient notification) throws SNSException {

			if (notification.getRegions() == null) {
				throw new SNSException("region can not be empty");
			}
			// Do some basic validations to check
			// if user object does not break any assumption of system
		}

	}
}
