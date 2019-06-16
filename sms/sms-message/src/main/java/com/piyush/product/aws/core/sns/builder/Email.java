/*
 * 
 */
package com.piyush.product.aws.core.sns.builder;

import java.util.List;
import java.util.Map;

import com.piyush.product.aws.core.sns.exception.SNSException;
import com.piyush.product.aws.core.sns.utils.SNSUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class Email.
 */
public class Email {

	/** The to. */
	public List<String> to;

	/** The from. */
	public String from;

	/** The text body. */
	public String textBody;

	/** The cc. */
	public List<String> cc;

	/** The bcc. */
	public List<String> bcc;

	/** The sub. */
	public String sub;

	/** The sns type. */
	public String snsType;

	/** The html body. */
	public String htmlBody;

	/** The has attachment. */
	public String hasAttachment;

	/** The attachment. */
	public List<Map<String, String>> attachments;

	/** The notification client. */
	public NotificationClient notificationClient;

	/**
	 * Instantiates a new email.
	 *
	 * @param builder
	 *            the builder
	 */
	private Email(EmailBuilder builder) {
		this.to = builder.to;
		this.from = builder.from;
		this.textBody = builder.textBody;
		this.cc = builder.cc;
		this.bcc = builder.bcc;
		this.sub = builder.sub;
		this.snsType = builder.snsType;
		this.htmlBody = builder.htmlBody;
		this.hasAttachment = builder.hasAttachment;
		this.notificationClient = builder.notificationClient;
		this.attachments = builder.attachments;
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
	 * Gets the to.
	 *
	 * @return the to
	 */
	public List<String> getTo() {
		return to;
	}

	/**
	 * Sets the to.
	 *
	 * @param to
	 *            the new to
	 */
	public void setTo(List<String> to) {
		this.to = to;
	}

	/**
	 * Gets the from.
	 *
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Sets the from.
	 *
	 * @param from
	 *            the new from
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * Gets the text body.
	 *
	 * @return the text body
	 */
	public String getTextBody() {
		return textBody;
	}

	/**
	 * Sets the text body.
	 *
	 * @param textBody
	 *            the new text body
	 */
	public void setTextBody(String textBody) {
		this.textBody = textBody;
	}

	/**
	 * Gets the cc.
	 *
	 * @return the cc
	 */
	public List<String> getCc() {
		return cc;
	}

	/**
	 * Sets the cc.
	 *
	 * @param cc
	 *            the new cc
	 */
	public void setCc(List<String> cc) {
		this.cc = cc;
	}

	/**
	 * Gets the bcc.
	 *
	 * @return the bcc
	 */
	public List<String> getBcc() {
		return bcc;
	}

	/**
	 * Sets the bcc.
	 *
	 * @param bcc
	 *            the new bcc
	 */
	public void setBcc(List<String> bcc) {
		this.bcc = bcc;
	}

	/**
	 * Gets the attachment.
	 *
	 * @return the attachment
	 */
	public List<Map<String, String>> getAttachments() {
		return attachments;
	}

	/**
	 * Sets the attachment.
	 *
	 * @param attachment
	 *            the attachment
	 */
	public void setAttachment(List<Map<String, String>> attachments) {
		this.attachments = attachments;
	}

	/**
	 * Gets the sub.
	 *
	 * @return the sub
	 */
	public String getSub() {
		return sub;
	}

	/**
	 * Sets the sub.
	 *
	 * @param sub
	 *            the new sub
	 */
	public void setSub(String sub) {
		this.sub = sub;
	}

	/**
	 * Gets the sns type.
	 *
	 * @return the sns type
	 */
	public String getSnsType() {
		return snsType;
	}

	/**
	 * Sets the sns type.
	 *
	 * @param snsType
	 *            the new sns type
	 */
	public void setSnsType(String snsType) {
		this.snsType = snsType;
	}

	/**
	 * Gets the html body.
	 *
	 * @return the html body
	 */
	public String getHtmlBody() {
		return htmlBody;
	}

	/**
	 * Sets the html body.
	 *
	 * @param htmlBody
	 *            the new html body
	 */
	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
	}

	/**
	 * Gets the checks for attachment.
	 *
	 * @return the checks for attachment
	 */
	public String getHasAttachment() {
		return hasAttachment;
	}

	/**
	 * Sets the checks for attachment.
	 *
	 * @param hasAttachment
	 *            the new checks for attachment
	 */
	public void setHasAttachment(String hasAttachment) {
		this.hasAttachment = hasAttachment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Client: " + this.bcc + ", " + this.cc + ", " + this.to + ", " + this.from + ", " + this.hasAttachment
				+ ", " + this.htmlBody + ", " + this.sub + ", " + this.textBody;
	}

	/**
	 * The Class EmailBuilder.
	 */
	public static class EmailBuilder {

		/** The to. */
		public List<String> to;

		/** The from. */
		public String from;

		/** The text body. */
		public String textBody;

		/** The sub. */
		public String sub = "";

		/** The sns type. */
		public String snsType;

		/** The html body. */
		public String htmlBody;

		/** The has attachment. */
		public String hasAttachment;

		/** The cc. */
		public List<String> cc;

		/** The bcc. */
		public List<String> bcc;

		/** The attachment. */
		public List<Map<String, String>> attachments;

		/** The notification client. */
		public NotificationClient notificationClient;

		/**
		 * Notification client.
		 *
		 * @param notificationClient
		 *            the notification client
		 * @return the email builder
		 */
		public EmailBuilder notificationClient(NotificationClient notificationClient) {
			this.notificationClient = notificationClient;
			return this;
		}

		/**
		 * To.
		 *
		 * @param to
		 *            the to
		 * @return the email builder
		 */
		public EmailBuilder to(List<String> to) {
			this.to = to;
			return this;
		}

		/**
		 * From.
		 *
		 * @param from
		 *            the from
		 * @return the email builder
		 */
		public EmailBuilder from(String from) {
			this.from = from;
			return this;
		}

		/**
		 * Text body.
		 *
		 * @param textBody
		 *            the text body
		 * @return the email builder
		 */
		public EmailBuilder textBody(String textBody) {
			this.textBody = textBody;
			return this;
		}

		/**
		 * Html body.
		 *
		 * @param htmlBody
		 *            the html body
		 * @return the email builder
		 */
		public EmailBuilder htmlBody(String htmlBody) {
			this.htmlBody = htmlBody;
			return this;
		}

		/**
		 * Sub.
		 *
		 * @param sub
		 *            the sub
		 * @return the email builder
		 */
		public EmailBuilder sub(String sub) {
			this.sub = sub;
			return this;
		}

		/**
		 * Checks for attachment.
		 *
		 * @param hasAttachment
		 *            the has attachment
		 * @return the email builder
		 */
		public EmailBuilder hasAttachment(String hasAttachment) {
			this.hasAttachment = hasAttachment;
			return this;
		}

		/**
		 * Cc.
		 *
		 * @param cc
		 *            the cc
		 * @return the email builder
		 */
		public EmailBuilder cc(List<String> cc) {
			this.cc = cc;
			return this;
		}

		/**
		 * Bcc.
		 *
		 * @param bcc
		 *            the bcc
		 * @return the email builder
		 */
		public EmailBuilder bcc(List<String> bcc) {
			this.bcc = bcc;
			return this;
		}

		/**
		 * Attachment.
		 *
		 * @param attachment
		 *            the attachment
		 * @return the email builder
		 */
		public EmailBuilder attachments(List<Map<String, String>> attachments) {
			this.attachments = attachments;
			return this;
		}

		/**
		 * Builds the.
		 *
		 * @return the email
		 * @throws Exception
		 *             the exception
		 */
		// Return the finally consrcuted Notification object
		public Email build() throws Exception {
			Email notification = new Email(this);
			validateNotificationObject(notification);
			return notification;
		}

		/**
		 * Validate notification object.
		 *
		 * @param email
		 *            the email
		 * @throws SNSException
		 *             the SNS exception
		 */
		private void validateNotificationObject(Email email) throws SNSException {

			if (SNSUtils.isNullObject(email)) {
				throw new SNSException("Email object is null");
			}
			if (SNSUtils.isNullObject(email.getNotificationClient())) {
				throw new SNSException("Notification Client object is null");
			}
			if (SNSUtils.isEmptyList(email.getTo())) {
				throw new SNSException("To can not be empty");
			} else if (SNSUtils.isEmptyString(email.getFrom())) {
				throw new SNSException("From can not be empty");
			} else if (email.getHasAttachment() != null) {
				if (email.getHasAttachment().equals("true") && SNSUtils.isEmptyList(email.getAttachments())) {
					throw new SNSException("Has Attachment flag is true but found no attachment");
				}
			} else {
				throw new SNSException("Has Attachment flag is mandatory");
			}
		}
	}
}
