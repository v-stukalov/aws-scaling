Fully-functional Spring JMS sample application. 
It lets the user to upload files and generate thumbnails.
Behind the scene it utilizes Amazon SQS queues and S3 bucket to store image result.

In fact, this is a fusion of two samples:
https://aws.amazon.com/blogs/developer/using-amazon-sqs-with-spring-boot-and-spring-jms/ and 
https://spring.io/guides/gs/uploading-files

The latter is a complete application and works out of the box, whereas the former is just an outline of the main idea and pile of code pieces.
