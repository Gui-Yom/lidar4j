package com.limelion.lidar4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Standard SOPAS/CoLa ascii data frame
 */
public class CoLaFrame {

    public static final char STX = 0x02;
    public static final char ETX = 0x03;

    private final Command cmd;
    private final String[] data;

    public CoLaFrame(Command cmd, String[] data) {

        this.cmd = cmd;
        this.data = data;
    }

    /**
     * Parse a valid frame from {@code in}
     * @param in the InputStream to read from
     * @return the parsed frame
     */
    public static CoLaFrame parse(InputStream in) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        int offset = 1;
        try {
            while ((length = in.read(buffer)) != -1) {
                baos.write(buffer, offset, (length < buffer.length ? length - 1 : length) - offset);
                offset = 0;
            }

            String[] packet = baos.toString("ascii").split(" ");
            return new CoLaFrame(Command.valueOf(packet[0] + " " + packet[1]), Arrays.copyOfRange(packet, 2, packet.length));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Command getCmd() {

        return cmd;
    }

    /**
     * @return the frame data as an array of fields
     */
    public String[] getData() {

        return data;
    }

    /**
     * @return a valid frame encapsulated within STX and ETX
     */
    public String make() {

        return STX + toString() + ETX;
    }

    /**
     * Use {@link CoLaFrame#make()} for a valid frame.
     *
     * @return a String representation of this frame
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(cmd.getCommandStr());
        if (data != null)
            for (String s : data)
                sb.append(" ").append(s);
        return sb.toString();
    }
}
