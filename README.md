Fully-functional Spring JMS sample application. 
It lets the user to upload files and generate thumbnails.
Behind the scene it utilizes Amazon SQS queues and S3 bucket to store image result.

In fact, this is a fusion of two samples:
https://aws.amazon.com/blogs/developer/using-amazon-sqs-with-spring-boot-and-spring-jms/ and 
https://spring.io/guides/gs/uploading-files

The latter is a complete application and works out of the box, whereas the former is just an outline of the main idea and pile of code pieces.

You may find usefull the following AWS CLI commands:
`<p>$ aws sqs create-queue --queue-name thumbnail-requests-dlq --region us-east-1
<p>$ aws sqs list-queues --region us-east-1
<p>$ echo '{ "RedrivePolicy": "{\"deadLetterTargetArn\":\"arn:aws:sqs:us-east-1:%YOUR_ACCOUNT_ID%:thumbnail_requests_dlq\",\"maxReceiveCount\":\"1000\"}", "MessageRetentionPeriod": "259200" }' > create-queue.json
<p>$ aws sqs create-queue --queue-name thumbnail-requests --attributes file://create-queue.json --region us-east-1
<p>$ aws sqs create-queue --queue-name thumbnail-results --region us-east-1
<p>NOTE you should choose another unique bucket name
<p>$ aws s3api create-bucket --bucket aws-scaling-thumbnails --region us-east-1
<p>$ aws s3api list-objects --bucket aws-scaling-thumbnails --region us-east-1
<p>$ aws sqs purge-queue --queue-url https://queue.amazonaws.com/%YOUR_ACCOUNT_ID%/thumbnail-requests --region us-east-1

Make sure your application uploaded to S3 bucket
<p>mvn clean install
<p>aws s3 cp target/aws-scaling-0.1.0.jar s3://source-code-bucket-aws-scaling

