package com.limelion.lidar4j;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

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

    public static CoLaFrame parse(InputStream in) {

        Scanner scan = new Scanner(in);
        scan.useDelimiter(Pattern.compile("" + ETX));
        String[] packet = scan.next().substring(1).split(" ");
        return new CoLaFrame(Command.valueOf(packet[0] + " " + packet[1]), Arrays.copyOfRange(packet, 2, packet.length));
    }

    public Command getCmd() {

        return cmd;
    }

    public String[] getData() {

        return data;
    }

    public String make() {

        StringBuilder sb = new StringBuilder();
        sb.append(STX);
        sb.append(cmd.getCommandStr());
        if (data != null)
            for (String s : data)
                sb.append(" ").append(s);
        sb.append(ETX);
        return sb.toString();
    }
}
