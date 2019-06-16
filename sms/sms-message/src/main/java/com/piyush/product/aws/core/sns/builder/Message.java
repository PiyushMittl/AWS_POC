/*
 * 
 */
package com.piyush.product.aws.core.sns.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.piyush.product.aws.core.sns.exception.SNSException;
import com.piyush.product.aws.core.sns.utils.SNSUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class Message.
 */
public class Message {

	/** The mobile number. */
	private List<String> mobileNumber;

	/** The message. */
	private String message;

	/** The notification client. */
	private NotificationClient notificationClient;

	private Map<String, MessageAttributeValue> smsAttribute;

	public Map<String, MessageAttributeValue> getSmsAttribute() {
		return smsAttribute;
	}

	public void setSmsAttribute(Map<String, MessageAttributeValue> smsAttribute) {
		this.smsAttribute = smsAttribute;
	}

	/**
	 * Gets the notification client.
	 *
	 * @return the notification client
	 */
	public NotificationClient getNotificationClient() {
		return notificationClient;
	}

	/**
	 * Sets the notification client.
	 *
	 * @param notificationClient
	 *            the new notification client
	 */
	public void setNotificationClient(NotificationClient notificationClient) {
		this.notificationClient = notificationClient;
	}

	/**
	 * Gets the mobile number.
	 *
	 * @return the mobile number
	 */
	public List<String> getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * Sets the mobile number.
	 *
	 * @param mobileNumber
	 *            the new mobile number
	 */
	public void setMobileNumber(List<String> mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message
	 *            the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Instantiates a new message.
	 *
	 * @param builder
	 *            the builder
	 */
	private Message(MessageBuilder builder) {
		this.mobileNumber = builder.mobileNumber;
		this.message = builder.message;
		this.notificationClient = builder.notificationClient;
		this.smsAttribute = builder.smsAttribute;
	}

	// All getter, and NO setter to provde immutability

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Client: " + this.message + ", " + this.mobileNumber;
	}

	/**
	 * The Class MessageBuilder.
	 */
	public static class MessageBuilder {

		/** The mobile number. */
		private List<String> mobileNumber;

		/** The message. */
		private String message;

		/** The notification client. */
		private NotificationClient notificationClient;

		private Map<String, MessageAttributeValue> smsAttribute = new HashMap();

		/**
		 * Message builder.
		 *
		 * @param mobileNumber
		 *            the mobile number
		 * @param message
		 *            the message
		 */
		public void MessageBuilder(List<String> mobileNumber, String message) {
			this.mobileNumber = mobileNumber;
			this.message = message;
		}

		/**
		 * Notification client.
		 *
		 * @param notificationClient
		 *            the notification client
		 * @return the message builder
		 */
		public MessageBuilder notificationClient(NotificationClient notificationClient) {
			this.notificationClient = notificationClient;
			return this;
		}

		/**
		 * Message.
		 *
		 * @param mobileNumber
		 *            the mobile number
		 * @return the message builder
		 */
		public MessageBuilder smsAttribute(Map<String, MessageAttributeValue> smsAttribute) {
			this.smsAttribute = smsAttribute;
			return this;
		}

		/**
		 * Message.
		 *
		 * @param mobileNumber
		 *            the mobile number
		 * @return the message builder
		 */
		public MessageBuilder mobileNumber(List<String> mobileNumber) {
			this.mobileNumber = mobileNumber;
			return this;
		}

		/**
		 * Message.
		 *
		 * @param message
		 *            the message
		 * @return the message builder
		 */
		public MessageBuilder message(String message) {
			this.message = message;
			return this;
		}

		/**
		 * Builds the.
		 *
		 * @return the message
		 * @throws SNSException
		 *             the SNS exception
		 */
		// Return the finally consrcuted message object
		public Message build() throws SNSException {
			Message message = new Message(this);
			validateMessageObject(message);
			return message;
		}

		/**
		 * Validate message object.
		 *
		 * @param message
		 *            the message
		 * @throws SNSException
		 *             the SNS exception
		 */
		private void validateMessageObject(Message message) throws SNSException {

			if (SNSUtils.isEmptyList(message.getMobileNumber())) {
				throw new SNSException("mobile number can not be empty");
			}

			// Do some basic validations to check
			// if user object does not break any assumption of system
		}
	}
}
