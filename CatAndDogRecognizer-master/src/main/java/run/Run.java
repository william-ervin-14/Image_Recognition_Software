package run;

import org.apache.commons.io.FileUtils;


import ui.IR;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.zip.Adler32;

public class Run {

    private static final String MODEL_URL = "https://dl.dropboxusercontent.com/s/djmh91tk1bca4hz/RunEpoch_class_2_soft_10_32_1800.zip?dl=0";

    public static void main(String[] args) throws Exception {
        downloadModelForFirstTime();

        JFrame mainFrame = new JFrame();
        IR ir = new IR();
        Executors.newCachedThreadPool().submit(() -> {
            try {
                ir.initIR();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                mainFrame.dispose();
            }
        });

    }

    private static void downloadModelForFirstTime() throws IOException {
        JFrame mainFrame = new JFrame();
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        File model = new File("resources/model.zip");
        if (!model.exists() || FileUtils.checksum(model, new Adler32()).getValue() != 3082129141l) {
            model.delete();
            URL modelURL = new URL(MODEL_URL);

            try {
                FileUtils.copyURLToFile(modelURL, model);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Failed to download model");
                throw new RuntimeException(e);
            } finally {
               
                mainFrame.dispose();
            }

        }
    }
}
