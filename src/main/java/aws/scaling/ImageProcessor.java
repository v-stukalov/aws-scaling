package aws.scaling;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class ImageProcessor {

    public static void main(String[] args) {
        ImageProcessor.scaleImage("/Users/vitalii.stukalov/Downloads/gallery/images", "demo-1.jpeg", "demo-1-thumb.jpg", 150);
    }

    private static void scaleImage(String sourceFolderName, String sourceName, String targetName, int scaleFactor) {
        String sourceFullPath = sourceFolderName + "/" + sourceName;
        String targetFullPath = sourceFolderName + "/" + targetName;
        File source = new File(sourceFullPath);
        try {
            BufferedImage buffImg = ImageIO.read(source);
            buffImg = Scalr.resize(buffImg, Scalr.Method.SPEED, scaleFactor);
            File target = new File(targetFullPath);
            ImageIO.write(buffImg, "jpeg", target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void scaleImage(File source, String targetFullPath, int scaleFactor) {
        try {
            BufferedImage buffImg = ImageIO.read(source);
            buffImg = Scalr.resize(buffImg, Scalr.Method.SPEED, scaleFactor);
            File target = new File(targetFullPath);
            ImageIO.write(buffImg, "jpeg", target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage imageFromInputStream(InputStream input) {
        BufferedImage image = null;
        try {
            ImageInputStream iis = ImageIO.createImageInputStream(input);
            Iterator readers = ImageIO.getImageReaders(iis);
            ImageReader reader = (ImageReader) readers.next();
            reader.setInput(iis, false);
            image = reader.read(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static BufferedImage scaleImage(BufferedImage image) {
        return Scalr.resize(image, Scalr.Method.SPEED, 150);
    }
}
