# Sending SMS Messages with Amazon SNS

this project manages the delivery or send the messages to clients ie mobile numbers.
You can use Amazon SNS to send text messages, or SMS messages, to SMS-enabled devices. You can send a message directly to a phone number, or you can send a message to multiple phone numbers at once.


## Sending Message

first we need to add some numbes to our List and that list would be of String type where we can add multiple numbers and can send same message to multiple numbers in a one go.

### adding number
in n below snippet we have below list (mobileNumber)
``` java
List<String> mobileNumber = new ArrayList<String>();
		mobileNumber.add("+919358909659");
```

### text message
after adding your number we need to have text message which is to be sent to those numbers.
here we have new Message.MessageBuilder() which conains .message() function through which we can set our text message to our Message object and can send it to the user.
``` java
Message m = new Message.MessageBuilder().notificationClient(nc).mobileNumber(mobileNumber)
					.message("test message").build();
```

### sending messsage
after adding number and putting text message to our Message object its time to send text message to the user.
we require SNSOperations class in our package to send our text message.
``` java
SNSOperations.sendMessage(m);
```


------------------------
#### Sample code snippet

``` java
public Object customHandleRequest(Object inputStream, Context context) throws IOException {
		
		List<String> mobileNumber = new ArrayList<String>();
		mobileNumber.add("+919358909659");
    
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
  
  ```
