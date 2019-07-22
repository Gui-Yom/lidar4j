package com.limelion.lidar4j;

public class ScanDataResponse {

    private int versionNumber;
    private int deviceNumber;
    private long serialNumber;
    private byte deviceStatus;
    private int telegramCount;
    private int scanCounter;
    private long timeSinceStartup;
    private long timeOfTransmission;
    private byte inputStatus;
    private byte outputStatus;
    private short checksum;
    private long scanningFrequency;
    private long measuredFrequency;
    private int numberEncoders;
    private int number16bitChannels;
    private float[] scalingFactor;
    private float[] scalingOffset;
    private long[] startingAngle;
    private int[] angularStepWidth;
    private int[] numberData;
    private int[][] data;
    private int number8bitChannels;
    private boolean position;
    private boolean name;
    private boolean comment;
    private boolean timeInfo;
    private boolean eventInfo;

    static float parseHexFloat(String s) {

        return Float.intBitsToFloat(((Long) Long.parseLong(s, 16)).intValue());
    }

    public static ScanDataResponse parse(CoLaFrame frame) {

        String[] frameData = frame.getData();
        ScanDataResponse sdr = new ScanDataResponse();

        if (frame.getCmd() != Command.Response.GET_SCAN_DATA)
            throw new IllegalArgumentException("Wrong frame !");

        int i = 0;
        sdr.versionNumber = Integer.parseUnsignedInt(frameData[i], 16);
        ++i;
        sdr.deviceNumber = Integer.parseUnsignedInt(frameData[i], 16);
        ++i;
        sdr.serialNumber = Long.parseUnsignedLong(frameData[i], 16);
        i += 2;
        sdr.deviceStatus = Byte.parseByte(frameData[i], 16);
        ++i;
        sdr.telegramCount = Integer.parseUnsignedInt(frameData[i], 16);
        ++i;
        sdr.scanCounter = Integer.parseUnsignedInt(frameData[i], 16);
        ++i;
        sdr.timeSinceStartup = Long.parseUnsignedLong(frameData[i], 16);
        ++i;
        sdr.timeOfTransmission = Long.parseUnsignedLong(frameData[i], 16);
        i += 2;
        sdr.inputStatus = Byte.parseByte(frameData[i], 16);
        i += 2;
        sdr.outputStatus = Byte.parseByte(frameData[i], 16);
        ++i;
        sdr.checksum = Short.parseShort(frameData[i], 16);
        i += 2; // Ignore reserved field
        sdr.scanningFrequency = Long.parseUnsignedLong(frameData[i], 16);
        ++i;
        sdr.measuredFrequency = Long.parseUnsignedLong(frameData[i], 16);
        ++i;
        sdr.numberEncoders = Integer.parseUnsignedInt(frameData[i], 16);
        ++i;
        sdr.number16bitChannels = Integer.parseUnsignedInt(frameData[i], 16);
        ++i;

        sdr.scalingFactor = new float[sdr.number16bitChannels];
        sdr.scalingOffset = new float[sdr.number16bitChannels];
        sdr.startingAngle = new long[sdr.number16bitChannels];
        sdr.angularStepWidth = new int[sdr.number16bitChannels];
        sdr.numberData = new int[sdr.number16bitChannels];
        sdr.data = new int[sdr.number16bitChannels][];

        // Channels
        for (int j = 0; j < sdr.number16bitChannels; ++j) {
            ++i;
            sdr.scalingFactor[j] = parseHexFloat(frameData[i]);
            ++i;
            sdr.scalingOffset[j] = parseHexFloat(frameData[i]);
            ++i;
            sdr.startingAngle[j] = Integer.parseUnsignedInt(frameData[i], 16);
            ++i;
            sdr.angularStepWidth[j] = Integer.parseUnsignedInt(frameData[i], 16);
            ++i;
            sdr.numberData[j] = Integer.parseUnsignedInt(frameData[i], 16);

            sdr.data[j] = new int[sdr.numberData[j]];
            for (int k = 0; k < sdr.numberData[j]; ++k) {
                sdr.data[j][k] = Integer.parseUnsignedInt(frameData[++i], 16);
            }
        }
        ++i;
        sdr.number8bitChannels = Integer.parseUnsignedInt(frameData[i], 16);
        ++i;
        sdr.position = Integer.parseUnsignedInt(frameData[i], 16) > 0;
        ++i;
        sdr.name = Integer.parseUnsignedInt(frameData[i], 16) > 0;
        ++i;
        sdr.comment = Integer.parseUnsignedInt(frameData[i], 16) > 0;
        ++i;
        sdr.timeInfo = Integer.parseUnsignedInt(frameData[i], 16) > 0;
        ++i;
        sdr.eventInfo = Integer.parseUnsignedInt(frameData[i], 16) > 0;

        System.out.printf("Parsed %d/%d fields.%n", i + 1, frameData.length);

        return sdr;
    }

    public int getVersionNumber() {

        return versionNumber;
    }

    public int getDeviceNumber() {

        return deviceNumber;
    }

    public long getSerialNumber() {

        return serialNumber;
    }

    public byte getDeviceStatus() {

        return deviceStatus;
    }

    public int getTelegramCount() {

        return telegramCount;
    }

    public int getScanCounter() {

        return scanCounter;
    }

    public long getTimeSinceStartup() {

        return timeSinceStartup;
    }

    public long getTimeOfTransmission() {

        return timeOfTransmission;
    }

    public byte getInputStatus() {

        return inputStatus;
    }

    public byte getOutputStatus() {

        return outputStatus;
    }

    public short getChecksum() {

        return checksum;
    }

    public long getScanningFrequency() {

        return scanningFrequency;
    }

    public long getMeasuredFrequency() {

        return measuredFrequency;
    }

    public int getNumberEncoders() {

        return numberEncoders;
    }

    public int getNumber16bitChannels() {

        return number16bitChannels;
    }

    public float[] getScalingFactor() {

        return scalingFactor;
    }

    public float[] getScalingOffset() {

        return scalingOffset;
    }

    public long[] getStartingAngle() {

        return startingAngle;
    }

    public int[] getAngularStepWidth() {

        return angularStepWidth;
    }

    public int[] getNumberData() {

        return numberData;
    }

    public int[][] getData() {

        return data;
    }

    public int getNumber8bitChannels() {

        return number8bitChannels;
    }

    public boolean isPosition() {

        return position;
    }

    public boolean isName() {

        return name;
    }

    public boolean isComment() {

        return comment;
    }

    public boolean isTimeInfo() {

        return timeInfo;
    }

    public boolean isEventInfo() {

        return eventInfo;
    }
}
