package aws.scaling.storage;

import com.amazonaws.services.s3.model.S3Object;

import java.awt.image.BufferedImage;
import java.io.File;

public interface S3Services {
    S3Object download(String key);

    void upload(String key, File file);

    void upload(BufferedImage image, String key);
}
