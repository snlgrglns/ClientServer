/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 *
 * @author SANIL
 */
public class ClientThread implements Runnable {

    private Socket clientSocket;
    private Thread clientThread;
    private static DataOutputStream dataOut;
    private DataInputStream dataIn;

    private enum Status {
        ON, OFF
    }
    private static Status clientStatus = Status.OFF;

    public void start() {
        try {
            clientStatus = Status.ON;
            clientSocket = new Socket("localhost", 8000);

            dataIn = new DataInputStream(clientSocket.getInputStream());

            // Create an output stream to send data to the server
            dataOut = new DataOutputStream(clientSocket.getOutputStream());
            clientThread = new Thread(this);
            clientThread.start();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void run() {
        try {
            while (clientStatus == Status.ON) {
                // Get message
                String msgPackage = dataIn.readUTF();
                System.out.println(msgPackage);
                String lines[] = msgPackage.split(Pattern.quote("[messageSplitToken]"));
                System.err.println(lines[0]);
                System.err.println(lines[1]);
                Platform.runLater(()
                        -> new Client().displayMsg(Integer.parseInt(lines[0]), lines[1]));
                System.out.println("recceive");
                // Display to the text area
                //ta.appendText(text + '\n');
            }
        } catch (IOException ex) {
//            ex.printStackTrace();
        //    throw new RuntimeException(ex.getMessage());
            new Client().popUpAlert(Alert.AlertType.ERROR, "Error!!!", ex.getMessage());
        }
    }

    public static void send(String msg, boolean isMessage) {
        try {
            // Send the text to the server
            dataOut.writeUTF(msg);
            if (isMessage) {
                Platform.runLater(()
                        -> new Client().displayMsg(2, msg));
            }
            System.err.println(msg);
            // Clear jtf
            //tf.setText("");
        } catch (IOException ex) {
//            System.err.println(ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void stop() {
        try {
            clientSocket.close();
            clientStatus = Status.OFF;
        } catch (Exception ex) {

        }
    }

}
