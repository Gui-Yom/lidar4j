package com.limelion.lidar4j;

import java.io.*;

/**
 * https://tinyurl.com/yywym8cd
 */
public class TiM510 implements ScanDevice, Closeable {

    public static final int BAUDRATE = 460800;

    private boolean isConnected;
    private BufferedWriter portOutput;
    private InputStream portInput;

    public TiM510(InputStream in, OutputStream out) {

        this.portInput = in;
        this.portOutput = new BufferedWriter(new OutputStreamWriter(out));
        this.isConnected = true;
    }

    public ScanDataResponse getScanData() {

        return ScanDataResponse.parse(makeRequest(Command.Request.GET_SCAN_DATA));
    }

    public CoLaFrame makeRequest(Command.Request cmd) {

        if (!isConnected) {
            throw new RuntimeException("No connection to the scanner is currently open !");
        }

        try {
            portOutput.write(new CoLaFrame(cmd, null).make());
            portOutput.flush();
            return CoLaFrame.parse(portInput);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {

        try {
            portOutput.close();
            portInput.close();
            this.isConnected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
