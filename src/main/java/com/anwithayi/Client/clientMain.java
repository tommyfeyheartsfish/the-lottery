package com.anwithayi.Client;

import java.io.IOException;
import java.net.Socket;

public class clientMain {
    public static void main(String[] args) {
        // Server details
        String serverAddress = "localhost"; // or actual server IP
        int serverPort = 5000; // or actual server port

        try {
            // Establish connection to the server
            Socket socket = new Socket(serverAddress, serverPort);
            System.out.println("Connected to the server at " + serverAddress + ":" + serverPort);

            // Initialize the UserInterface with the connected socket
            UserInterface userInterface = new UserInterface(socket);

            // Start the interaction with the server through the user interface
            userInterface.start();
            System.out.println();
            System.out.println("You are disconnected from the server.");
        } catch (IOException e) {
            System.err.println("Error connercting to the server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
