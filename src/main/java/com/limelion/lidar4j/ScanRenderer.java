package com.limelion.lidar4j;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ScanRenderer {

    public static BufferedImage render(Scan scan,
                                       int width,
                                       int height,
                                       Color rayColor,
                                       Color objectRayColor,
                                       Color objectColor,
                                       boolean lineMode,
                                       boolean mirror,
                                       float scale,
                                       float rotation) {

        int centerX = width / 2;
        int centerY = (int) (0.8f * height);
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        //g.setColor(rayColor);

        for (int i = 0; i < scan.getX().length; ++i) {

            if (i >= scan.getFirstRay() && i <= scan.getLastRay())
                g.setColor(objectRayColor);
            else
                g.setColor(rayColor);

            int x = (int) (rotX(scan.getX()[i], scan.getY()[i], rotation) * scale) + centerX;
            int y = (int) (rotY(scan.getX()[i], scan.getY()[i], rotation) * scale) + centerY;

            if (mirror)
                x = width - x;

            if (lineMode)
                g.drawLine(centerX, centerY, x, y);
            else
                g.fillArc(x, y, 5, 5, 0, 360);
        }

        g.setColor(objectColor);

        int x1 = (int) (rotX(scan.getX()[scan.getFirstRay()], scan.getY()[scan.getFirstRay()], rotation) * scale) + centerX;
        int x2 = (int) (rotX(scan.getX()[scan.getLastRay()], scan.getY()[scan.getLastRay()], rotation) * scale) + centerY;

        if (mirror) {
            x1 = width - x1;
            x2 = width - x2;
        }

        g.drawLine(x1,
                   (int) (rotY(scan.getX()[scan.getFirstRay()], scan.getY()[scan.getFirstRay()], rotation) * scale) + centerY,
                   x2,
                   (int) (rotY(scan.getX()[scan.getLastRay()], scan.getY()[scan.getLastRay()], rotation) * scale) + centerY);

        g.dispose();
        return img;
    }

    private static float rotX(float x, float y, float angle) {

        return (float) (x * Math.cos(angle) - y * Math.sin(angle));
    }

    private static float rotY(float x, float y, float angle) {

        return (float) (x * Math.sin(angle) + y * Math.cos(angle));
    }
}
