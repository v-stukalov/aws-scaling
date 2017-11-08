package aws.scaling.storage;

import java.awt.image.BufferedImage;
import java.io.*;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import javax.imageio.ImageIO;

@Service
public class S3ServicesImpl implements S3Services {

    private final Logger log = LoggerFactory.getLogger(S3ServicesImpl.class);

    @Autowired
    private AmazonS3 s3client;

    @Value("${s3.bucket}")
    private String bucketName;

    @Override
    public S3Object download(String key) {
        S3Object s3Object = null;
        try {
            System.out.println("Downloading an object");
            s3Object = s3client.getObject(new GetObjectRequest(bucketName, key));
            System.out.println("Content-Type: " + s3Object.getObjectMetadata().getContentType());
            log.info("===================== Import File - Done! =====================");
        } catch (AmazonServiceException ase) {
            log.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
            log.info("Error Message:    " + ase.getMessage());
            log.info("HTTP Status Code: " + ase.getStatusCode());
            log.info("AWS Error Code:   " + ase.getErrorCode());
            log.info("Error Type:       " + ase.getErrorType());
            log.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            log.info("Caught an AmazonClientException: ");
            log.info("Error Message: " + ace.getMessage());
        }
        return s3Object;
    }

    @Override
    public void upload(String key, File file) {
        try {
            s3client.putObject(new PutObjectRequest(bucketName, key, file));
            log.info("===================== Upload File - Done! =====================");
        } catch (AmazonServiceException ase) {
            log.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
            log.info("Error Message:    " + ase.getMessage());
            log.info("HTTP Status Code: " + ase.getStatusCode());
            log.info("AWS Error Code:   " + ase.getErrorCode());
            log.info("Error Type:       " + ase.getErrorType());
            log.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            log.info("Caught an AmazonClientException: ");
            log.info("Error Message: " + ace.getMessage());
        }
    }

    @Override
    public void upload(BufferedImage image, String key) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpeg", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buffer = os.toByteArray();
        InputStream is = new ByteArrayInputStream(buffer);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(buffer.length);
        s3client.putObject(new PutObjectRequest(bucketName, key, is, meta));
    }
}
