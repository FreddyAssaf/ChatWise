package com.example.whisper;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

class ClientNameGenerator {
    static String generateRandomClientName() {
        return "Client-" + UUID.randomUUID().toString().substring(0, 8);
    }
}

public class Client extends Application {
    TextField messageField = new TextField();
    private PrintWriter writer;
    private String clientName;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        clientName = ClientNameGenerator.generateRandomClientName();

        Text text = new Text("CHAT ROOM");
        text.setFont(Font.font("Arial", FontWeight.BOLD,30));
        text.setFill(Color.BLUE);


        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);

        Button send = new Button("Send");
        HBox hBox = new HBox(messageField,send);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);

        VBox vbox = new VBox(text,chatArea, hBox);
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 400, 300);

        primaryStage.setTitle(clientName);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        try {
            Socket socket = new Socket("localhost", 5555);
            writer = new PrintWriter(socket.getOutputStream(), true);
            new Thread(() -> handleServerMessages(socket, chatArea)).start();

            messageField.setOnAction(e -> sendMessage(messageField.getText()));
            send.setOnAction(e -> sendMessage(messageField.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleServerMessages(Socket socket, TextArea chatArea) {
        try {
            Scanner scanner = new Scanner(socket.getInputStream());
            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                chatArea.appendText(message + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        // Send message to the server
        if (writer != null) {
            writer.println(clientName + ": " + message);
        }
        messageField.setText("");
    }
}
