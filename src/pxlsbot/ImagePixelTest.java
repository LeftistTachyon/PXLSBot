package pxlsbot;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImagePixelTest {
    public static void main(String[] args) throws IOException {
        BufferedImage bi = ImageIO.read(new File("image.png"));
        int w = bi.getWidth(), h = bi.getHeight();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int rgb = bi.getRGB(j, i);
                Color c = new Color(rgb);
                System.out.print("@(" + i + ", " + j + "):\t");
                System.out.printf("r: %3d g: %3d b: %3d a: %3d%n", 
                        c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
            }
        }
    }
}