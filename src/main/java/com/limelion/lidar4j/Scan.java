package com.limelion.lidar4j;

public class Scan {

    private float[] angles;
    // Premultiplied ranges
    private float[] ranges;
    // Find object
    private int firstRay;
    private int lastRay;
    private float objectAngle;

    public static Scan parse(ScanDataResponse[] sdr, float start, float end, float threshold) {

        Scan s = new Scan();

        int number = (int) ((end - start) / 3);// + 1;
        s.angles = new float[number];
        s.ranges = new float[number];

        System.out.println(number);

        for (int i = 0; i < sdr.length; ++i) {

            float angle = sdr[i].getStartingAngle()[0] / 10000;
            float angleStep = sdr[i].getAngularStepWidth()[0] / 10000;
            float scalingFactor = sdr[i].getScalingFactor()[0];
            int[] ranges = sdr[i].getData()[0];
            int k = 0;

            for (int j = 0; j < ranges.length; ++j) {
                if (angle >= start && angle < end) {
                    s.angles[k] += angle;
                    s.ranges[k] += ranges[j] * scalingFactor;
                    ++k;
                }
                angle += angleStep;
            }
            System.out.println(k);
        }
        for (int i = 0; i < s.angles.length; ++i) {

            s.angles[i] /= sdr.length;
            s.ranges[i] /= sdr.length;
        }

        s.firstRay = -1;
        s.lastRay = -1;

        for (int i = 0; i < s.ranges.length; ++i) {
            if (s.ranges[i] <= threshold) {
                s.lastRay = i;
                if (s.firstRay == -1)
                    s.firstRay = i;
            }
        }
        //System.out.printf("Infered rays : %d -> %d%n", s.firstRay, s.lastRay);

        double ux = s.ranges[s.lastRay] * Math.cos(Math.toRadians(s.angles[s.lastRay])) - s.ranges[s.firstRay] * Math.cos(Math.toRadians(s.angles[s.firstRay]));
        double uy = s.ranges[s.lastRay] * Math.sin(Math.toRadians(s.angles[s.lastRay])) - s.ranges[s.firstRay] * Math.sin(Math.toRadians(s.angles[s.firstRay]));
        s.objectAngle = (float) Math.toDegrees(Math.PI - Math.acos(ux / Math.sqrt(Math.pow(ux, 2) + Math.pow(uy, 2))));
        System.out.printf("Object angle : %f deg%n", s.objectAngle);

        return s;
    }

    public static Scan parse(ScanDataResponse[] sdr, float threshold) {

        return parse(sdr, -45, 225, threshold);
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

    public float[] getAngles() {

        return angles;
    }

    public float[] getRanges() {

        return ranges;
    }
}
