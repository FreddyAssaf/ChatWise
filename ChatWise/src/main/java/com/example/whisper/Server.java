package com.example.whisper;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Server extends Application {

    private Set<Socket> clientSockets = new HashSet<>();
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    // MongoDB connection details
    private static final String MONGO_CONNECTION_STRING = "mongodb://127.0.0.1:27017";
    private static final String DATABASE_NAME = "chat";
    private static final String COLLECTION_NAME = "Messages";
    static TextArea logTextArea = new TextArea();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            mongoClient = MongoClients.create(MONGO_CONNECTION_STRING);

            // Access the database
            database = mongoClient.getDatabase(DATABASE_NAME);

            // Access the collection (similar to a table in a relational database)
            collection = database.getCollection(COLLECTION_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Text text = new Text("Server");
        text.setFill(Color.GREY);
        text.setFont(Font.font(30));


        logTextArea.setEditable(false);
        logTextArea.setStyle("-fx-text-fill: green;");
        VBox vBox = new VBox(text, logTextArea);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox, 400, 300);

        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            // Close all client sockets when the server window is closed
            closeAllClients();
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setResizable(false);
        primaryStage.show();

        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(5555);
                Text msg = new Text("Server is running...");
                log(msg.getText(), primaryStage);

                Document query = new Document("fieldName", "fieldValue");
                processDocuments(collection);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    clientSockets.add(clientSocket);
                    log("Client connected: " + clientSocket, primaryStage);

                    new Thread(() -> handleClient(clientSocket, logTextArea, primaryStage)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleClient(Socket clientSocket, TextArea logTextArea, Stage primaryStage) {
        try (Scanner scanner = new Scanner(clientSocket.getInputStream())) {
            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                log("Received message from client: " + message, primaryStage);

                // Create a document with the current date
                Document document = new Document("text", message)
                        .append("timestamp", new Date());

                // Insert the document into the collection
                collection.insertOne(document);

                System.out.println("Document inserted successfully.");
                broadcast(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(String message) {
        for (Socket socket : clientSockets) {
            try {
                socket.getOutputStream().write((message + "\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeAllClients() {
        for (Socket socket : clientSockets) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void log(String message, Stage primaryStage) {
        Platform.runLater(() -> {
            // Append log messages in the UI thread
            TextArea logTextArea = (TextArea) primaryStage.getScene().getRoot().getChildrenUnmodifiable().get(1);
            logTextArea.appendText(message + "\n");
        });
    }

    @Override
    public void stop() throws Exception {
        // Close the MongoClient when the application exits
        if (mongoClient != null) {
            mongoClient.close();
        }
        super.stop();
    }
    private static void processDocuments(MongoCollection<Document> collection) {
        try {
            // Execute the query and retrieve all results (empty Document as query)
            for (Document document : collection.find()) {
                // Extract "text" and "timestamp" fields from the document
                String text = document.getString("text");
                Date timestamp = document.getDate("timestamp");

                // Now you can use the variables as needed
                System.out.println("Text: " + text);
                System.out.println("Timestamp: " + timestamp);

                logTextArea.appendText(text+" "+timestamp+"\n");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
