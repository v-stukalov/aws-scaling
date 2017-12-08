package aws.scaling.thumbnail;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ThumbnailProcessor {
    public static File scaleImage(File source, String targetPath) {
        File target = null;
        try {
            BufferedImage buffImg = ImageIO.read(source);
            buffImg = Scalr.resize(buffImg, Scalr.Method.QUALITY, 300);
            target = new File(targetPath);
            ImageIO.write(buffImg, "png", target);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return target;
    }
}
