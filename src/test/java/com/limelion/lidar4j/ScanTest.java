package com.limelion.lidar4j;

import com.fazecast.jSerialComm.SerialPort;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public class ScanTest {

    @Test
    public void test() {

        SerialPort port = SerialPort.getCommPort("COM4");
        port.setComPortParameters(TiM510.BAUDRATE, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 50, 50);
        port.openPort(100);
        BufferedImage img;

        try (TiM510 tim510 = new TiM510(port.getInputStream(), port.getOutputStream())) {

            // Take the average of 6 scans
            ScanDataResponse[] sdr = Stream.generate(() -> {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return tim510.getScanData();
            }).limit(6).toArray(ScanDataResponse[]::new);

            img = ScanRenderer.render(Scan.parse(sdr, 60, 120, 300),
                                      1280,
                                      720,
                                      Color.RED,
                                      Color.GREEN,
                                      Color.BLUE,
                                      true,
                                      true,
                                      1.2f,
                                      180.0f);
        }

        try {
            ImageIO.write(img, "png", new File("render.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
