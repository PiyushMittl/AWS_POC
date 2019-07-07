Why oauth2.0:

What Is OAuth?

To begin at a high level, OAuth is not an API or a service: it’s an open standard for authorization and anyone can implement it.

More specifically, OAuth is a standard that apps can use to provide client applications with “secure delegated access”. OAuth works over HTTPS and authorizes devices, APIs, servers, and applications with access tokens rather than credentials.

There are two versions of OAuth: OAuth 1.0a and OAuth 2.0. These specifications are completely different from one another, and cannot be used together: there is no backwards compatibility between them.

Which one is more popular? Great question! Nowadays, OAuth 2.0 is the most widely used form of OAuth. So from now on, whenever I say “OAuth”, I’m talking about OAuth 2.0 – as it’s most likely what you’ll be using.

Why OAuth?

OAuth was created as a response to the direct authentication pattern. This pattern was made famous by HTTP Basic Authentication, where the user is prompted for a username and password. Basic Authentication is still used as a primitive form of API authentication for server-side applications: instead of sending a username and password to the server with each request, the user sends an API key ID and secret. Before OAuth, sites would prompt you to enter your username and password directly into a form and they would login to your data (e.g. your Gmail account) as you. This is often called the password anti-pattern.

To create a better system for the web, federated identity was created for single sign-on (SSO). In this scenario, an end user talks to their identity provider, and the identity provider generates a cryptographically signed token which it hands off to the application to authenticate the user. The application trusts the identity provider. As long as that trust relationship works with the signed assertion, you’re good to go. The diagram below shows how this works.

![Cognito Authorization](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img1_cognito_authorization.PNG)

Federated identity was made famous by SAML 2.0, an OASIS Standard released on March 15, 2005. It’s a large spec but the main two components are its authentication request protocol (aka Web SSO) and the way it packages identity attributes and signs them, called SAML assertions. Okta does this with its SSO chiclets. We send a message, we sign the assertion, inside the assertion it says who the user is, and that it came from Okta. Slap a digital signature on it and you’re good to go.

Use Case :

Any organization building an API based architecture has to build a common security layer around these APIs, basically on the edge so that all the APIs are secured. There are multiple ways to build API security like writing some filters in the case of Java / J2EE application, installing some agents in front of APIs which can make policy decisions etc. One of the most widely used protocol for Authorization is OAuth2. AWS API Gateway provides built-in support to secure APIs using AWS Cognito OAuth2 scopes.

Below is the architecture diagram:

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img2_cognito_cognitoapigateway.PNG)


1.	Invoke AWS Cognito /oauth2/token endpoint with grant_type as client_credentials. Refer https://docs.aws.amazon.com/cognito/latest/developerguide/token-endpoint.html

2.	If the request is valid, AWS Cognito will return a JWT (JSON Web Token) formatted access_token

3.	Pass this token in Authorization header for all API calls

4.	API Gateway makes a call to AWS Cognito to validate the access_token.

5.	AWS Cognito returns token validation response.

6.	If token is valid, API Gateway will validate the OAuth2 scope in the JWT token and ALLOW or DENY API call. This is entirely handled by API Gateway once configuration is in place

7.	Perform the actual API call whether it is a Lambda function or custom web service application.

8.	Return the results from Lambda function.

9.	Return results to API Gateway.

10.	If there are no issues with the Lambda function, API Gateway will return a HTTP 200 with response data to the client application.

There are few prerequisites for setting up this integration:

1.	AWS Account — business or free tier.

2.	Knowledge on AWS API Gateway , AWS Cognito services

3.	Knowledge on OAuth2 protocol

We have to perform the below steps for this integration:

1.	Create a AWS Cognito user pool and configure OAuth agents

2.	Deploy a sample micro webservice application using AWS API Gateway and Lambda/SQS

3.	Configure Cognito Authorizer in API Gateway


Step 1: Create AWS Cognito user pool and setup a OAuth application

•	Login to AWS Management console and navigate to Cognito service

•	Select “Manage your user pools” and click “Create a user pool”

•	Enter a pool name and select “Review defaults”. Then select “Create pool”.

Goto AWS Management Console and search for cognito service.

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img3_cognito_cognito.PNG)

Click on “Create a user pool”

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img4_cognito_cretaeuserpool.PNG)

 Give pool name and click on “Review defaults”

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img4_cognito_manageuserpool.PNG)


Then click on “Create pool”

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img5_cognito_givepoolnameAndreviewdefault.PNG)


Now “user pool” has been created.

After creating “user pool” create a “Domain name”

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img6_cognito_createpool.PNG)

Before creating user pool first check availability of user pool. Once it is available click on “save changes”.

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img7_cognito_domainname.PNG)

After saving changes create “Resource servers” and provide scopes 

Click on Resource server

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img8_cognito_checkavailabilityandsavechanges.PNG)

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img9_cognito-resourse-server-and-add-resourceserver.PNG)

 After creating resource server create an “App client”

Provide “App client name” and click on create app client.

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img10_cognito-resourceserver-and-scoopes.PNG)

 
Once app client is created app client id and app client secret would be available.

Click on show details to get app client id and client secret.

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img11_cognito-appclient-create.PNG)

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img12_cognito-appclient-show-detail.PNG)

Now click on “App client settings” and check “Select all” and “Client credentials”. Then save changes.

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img13_cognito-appclient-showdetails.PNG)

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img14_cognito-appclientsetting.PNG)

Now goto “Domain name” and copy “Amazon Cognito domain”.

This api will be used to generate auth_token using scopes.

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img14_cognito-selectall-clientcredentials-allowedscopes.PNG)

Copy above domain. Open POSTMAN paste domain in post type request. Click on Authorization. tab

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img15_cognito-domainname-copyurl.PNG)

Copy App clinet id and App client secret and paste Appclient in Username and App client secret in password.

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img16_cognito-pasteurl.PNG)

Now click on Body tab and put two keys “scope” and “grant_type”.

“client_credential” would be your grant_type and provide already created scopes in scope.

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img17_cognito-appclient-copy-clientid-and-secret.PNG)

Now post the request by clicking on “Send” button.

It will retuen “access_token”

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img18_cognito-putscope-and-granttype.PNG)

![Throughput Graph](https://github.com/PiyushMittl/AWS_POC/blob/master/aws-cognito-apigateway/img19_cognito-makepost-call-gettoken.PNG) 

Cont…