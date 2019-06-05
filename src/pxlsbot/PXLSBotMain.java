package pxlsbot;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JWindow;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PXLSBotMain {

    static final List<Integer> palette;

    static {
        List<Color> colors = Arrays.asList(
                new Color(255, 255, 255),
                new Color(205, 205, 205),
                new Color(136, 136, 136),
                new Color(85, 85, 85),
                new Color(34, 34, 34),
                new Color(0, 0, 0),
                new Color(255, 167, 209),
                new Color(229, 0, 0),
                new Color(128, 0, 0),
                new Color(255, 221, 202),
                new Color(246, 179, 137),
                new Color(229, 149, 0),
                new Color(160, 106, 66),
                new Color(96, 64, 40),
                new Color(255, 255, 0),
                new Color(148, 224, 68),
                new Color(2, 190, 1),
                new Color(0, 95, 0),
                new Color(0, 211, 221),
                new Color(0, 131, 199),
                new Color(0, 0, 234),
                new Color(207, 110, 228),
                new Color(255, 0, 255),
                new Color(130, 0, 128));
        palette = new ArrayList<>();
        for (Color c : colors) {
            palette.add(c.getRGB());
        }
    }

    static final BufferedImage image;

    static {
        BufferedImage temp = null;
        try {
            temp = ImageIO.read(new File("image.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        image = temp;
    }

    static JToggleButton button1;

    static JToggleButton button2;

    static JCheckBox offWhenDone;
    
    static JCheckBox repeat;

    static JWindow pointer;

    static Point at = new Point(0, 0);

    static JFrame frame;

    static JTextField tField;

    static volatile boolean flag = true;

    static final long FAST = 10, MED = 140, SLOW = 180;

    public static void main(String[] args) {
        System.out.println("Remember, Y equals QUIT");

        frame = new JFrame("LMAO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.setResizable(false);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(true);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case VK_Y:
                        System.out.println("Y: QUIT!");
                        flag = false;
                        frame.requestFocus();
                        break;
                    case VK_K:
                        System.out.println("WHAT ARE YOU DOING");
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        button1 = new JToggleButton("Click when ready (Color 0)");
        button1.addItemListener((ItemEvent e) -> {
            switch (e.getStateChange()) {
                case ItemEvent.SELECTED:
                    flag = true;
                    whenReadyToggled();
                    frame.requestFocus();
                    break;
                case ItemEvent.DESELECTED:
                    flag = false;
                    frame.requestFocus();
                    break;
            }
        });
        button1.setPreferredSize(new Dimension(200, 100));

        frame.add(button1);

        button2 = new JToggleButton("Spawn pointer (for top left corner)");
        button2.addItemListener((ItemEvent e) -> {
            switch (e.getStateChange()) {
                case ItemEvent.SELECTED:
                    pointer = new Draggable(frame);
                    pointer.setLocation(at);
                    when2Clicked();
                    break;
                case ItemEvent.DESELECTED:
                    when3Clicked();
                    break;
            }
        });
        button2.setPreferredSize(new Dimension(button2.getPreferredSize().width,
                100));

        frame.add(button2);

        JLabel label = new JLabel("Scale:");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        frame.add(label);

        tField = new JTextField(20);
        tField.setFont(new Font("Consolas", Font.PLAIN, 15));
        frame.add(tField);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        Font fTemp = new Font("Segoe UI", Font.PLAIN, 12);
        offWhenDone = new JCheckBox("Turn off computer when completed");
        offWhenDone.setFont(fTemp);
        panel.add(offWhenDone);
        
        repeat = new JCheckBox("Repeat job when done");
        repeat.setFont(fTemp);
        panel.add(repeat);
        
        JCheckBox meme = new JCheckBox("Chew an egg");
        meme.setFont(fTemp);
        meme.addChangeListener((e) -> {
            if (meme.isSelected()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                meme.setSelected(false);
            }
        });
        panel.add(meme);
        
        panel.revalidate();
        
        panel.setPreferredSize(new Dimension(
                offWhenDone.getPreferredSize().width, 100));
        
        frame.add(panel);

        frame.pack();

        frame.setVisible(true);
    }

    /*public static void sleep(long millis) throws InterruptedException {
        final Object LOCK = new Object();
        ScheduledExecutorService stse = Executors.newSingleThreadScheduledExecutor();
        stse.schedule(() -> {
            try {
                synchronized (LOCK) {
                    LOCK.notify();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, millis, TimeUnit.MILLISECONDS);
        synchronized (LOCK) {
            LOCK.wait();
        }
        stse = null;
        System.gc();
    }*/
    static final int transparent = new Color(0, 0, 0, 0).getRGB();

    static int selCol;

    private static void whenReadyToggled() {
        new Thread(() -> {
            System.out.println("trans: " + Integer.toString(transparent, 2));
            try {
                Robot r = new Robot();
                int scale = Integer.parseInt(tField.getText());
                int w = image.getWidth(), h = image.getHeight();
                selCol = 0;
                int x = 0, y = 0;
                
                do {
                    Iterator<Point> pIter = new ImagePixelIterator(image);
                    for (Point p = pIter.next();
                            flag && p != null && p.y < h && p.x < w;
                            p = pIter.next()) {
                        frame.requestFocus();
                        int dX = p.x - x, dY = p.y - y;
                        if (dX != 0) {
                            if (dX < 0) {
                                while (flag && dX++ < 0) {
                                    drag(r, at.x, at.y, at.x + scale, at.y);
                                    System.out.println("left " + dX);
                                    if (dX != 0) {
                                        frame.requestFocus();
                                    }
                                }
                            } else {
                                while (flag && dX-- > 0) {
                                    drag(r, at.x + scale, at.y, at.x, at.y);
                                    System.out.println("right " + dX);
                                    if (dX != 0) {
                                        frame.requestFocus();
                                    }
                                }
                            }
                        }
                        if (!flag) {
                            return;
                        }
                        if (dY != 0) {
                            if (dY < 0) {
                                while (flag && dY++ < 0) {
                                    drag(r, at.x, at.y, at.x, at.y + scale);
                                    System.out.println("up " + dY);
                                    if (dY != 0) {
                                        frame.requestFocus();
                                    }
                                }
                            } else {
                                while (flag && dY-- > 0) {
                                    drag(r, at.x, at.y + scale, at.x, at.y);
                                    System.out.println("down " + dY);
                                    if (dY != 0) {
                                        frame.requestFocus();
                                    }
                                }
                            }
                        }
                        if (!flag) {
                            return;
                        }

                        x = p.x;
                        y = p.y;
                        placePixel(x, y, scale, r);
                        if (!flag) {
                            return;
                        }
                    }
                    /*outer:
                    for (int i = 0; i < h && flag; i++) {
                        for (int j = 0; j < w && flag; j++) {
                            placePixel(j, i, scale, r);

                            if (!flag) {
                                return;
                            }
                            drag(r, at.x + scale, at.y, at.x, at.y);
                            System.out.println("right");
                            frame.requestFocus();
                        }
                        if (!flag) {
                            return;
                        }
                        drag(r, at.x, at.y + scale, at.x, at.y);
                        System.out.println("down");
                        if (!flag) {
                            return;
                        }

                        sleep(MED);

                        drag(r, at.x, at.y, at.x + scale, at.y);
                        System.out.println("left");
                        if (!flag) {
                            return;
                        }

                        i++;
                        if (i >= h) {
                            break outer;
                        }

                        for (int j = w - 1; j >= 0 && flag; j--) {
                            placePixel(j, i, scale, r);

                            if (!flag) {
                                return;
                            }
                            drag(r, at.x, at.y, at.x + scale, at.y);
                            System.out.println("left");
                            frame.requestFocus();
                        }
                        if (!flag) {
                            return;
                        }

                        drag(r, at.x, at.y + scale, at.x, at.y);
                        System.out.println("down");
                        if (!flag) {
                            return;
                        }

                        sleep(MED);

                        drag(r, at.x + scale, at.y, at.x, at.y);
                        System.out.println("right");
                        if (!flag) {
                            return;
                        }
                    }*/
                } while(flag && repeat.isSelected());

                System.out.println("DONE!");

                if (offWhenDone.isSelected()) {
                    System.out.println("Shutting down....");
                    Runtime runtime = Runtime.getRuntime();
                    Process proc = runtime.exec(
                            "shutdown /s /t 0 /c \"Job complete!\"");
                    System.exit(0);
                }
            } catch (AWTException | InterruptedException | IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
    
    private static final long TOGGLE = 80L;

    private static void placePixel(int x, int y, int scale, Robot r) throws InterruptedException {
        int temp_ = image.getRGB(x, y);
        boolean first = true;
        System.out.println("@(" + y + ", " + x + "): "
                + new Color(temp_));
        if (temp_ != transparent) {
            r.mouseMove(at.x + scale, at.y);
            sleep(TOGGLE);
            while (flag && r.getPixelColor(at.x, at.y).getRGB() != temp_) {
                //<editor-fold defaultstate="collapsed" desc="if first">
                if (first) {
                    System.out.println("First!");
                    int idx = palette.indexOf(temp_);
                    System.out.println("idx: " + idx + "\tselCol: " + selCol);
                    if (idx != selCol) {
                        if (idx < selCol) {
                            idx += palette.size();
                        }

                        if (frame.isFocusOwner()) {
                            if (!flag) {
                                return;
                            }
                            r.mouseMove(at.x + scale, at.y);
                            sleep(MED);
                            r.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
                            sleep(SLOW);
                            r.mouseMove(at.x, at.y);
                            sleep(SLOW);
                            r.mouseMove(at.x + scale, at.y);
                            sleep(MED);
                            r.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                            sleep(MED);
                            System.out.println("shake");
                        }
                        
                        for (; selCol < idx && flag; selCol++) {
                            r.keyPress(VK_K);
                            sleep(30);
                            r.keyRelease(VK_K);
                            sleep(30);
                            System.out.print("k");
                        }
                        selCol %= palette.size();
                        System.out.println("\nidx: " + idx + "\tselCol: " + selCol);
                    }

                    first = false;
                }
                //</editor-fold>
                frame.requestFocus();

                if (!flag) {
                    return;
                }
                r.mouseMove(at.x, at.y);
                sleep(FAST);
                r.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
                sleep(FAST);
                r.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                sleep(FAST);
                r.mouseMove(at.x + scale, at.y);
                sleep(TOGGLE);
            }

            sleep(FAST);
        }
    }

    public static void drag(Robot r, int fromX, int fromY, int toX, int toY) throws InterruptedException {
        r.mouseMove(fromX, fromY);
        sleep(MED);
        r.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
        sleep(SLOW);
        r.mouseMove(toX, toY);
        sleep(MED);
        r.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
        sleep(MED);
    }

    public static class Draggable extends JWindow {

        int X;
        int Y;

        Draggable() {

            setBounds(50, 50, 100, 100);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent me) {
                    X = me.getX();
                    Y = me.getY();
                }
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent me) {
                    Point p = getLocation();
                    setLocation(p.x + (me.getX() - X), p.y + (me.getY() - Y));
                }
            });
        }

        Draggable(JFrame parent) {
            super(parent);
            setBounds(50, 50, 100, 100);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent me) {
                    X = me.getX();
                    Y = me.getY();
                }
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent me) {
                    Point p = getLocation();
                    setLocation(p.x + (me.getX() - X), p.y + (me.getY() - Y));
                    repaint();
                }
            });
        }

        @Override
        public void update(Graphics g) {
            super.update(g);
            paint(g);
        }

        private static final BasicStroke STROKE
                = new BasicStroke(5, BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND);

        private int height = 0, width = 0;

        private Paint gradient = new GradientPaint(
                0, 0, Color.PINK,
                width, height, Color.RED);

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            boolean change = false;
            int height_ = getHeight();
            if (height_ != height) {
                height = height_;
                change = true;
            }
            int width_ = getWidth();
            if (width_ != width) {
                width = width_;
                change = true;
            }
            if (change) {
                gradient = new GradientPaint(0, 0, Color.PINK,
                        width, height, Color.RED);
            }
            Graphics2D g2D = (Graphics2D) g;
            g2D.setStroke(STROKE);
            g2D.setPaint(gradient);
            g2D.fillRect(0, 0, width, height);
            g2D.setColor(Color.BLACK);
            g2D.drawRect(0, 0, width, height);
        }
    }

    public static void when2Clicked() {
        pointer.setSize(100, 100);
        pointer.setBackground(Color.MAGENTA);
        pointer.setVisible(true);
        pointer.requestFocus();
    }

    public static void when3Clicked() {
        at = pointer.getLocationOnScreen();
        System.out.println("Point set to (" + at.x + ", " + at.y + ")");
        pointer.dispose();
    }

    public static class ImagePixelIterator implements Iterator<Point> {

        private BufferedImage bi;

        private int x, y;

        public ImagePixelIterator(BufferedImage image, int x, int y) {
            this.x = x;
            this.y = y;
            bi = image;
        }

        public ImagePixelIterator(BufferedImage image) {
            this(image, 0, 0);
        }

        @Override
        public boolean hasNext() {
            return y < bi.getHeight();
        }

        @Override
        public Point next() {
            int h = bi.getHeight(), w = bi.getWidth();
            if (y >= h) {
                return null;
            }
            outer:
            for (; y < h; y++) {
                if (y % 2 == 0) {
                    for (; x < w; x++) {
                        if (bi.getRGB(x, y) != transparent) {
                            break outer;
                        }
                    }
                    x--;
                } else {
                    for (; x >= 0; x--) {
                        if (bi.getRGB(x, y) != transparent) {
                            break outer;
                        }
                    }
                    x++;
                }
            }
            Point output = new Point(x, y);
            if (y % 2 == 0) {
                x++;
                if (x >= w) {
                    x = w - 1;
                    y++;
                }
            } else {
                x--;
                if (x < 0) {
                    x = 0;
                    y++;
                }
            }
            return output;
        }
    }
}
