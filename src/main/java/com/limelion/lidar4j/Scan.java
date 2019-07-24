package com.limelion.lidar4j;

public class Scan {

    private float[] x;
    // Premultiplied ranges
    private float[] y;
    // Find object
    private int firstRay;
    private int lastRay;
    private float objectAngle;

    // TODO convert from polar to cartesian
    public static Scan parse(ScanDataResponse[] sdr, float start, float end, float threshold) {

        Scan s = new Scan();

        int number = (int) ((end - start) / 3);// + 1;
        s.x = new float[number];
        s.y = new float[number];

        System.out.println(number);

        for (int i = 0; i < sdr.length; ++i) {

            float angle = sdr[i].getStartingAngle()[0] / 10000;
            float angleStep = sdr[i].getAngularStepWidth()[0] / 10000;
            float scalingFactor = sdr[i].getScalingFactor()[0];
            int[] ranges = sdr[i].getData()[0];
            int k = 0;

            for (int j = 0; j < ranges.length; ++j) {
                if (angle >= start && angle < end) {

                    s.x[k] = ranges[i] * (float) Math.cos(Math.toRadians(angle)) * scalingFactor;
                    s.y[k] = ranges[i] * (float) Math.sin(Math.toRadians(angle)) * scalingFactor;
                    ++k;
                }
                angle += angleStep;
            }
            System.out.println(k);
        }
        for (int i = 0; i < number; ++i) {

            s.x[i] /= sdr.length;
            s.y[i] /= sdr.length;
        }

        s.firstRay = -1;
        s.lastRay = -1;

        for (int i = 0; i < number; ++i) {
            if (distance(s.x[i], s.y[i]) <= threshold) {
                s.lastRay = i;
                if (s.firstRay == -1)
                    s.firstRay = i;
            }
        }
        //System.out.printf("Infered rays : %d -> %d%n", s.firstRay, s.lastRay);

        double ux = s.x[s.lastRay] - s.x[s.firstRay];
        double uy = s.y[s.lastRay] - s.y[s.firstRay];
        s.objectAngle = (float) Math.toDegrees(Math.PI - Math.acos(ux / Math.sqrt(Math.pow(ux, 2) + Math.pow(uy, 2))));
        //System.out.printf("Object angle : %f deg%n", s.objectAngle);

        return s;
    }

    public static Scan parse(ScanDataResponse[] sdr, float threshold) {

        return parse(sdr, -45, 225, threshold);
    }

    private static float distance(float x, float y) {

        return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public float getObjectAngle() {

        return objectAngle;
    }

    public int getFirstRay() {

        return firstRay;
    }

    public int getLastRay() {

        return lastRay;
    }

    public float[] getX() {

        return x;
    }

    public float[] getY() {

        return y;
    }
}
