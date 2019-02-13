package util;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Pontus on 2016-03-06.
 */
public class ImageHandler {
    public static final String filePath = "assets/";

    public static BufferedImage loadImage(String name){
        try{
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream is = classLoader.getResourceAsStream(filePath + name + ".png");
            return ImageIO.read(is);
        }catch(IOException e){
            System.out.println("Unable to load " + name + ".png.");
            e.getStackTrace();
            return null;
        }
    }

    public static BufferedImage cutImage(BufferedImage image, int state, int frame, int width, int height){
        return image.getSubimage(frame*width, state*height, width, height);
    }

    public static BufferedImage scaleImage(BufferedImage unscaled, double scale){
        int w = unscaled.getWidth();
        int h = unscaled.getHeight();
        BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        after = scaleOp.filter(unscaled, after);

        return after;
    }
}
