Fully-functional Spring JMS sample application. 
It lets the user to upload files and generate thumbnails.
Behind the scene it utilizes Amazon SQS queues and S3 bucket to store image result.

In fact, this is a fusion of two samples:
https://aws.amazon.com/blogs/developer/using-amazon-sqs-with-spring-boot-and-spring-jms/ and 
https://spring.io/guides/gs/uploading-files

The latter is a complete application and works out of the box, whereas the former is just an outline of the main idea and pile of code pieces.

You may find usefull the following AWS CLI commands:
$ aws sqs create-queue --queue-name thumbnail_requests_dlq
$ aws sqs list-queues --region "us-east-1"
$ nano create-queue.json
{
  "RedrivePolicy": "{\"deadLetterTargetArn\":\"arn:aws:sqs:us-east-1:417207121721:thumbnail_requests_dlq\",\"maxReceiveCount\":\"1000\"}",
  "MessageRetentionPeriod": "259200"
}
$ aws sqs create-queue --queue-name thumbnail_requests --attributes file://create-queue.json --region us-east-1
$ aws sqs create-queue --queue-name thumbnail_results --region us-east-1
$ aws s3api create-bucket --bucket spring-jms-experiment --region us-east-1
$ aws s3api list-objects --bucket spring-jms-experiment --region us-east-1
$ aws sqs purge-queue --queue-url https://queue.amazonaws.com/EXAMPLE21721/thumbnail_requests --region us-east-1
