/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author SANIL
 */
public class Client extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    public static List<Label> messages = new ArrayList<>();
    public static ScrollPane scrollPane = new ScrollPane();
    private static VBox chatBox = new VBox(5);
    //  private Pane root = new Pane();
    private Scene scene;
    
    public void start(Stage stage) {
        //Setting title to the Stage 
        stage.setTitle("Client Register");
        TextField username = new TextField();
        Label userNameLabel = new Label("Username");
        Text errText = new Text();
        Button submitBtn = new Button("Submit");
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(30));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(userNameLabel, 0, 0);
        gridPane.add(username, 1, 0);
        gridPane.add(errText, 2, 0);
        gridPane.add(submitBtn, 1, 1);
        submitBtn.setOnAction(e -> submit(stage, username.getText()));
        //Adding scene to the stage 
        Scene scene = new Scene(gridPane);
        stage.setScene(scene);

        //Displaying the contents of the stage 
        stage.show();
    }
    
    public static ClientThread clientThread;
    private TextField msgField = new TextField();
    
    public void submit(Stage stage, String username) {
        try {
            if (username.trim().isEmpty()) {
                throw new RuntimeException("Username cannot be empty.");
            }
            scrollPane.getStylesheets().add(getClass().getResource("chatbox.css").toExternalForm());
            scrollPane.setPrefSize(610, 475);
            scrollPane.setContent(chatBox);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            chatBox.getStyleClass().add("chatbox");
            clientThread = new ClientThread();
            clientThread.start();
            send(username, false);
            stage.hide();
            Stage newStage = new Stage();
            newStage.setResizable(false);
            
            VBox vBox = new VBox();
            newStage.setTitle(username);
            
            msgField.setPrefSize(450, 50);
            Button sendBtn = new Button("Submit");
            sendBtn.setPrefSize(150, 50);
            GridPane gridPane = new GridPane();
            gridPane.setAlignment(Pos.CENTER);
            gridPane.add(msgField, 0, 0);
            gridPane.add(sendBtn, 1, 0);
            msgField.setOnAction(e -> send(msgField.getText(), true));
            sendBtn.setOnAction(e -> send(msgField.getText(), true));
            vBox.getChildren().addAll(scrollPane, gridPane);
            //Adding scene to the stage 
            scene = new Scene(vBox);
            newStage.setScene(scene);
            //Displaying the contents of the stage 
            newStage.show();
        } catch (Exception ex) {
            popUpAlert(Alert.AlertType.ERROR, "Error!!!", ex.getMessage());
//            System.out.println(ex.getMessage());
        }
    }
    
    public void send(String msg, boolean isMessage) {
        try {
            if (!msg.trim().isEmpty()) {
                clientThread.send(msg.trim(), isMessage);
                msgField.setText("");
            }
        } catch (Exception ex) {
            popUpAlert(Alert.AlertType.ERROR, "Error!!!", ex.getMessage());
//            System.out.println(ex.getMessage());
        }
    }
    
    public static void displayMsg(int index, String msg) {
        Label msgLbl = new Label(msg);
        HBox hbox = new HBox();
        hbox.setPrefWidth(500);
        msgLbl.setPrefWidth(Control.USE_COMPUTED_SIZE);
        if (index == -1) {
            msgLbl.getStyleClass().add("label_client_left");
            messages.add(msgLbl);
            int lastMsgIndex = messages.size();

            //     chatBox.getStyleClass().add("label_announce");
            messages.get(lastMsgIndex - 1).setAlignment(Pos.CENTER);
            hbox.setAlignment(Pos.CENTER);
            
        } else if (index == 0) {
            msgLbl.getStyleClass().add("label_clent_in");
            messages.add(msgLbl);
            int lastMsgIndex = messages.size();

            //     chatBox.getStyleClass().add("label_announce");
            messages.get(lastMsgIndex - 1).setAlignment(Pos.CENTER);
            hbox.setAlignment(Pos.CENTER);
            
        } else if (index == 1) {
            msgLbl.getStyleClass().add("label_msg_recv");
            messages.add(msgLbl);
            int lastMsgIndex = messages.size();
            messages.get(lastMsgIndex - 1).setAlignment(Pos.CENTER_LEFT);
            hbox.setAlignment(Pos.CENTER_LEFT);
            
        } else {
            msgLbl.getStyleClass().add("label_msg_send");
            messages.add(msgLbl);
            int lastMsgIndex = messages.size();
            messages.get(lastMsgIndex - 1).setAlignment(Pos.CENTER_RIGHT);
            hbox.setAlignment(Pos.CENTER_RIGHT);
            
        }
        hbox.getChildren().add(messages.get(messages.size() - 1));
        chatBox.getChildren().add(hbox);
        scrollPane.setVvalue(1.0);
    }
    
    public void stop() {
        clientThread.stop();
    }
    
    Alert alert;

    /*
     * This method shows the alert box which displays aleert type, title and message received on the parameter 
     */
    public void popUpAlert(Alert.AlertType alertType, String title, String msg) {
        alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(msg);
        Optional<ButtonType> box = alert.showAndWait();
    }
}
