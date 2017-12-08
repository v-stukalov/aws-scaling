package aws.scaling.storage;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
@Primary
public class AmazonSimpleStorageService implements StorageService {

    private final Logger log = LoggerFactory.getLogger(AmazonSimpleStorageService.class);

    @Autowired
    private AmazonS3 s3client;

    @Autowired
    private FileSystemStorageService fileSystemStorageService;

    @Value("${thumbnails.bucket}")
    private String bucketName;

    @Override
    public void init() {
        try {
            fileSystemStorageService.init();
        } catch (Exception e) {
            // TODO handle this
        }
    }

    @Override
    public void store(MultipartFile file) {
        fileSystemStorageService.store(file);
        Path path = fileSystemStorageService.load(file.getOriginalFilename());
        putS3Object(file.getOriginalFilename(), path.toFile());
    }


    public void store(File file) {
        putS3Object(file.getName(), file);
    }

    @Override
    public Stream<Path> loadAll() {
        return fileSystemStorageService.loadAll();
    }

    @Override
    public Path load(String filename) {
        final S3Object s3Object = getS3Object(filename);
        try (InputStream in = s3Object.getObjectContent()) {
            Path path = fileSystemStorageService.load(filename);
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
            return path;
        } catch (IOException e) {
            log.error("Exception occurred while loading file", e);
        }
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
    }

    private S3Object getS3Object(String key) {
        try {
            return s3client.getObject(new GetObjectRequest(bucketName, key));
        } catch (AmazonClientException e) {
            log.error("Exception occurred while getting S3Object", e);
        }
        return null;
    }

    private void putS3Object(String key, File file) {
        try {
            s3client.putObject(new PutObjectRequest(bucketName, key, file));
        } catch (AmazonClientException e) {
            log.error("Exception occurred while putting S3Object", e);
        }
    }

    public static void s3ObjectToFile(S3Object s3Object, String first) {
        try (InputStream in = s3Object.getObjectContent();) {
            Files.copy(in, Paths.get(first));
            File tmp = File.createTempFile("AS", "");
            Files.copy(in, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
