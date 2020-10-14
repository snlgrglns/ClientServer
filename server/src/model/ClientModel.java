/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.net.Socket;
import javafx.scene.control.Label;

/**
 *
 * @author SANIL
 */
public class ClientModel {

    private Socket clientSocket;
    private String name;
    private Label label;
    //private ArrayList<ClientModel> clintList;

    public ClientModel() {
    }

    public ClientModel(String name, Socket socket) {
        this.name = name;
        this.clientSocket = socket;
        this.label = new Label(name);
    }

    public String getName() {
        return name;
    }

    public Label getLabel() {
        return label;
    }

    public void setName(String name, Socket socket) {
        this.name = name;
        clientSocket = socket;
    }

    public Socket getCientSocket() {
        return clientSocket;
    }

    public void setCientSocket(Socket socket) {
        this.clientSocket = socket;
    }
}
