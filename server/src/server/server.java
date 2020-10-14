/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.ServerListenThread;

/**
 *
 * @author SANIL
 */
public class server extends Application {

    /**
     * @param args the command line arguments
     */
    public static ServerListenThread listenThread = new ServerListenThread();

    public static void main(String[] args) {
        listenThread.start();
        launch(args);
    }
    public static List<Label> messages = new ArrayList<>();
    public static List<Label> members = new ArrayList<>();
    public static ScrollPane chatScrollPane = new ScrollPane();
    public ScrollPane memberScrollPane = new ScrollPane();
    private static VBox chatBox = new VBox(5);
    private static VBox memberBox = new VBox(5);
    public static Label countLabel = new Label("Total Client: 0");
    //  private Pane root = new Pane();
    private Scene scene;

    public void start(Stage stage) {
        stage.setTitle("Server");
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPosition(0, 9 / (double) 12);
        splitPane.setDividerPosition(1, 3 / (double) 12);

//        right.setStyle("-fx-background-color: darkorange");
        chatScrollPane.getStylesheets().add(getClass().getResource("chatbox.css").toExternalForm());
        memberBox.getStylesheets().add(getClass().getResource("chatbox.css").toExternalForm());
        chatScrollPane.setPrefSize(900, 400);
        chatScrollPane.setContent(chatBox);
        chatScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        chatBox.getStyleClass().add("chatbox");
        SplitPane.setResizableWithParent(chatScrollPane, Boolean.FALSE);
        countLabel.getStyleClass().add("label_client_count");
        memberBox.getChildren().add(countLabel);
        memberScrollPane.setContent(memberBox);
        splitPane.getItems().addAll(chatScrollPane, memberBox);
        //Adding scene to the stage 
        scene = new Scene(splitPane, 1200, 400);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void appendMember() {
        //  members.add(memLbl);
        int totalClientSize = ServerListenThread.clientArrayList.size();
        countLabel.setText("Total Client: " + totalClientSize);
        Label memListLbl = ServerListenThread.clientArrayList.get(totalClientSize - 1).getLabel();
        memListLbl.setId(Integer.toString(totalClientSize));
        memListLbl.setAlignment(Pos.CENTER_LEFT);
        memListLbl.getStyleClass().add("label_clent_list");
        memberBox.getChildren().add(memListLbl);
    }

    public static void removeMember(int removeIndex) {
        try {
            countLabel.setText("Total Client: " + ServerListenThread.clientArrayList.size());
            memberBox.getChildren().remove(removeIndex + 1);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void displayMsg(int index, String msg) {
        Label msgLbl = new Label(msg);
        if (index == -1) {
            msgLbl.getStyleClass().add("label_client_left");
            messages.add(msgLbl);
            int lastMsgIndex = messages.size();
            messages.get(lastMsgIndex - 1).setAlignment(Pos.CENTER);

        } else if (index == 0) {
            msgLbl.getStyleClass().add("label_clent_in");
            messages.add(msgLbl);
            int lastMsgIndex = messages.size();
            messages.get(lastMsgIndex - 1).setAlignment(Pos.CENTER);
        } else {
            msgLbl.getStyleClass().add("label_msg");
            messages.add(msgLbl);
            int lastMsgIndex = messages.size();
            messages.get(lastMsgIndex - 1).setAlignment(Pos.CENTER_LEFT);
        }
        chatBox.getChildren().add(messages.get(messages.size() - 1));
        chatScrollPane.setVvalue(1.0);
    }

    public void stop() {
        listenThread.stop();
    }
}
