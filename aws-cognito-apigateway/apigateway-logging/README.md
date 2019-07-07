Enable API Gateway CloudWatch Logs  
This is a two step process. First, we need to create an IAM role that allows API Gateway to write logs in CloudWatch. Then we need to turn on logging for our API project.
First, log in to your AWS Console and select IAM from the list of services.  
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img1_apigateway-select-iam.png)
 
Select Roles on the left menu.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img2_apigateway-select-roles.png)

Select Create Role.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img3_apigateway-create-role.png) 


Under AWS service, select API Gateway.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img4_apigateway-select-role-as-apigateway.png) 
 
Click Next: Permissions.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img5_apigateway-next-permission.png)
 
Click Next: Review.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img6_apigateway-next-review.png)
 
Enter a Role name and select Create role. In our case, we called our role APIGatewayCloudWatchLogs.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img7_apigateway-create-role.png)
 
Click on the role we just created.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img8_apigateway-select-role.png)
 
Take a note of the Role ARN. We will be needing this soon.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img9_apigateway-copy-role-arn.png) 

Now that we have created our IAM role, let’s turn on logging for our API Gateway project.
Go back to your AWS Console and select API Gateway from the list of services.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img10_apigateway-goto-apigateway.png)
 
Select Settings from the left panel.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img11_apigateway-select-settings.png)
 
Enter the ARN of the IAM role we just created in the CloudWatch log role ARN field and hit Save.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img12_apigateway-paste-role-arn.png)
 
Select your API project from the left panel, select Stages, then pick the stage you want to enable logging for. For the case of our Notes App API, we deployed to the prod stage.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img13_apigateway-select-stage.png)
 
In the Logs tab:
•	Check Enable CloudWatch Logs.
•	Select INFO for Log level to log every request.
•	Check Log full requests/responses data to include entire request and response body in the log.
•	Check Enable Detailed CloudWatch Metrics to track latencies and errors in CloudWatch metrics.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img14_apigateway-stage-logs-tab.png)

 
Scroll to the bottom of the page and click Save Changes. Now our API Gateway requests should be logged via CloudWatch.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img15_apigateway-cloudwatch-logs.png)

Viewing API Gateway CloudWatch Logs
CloudWatch groups log entries into Log Groups and then further into Log Streams. Log Groups and Log Streams can mean different things for different AWS services. For API Gateway, when logging is first enabled in an API project’s stage, API Gateway creates 1 log group for the stage, and 300 log streams in the group ready to store log entries. API Gateway picks one of these streams when there is an incoming request.
To view API Gateway logs, log in to your AWS Console and select CloudWatch from the list of services.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img15_apigateway-open-cloudwatch.png) 

Select Logs from the left panel.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img16_apigateway-cloudwatch-logs-groups.png) 
 
Select the log group prefixed with API-Gateway-Execution-Logs_ followed by the API Gateway id.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img17_apigateway-cloudwatch-logs-groups-log.png)
 
You should see 300 log streams ordered by the last event time. This is the last time a request was recorded. Select the first stream.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img18_apigateway-cloudwatch-logs-groups-log.png)
 
This shows you the log entries grouped by request.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img19_apigateway-cloudwatch-lambda-logs-groups.png)
 
Note that two consecutive groups of logs are not necessarily two consecutive requests in real time. This is because there might be other requests that are processed in between these two that were picked up by one of the other log streams.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img20_apigateway-cloudwatch-lambda-logs-groups-log.png)

Viewing Lambda CloudWatch Logs
For Lambda, each function has its own log group. And the log stream rotates if a new version of the Lambda function has been deployed or if it has been idle for some time.
To view Lambda logs, select Logs again from the left panel. Then select the first log group prefixed with /aws/lambda/ followed by the function name.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img21_apigateway-cloudwatch-lambda-logs-groups-log.png)
 

Select the first stream.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img22_apigateway-cloudwatch-lambda-logs-groups-log.png)
 
You should see START, END and REPORT with basic execution information for each function invocation. You can also see content logged via console.log in your Lambda code.
![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/apigateway-logging/img22_apigateway-cloudwatch-lambda-logs-groups-log.png) 
 
