package com.anwithayi.Server;

import java.net.*;
import java.io.*;

public class ServerMain
{
    //initialize socket and input stream
    private Socket          clientSocket   = null;
    private int port;
    private boolean running =false;

    // constructor with port
    public ServerMain(int port)
    {
        this.port = port;
    }

    public void startServer(){
        running = true;
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server started on port "+ port);
            while(running){
                try{
                    clientSocket = serverSocket.accept();
                    //create and start a new Client thread for each client connection
                    new Thread(new ClientHandler(clientSocket)).start();
                }catch(IOException e){
                    System.err.println("error connection: "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
            catch(IOException e){
                System.err.println("Could not listen on port "+port+": "+e.getMessage());
                e.printStackTrace();
            }
        }
        public void stopServer(){
            running = false;
        }

    public static void main(String args[])
    {
        DatabaseConnector.connect();
        int port = 5000;
        ServerMain server = new ServerMain(port);
        server.startServer();

    }
}
