package com.anwithayi.Server;


import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.OnError;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint(value = "/src/main/java/com/anwithayi/Server/WebServerMain.java")
public class WebServerMain {
    private Client cl;

    @OnOpen
    public void onOpen(Session session){
        System.out.println("Open Connection ...");
    }

    @OnClose
    public void onClose(Session session){
        System.out.println("Close Connection ...");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Handle error here
        System.err.println("An error occurred: " + throwable.getMessage());
    }
    @OnMessage
    public void onMessage(String message, Session session)throws IOException {
        System.out.println("Message from the client: " + message);

        String response = processMessage(message);
        System.out.println("Message send back to the client:"+ response);
        try{
        session.getBasicRemote().sendText(response);
        }catch(IOException e) {
            System.err.println("Error sending message: "+e.getMessage());
        }
    }
    //TODO:make sure the client sent the message is the same client
    private String processMessage(String message) {
        String response;

        if(message.startsWith("checkUsername")){
            //create new user
            this.cl= new Client();
        }
        else if(cl==null)
        {
            response="error: player doesn't exsit";
        }
        RPCProcessor messageProcessor =new RPCProcessor(cl, message);
        response= messageProcessor.rpcProcessor();
        return response;
    }
}
