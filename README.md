# ChatWise-Client/Server ChatRoom

ChatWise is a collaborative chat application project that seamlessly integrates JavaFX, socket programming, and multithreading. The client-side, implemented in Java, features an engaging graphical user interface allowing users to exchange messages effortlessly. Each client is assigned a unique name generated at runtime for a personalized touch.

## Server

### Setup

1. Ensure you have MongoDB installed and running on your local machine.

2. Clone the repository:

    ```bash
    git clone https://github.com/FreddyAssaf/ChatWise.git
    cd ChatWise
    ```

3. Open the `Server` project in your preferred Java IDE.

4. Update MongoDB Connection Details:

    - Open the `Server.java` file.
    
    - Modify the `MONGO_CONNECTION_STRING`, `DATABASE_NAME`, and `COLLECTION_NAME` variables according to your MongoDB setup.

5. Run the `Server` class to start the server.

### Features

- The server, implemented with JavaFX, displays server activities and integrates querying capabilities to retrieve and process messages from MongoDB.

- Messages received from clients are logged into a MongoDB database, providing a robust and persistent storage solution.

- The use of multithreading ensures simultaneous connections are handled seamlessly, contributing to the real-time nature of the chat experience.

- The server logs incoming messages to the console.

### Dependencies

- JavaFX
- MongoDB Java Driver

## Client

### Setup

1. Clone the repository:

    ```bash
    git clone https://github.com/FreddyAssaf/ChatWise.git
    cd ChatWise
    ```

2. Open the `Client` project in your preferred Java IDE.

3. Run the `Client` class to start the chat client.

### Features

- Each client connects to the server on localhost:5555.

- Clients can send messages to the server, and the server broadcasts messages to all connected clients.

- The client's name is generated randomly using UUID.

### Dependencies

- JavaFX

### Usage

1. **Client Interaction:**
   - Launch the client application.
   - Enter your messages in the provided text field.
   - Press "Send" or hit Enter to send your message to the server.

2. **Server Interaction:**
   - Launch the server application.
   - Observe server activities displayed in the JavaFX-based user interface.
   - Messages from clients are logged to the console.

3. **Screenshots:**
   - View application screenshots in the [Screenshots-ChatWise](Screenshots-ChatWise/) folder.

## Screenshots

For a visual preview of ChatWise, please refer to the [Screenshots-ChatWise](Screenshots-ChatWise/) folder.

## Contribution

Thank you for considering contributing to ChatWise! Your contributions are highly appreciated. To contribute:

1. Fork the repository.
2. Clone your forked repository to your local machine.
3. Make changes, fix bugs, or add new features.
4. Commit your changes and push to your fork.
5. Open a pull request from your fork to the main repository.

Happy coding! 🚀
