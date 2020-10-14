/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import javafx.application.Platform;
import server.MessagePackage;
import server.server;

/**
 *
 * @author SANIL
 */
public class ServerListenThread implements Runnable {

    private ServerSocket serverSocket;
    private Socket socket;
    private Thread listenThread;
    private ServerMessageThread serverMessageThread;
    public static ArrayList<ClientModel> clientArrayList = new ArrayList<>();
    private DataInputStream din;

    private enum Status {
        ON, OFF
    }
    private static Status listenStatus = Status.OFF;

    public void start() {
        try {
            listenStatus = Status.ON;
            listenThread = new Thread(this);
            listenThread.start();
        } catch (Exception ex) {

        }
    }

    public void stop() {
        try {
            listenStatus = Status.OFF;
            din.close();
            socket.close();
            serverSocket.close();
            serverMessageThread.stop();
        } catch (Exception ex) {

        }
    }

    public void run() {
        try {
            // Create a server socket
            serverSocket = new ServerSocket(8000);

            //  SocketProgramming.progressTextArea.appendText("MultiThreadServer started at " + new Date() + '\n');
            while (listenStatus == Status.ON) {
                // Listen for a new connection request
                socket = serverSocket.accept();
                System.err.println(socket);
                // Display the client number
                //   SocketProgramming.progressTextArea.appendText("Connection from " + socket + " at " + new Date() + '\n');
                din = new DataInputStream(socket.getInputStream());
                String username = din.readUTF();
                ClientModel clientModel = new ClientModel(username, socket);
                int index = 0;
                String message = username + " has entered at " + new Date();
                MessagePackage messagePackage = new MessagePackage(index, message);
                Platform.runLater(()
                        -> new server().displayMsg(index, message));

                addClients(username, socket);
                // Create output stream
                serverMessageThread = new ServerMessageThread(clientModel);
//                // Create a new thread for the connection
                serverMessageThread.start();
                serverMessageThread.broadcastMsg(clientModel, messagePackage);
            }
        } catch (IOException ex) {
            System.err.println("lis= " + ex.getMessage());
//            ex.printStackTrace();
        }
    }

    public static void addClients(String username, Socket clientSocket) {
        try {
            clientArrayList.add(new ClientModel(username, clientSocket));
            Platform.runLater(()
                    -> new server().appendMember());
            // new SocketProgramming().refresh();

            // clients = FXCollections.observableArrayList(new Client("Abc"));
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static void removeClient(int index) {
        try {
            System.out.println("removeClientsize => " + clientArrayList.size());
            clientArrayList.remove(index);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

}
