Enable API Gateway CloudWatch Logs
This is a two step process. First, we need to create an IAM role that allows API Gateway to write logs in CloudWatch. Then we need to turn on logging for our API project.
First, log in to your AWS Console and select IAM from the list of services.
 
Select Roles on the left menu.
 

Select Create Role.
 


Under AWS service, select API Gateway.
 
Click Next: Permissions.

 
Click Next: Review.
 
Enter a Role name and select Create role. In our case, we called our role APIGatewayCloudWatchLogs.
 
Click on the role we just created.
 
Take a note of the Role ARN. We will be needing this soon.
 

Now that we have created our IAM role, let’s turn on logging for our API Gateway project.
Go back to your AWS Console and select API Gateway from the list of services.
 
Select Settings from the left panel.
 
Enter the ARN of the IAM role we just created in the CloudWatch log role ARN field and hit Save.
 
Select your API project from the left panel, select Stages, then pick the stage you want to enable logging for. For the case of our Notes App API, we deployed to the prod stage.
 
In the Logs tab:
•	Check Enable CloudWatch Logs.
•	Select INFO for Log level to log every request.
•	Check Log full requests/responses data to include entire request and response body in the log.
•	Check Enable Detailed CloudWatch Metrics to track latencies and errors in CloudWatch metrics.

 
Scroll to the bottom of the page and click Save Changes. Now our API Gateway requests should be logged via CloudWatch.


Viewing API Gateway CloudWatch Logs
CloudWatch groups log entries into Log Groups and then further into Log Streams. Log Groups and Log Streams can mean different things for different AWS services. For API Gateway, when logging is first enabled in an API project’s stage, API Gateway creates 1 log group for the stage, and 300 log streams in the group ready to store log entries. API Gateway picks one of these streams when there is an incoming request.
To view API Gateway logs, log in to your AWS Console and select CloudWatch from the list of services.
 

Select Logs from the left panel.
 
 
Select the log group prefixed with API-Gateway-Execution-Logs_ followed by the API Gateway id.
 
You should see 300 log streams ordered by the last event time. This is the last time a request was recorded. Select the first stream.
 
This shows you the log entries grouped by request.
 
Note that two consecutive groups of logs are not necessarily two consecutive requests in real time. This is because there might be other requests that are processed in between these two that were picked up by one of the other log streams.

Viewing Lambda CloudWatch Logs
For Lambda, each function has its own log group. And the log stream rotates if a new version of the Lambda function has been deployed or if it has been idle for some time.
To view Lambda logs, select Logs again from the left panel. Then select the first log group prefixed with /aws/lambda/ followed by the function name.

 

Select the first stream.
 
You should see START, END and REPORT with basic execution information for each function invocation. You can also see content logged via console.log in your Lambda code.
 
 