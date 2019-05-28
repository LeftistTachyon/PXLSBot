package pxlsbot;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import pxlsbot.PXLSBotMain.ImagePixelIterator;

public class QuickieTest1 {

    public static void main(String[] args) throws IOException {
        BufferedImage bi = ImageIO.read(new File("testest.png"));
        ImagePixelIterator ipi = new PXLSBotMain.ImagePixelIterator(bi);
        for (Point p = ipi.next(); p != null; p = ipi.next()) {
            System.out.println("got: " + p);
        }
    }
}
