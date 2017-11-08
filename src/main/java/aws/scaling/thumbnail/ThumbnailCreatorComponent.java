package aws.scaling.thumbnail;

import aws.scaling.ImageProcessor;
import aws.scaling.storage.S3Services;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
class ThumbnailCreatorComponent {
    private Logger log = Logger.getLogger(ThumbnailCreatorComponent.class);

    @Autowired
    private
    S3Services s3Services;

    public String createThumbnail(String imageUrl) {
        S3Object s3Object = s3Services.download(imageUrl);
        BufferedImage image = ImageProcessor.imageFromInputStream(s3Object.getObjectContent());
        image = ImageProcessor.scaleImage(image);
        String thumbnailUrl = new StringBuilder(imageUrl).insert(imageUrl.lastIndexOf('.'), "_thumbnail").toString();
        s3Services.upload(image, thumbnailUrl);
        return thumbnailUrl;
    }
}
