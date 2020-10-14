/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;
import javafx.application.Platform;
import static model.ServerListenThread.clientArrayList;
import server.MessagePackage;
import server.server;

/**
 *
 * @author SANIL
 */
public class ServerMessageThread implements Runnable {

    private DataInputStream din;
    private Thread thread;
    private ClientModel clientModel;

    private enum Status {
        ON, OFF
    }
    private static Status messageStatus = Status.OFF;

    public ServerMessageThread(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

    public void start() {
        try {
            messageStatus = Status.ON;
            thread = new Thread(this);
            thread.start();
            //   new Thread(() -> listen()).start();
        } catch (Exception ex) {
            System.err.println("ServerMessageThread.start()= " + ex.getMessage());
        }
    }

    public void run() {
        MessagePackage messagePackage;

        try {
            din = new DataInputStream(clientModel.getCientSocket().getInputStream());

            // Continuously serve the client
            while (messageStatus == Status.ON) {
                String clientMsg = din.readUTF();
                String senderInfo = "From: " + clientModel.getName() + " \n";
                int index = 1;
                messagePackage = new MessagePackage(index, senderInfo + clientMsg);
                System.err.println(messagePackage);
                broadcastMsg(clientModel, messagePackage);
                // Send text back to the clients
                //     server.sendToAll(string);
                // Add chat to the server jta
                Platform.runLater(()
                        -> new server().displayMsg(index, senderInfo + clientMsg));
                //    SocketProgramming.progressTextArea.appendText(clientMsg + '\n');
            }
        } catch (Exception ex) {
            handleClientLeft();
            //       serverStatus = Status.OFF;
        }
    }

    public void handleClientLeft() {
        try {
            int removeIndex = indexOfRemove();

            MessagePackage messagePackage;
            int index = -1;
            String message = clientModel.getName() + " has left at " + new Date();
            messagePackage = new MessagePackage(index, message);
            broadcastMsg(clientModel, messagePackage);
            ServerListenThread.removeClient(removeIndex);
            Platform.runLater(()
                    -> new server().removeMember(removeIndex));
            Platform.runLater(()
                    -> new server().displayMsg(index, message));
        } catch (Exception ex) {

        }
    }

    public int indexOfRemove() {
        int totalClient = ServerListenThread.clientArrayList.size();
        int removeIndex = -1;
        for (int i = 0; i <= totalClient; i++) {
            ClientModel cm = ServerListenThread.clientArrayList.get(i);
            if (cm.getCientSocket() == clientModel.getCientSocket()) {
                removeIndex = i;
                return removeIndex;
            }
        }
        return removeIndex;
    }

    public void stop() {
        try {
            din.close();
            messageStatus = Status.OFF;
        } catch (Exception ex) {

        }
    }

    public void broadcastMsg(ClientModel clientModel, MessagePackage messagePackage) {
        try {
            if (clientArrayList.size() > 0) {
                for (ClientModel cm : clientArrayList) {
                    if (cm.getCientSocket() == clientModel.getCientSocket()) {
                        System.out.println("equal");
                    } else {
                        DataOutputStream dataOut = new DataOutputStream(cm.getCientSocket().getOutputStream());
                        dataOut.writeUTF(messagePackage.toString());
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}
