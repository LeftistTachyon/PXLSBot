package pxlsbot;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class Quickie5 {
    public static void main(String[] args) throws IOException {
        File f = new File("image.png");
        BufferedImage image = ImageIO.read(f);
        System.out.println("Color histogram on " + f.getName() + ":");
        HashMap<Integer, Integer> hist = new HashMap<>();
        
        int w = image.getWidth(), h = image.getHeight();
        for(int i = 0;i++<w;) {
            for(int j = 0;j++<h;) {
                int x = i-1, y = j-1;
                int rgb = image.getRGB(x, y);
                if(hist.containsKey(rgb)) {
                    hist.put(rgb, hist.get(rgb) + 1);
                } else {
                    hist.put(rgb, 1);
                }
            }
        }
        
        for (int i : hist.keySet()) {
            System.out.println((new Color(i, true)).toString() + " occurs " + hist.get(i) + " times");
        }
    }
}