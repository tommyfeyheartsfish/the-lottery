package com.anwithayi.Server;

// import java.net.*;
// import java.io.*;

// public class ServerMain
// {
//     //initialize socket and input stream
//     private Socket          clientSocket   = null;
//     private int port;
//     private boolean running =false;

//     // constructor with port
//     public ServerMain(int port)
//     {
//         this.port = port;
//     }

//     public void startServer(){
//         running = true;
//         try(ServerSocket serverSocket = new ServerSocket(port)){
//             System.out.println("Server started on port "+ port);
//             while(running){
//                 try{
//                     clientSocket = serverSocket.accept();
//                     //create and start a new Client thread for each client connection
//                     new Thread(new ClientHandler(clientSocket)).start();
//                 }catch(IOException e){
//                     System.err.println("error connection: "+e.getMessage());
//                     e.printStackTrace();
//                 }
//             }
//         }
//             catch(IOException e){
//                 System.err.println("Could not listen on port "+port+": "+e.getMessage());
//                 e.printStackTrace();
//             }
//         }
//         public void stopServer(){
//             running = false;
//         }

    // public static void main(String args[])
    // {
    //     // DatabaseConnector.connect();
    //     int port = 5000;
    //     ServerMain server = new ServerMain(port);
    //     server.startServer();

    // }

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.websocket.server.ServerContainer;

public class ServerMain {
    /**
        * The main method of the ServerMain class.
        * It starts the server and configures the WebSocket endpoint.
        *
        * @param args the command line arguments
        * @throws Exception if an error occurs while starting the server
        */
    public static void main(String[] args) throws Exception {
        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // Enable javax.websocket configuration for the context
        ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);

        // Add WebSocket endpoint to javax.websocket layer
        wscontainer.addEndpoint(WebServerMain.class);

        try {
            server.start();
            server.join();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }
}
