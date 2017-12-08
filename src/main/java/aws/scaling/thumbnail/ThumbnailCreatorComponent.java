package aws.scaling.thumbnail;

import aws.scaling.storage.AmazonSimpleStorageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;

@Component
@Profile("processor")
class ThumbnailCreatorComponent {
    private Logger log = Logger.getLogger(ThumbnailCreatorComponent.class);

    @Autowired
    private AmazonSimpleStorageService s3Service;

    public String createThumbnail(String fileName) {
        Path path = s3Service.load(fileName);
        String tmbName = new StringBuilder(fileName).insert(fileName.lastIndexOf('.'), "_thumbnail").toString();
        File tmbFile = ThumbnailProcessor.scaleImage(path.toFile(), tmbName);
        s3Service.store(tmbFile);
        return tmbName;
    }
}
