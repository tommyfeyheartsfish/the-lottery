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
        System.out.println(response);
        try{
        session.getBasicRemote().sendText(response);
        }catch(IOException e) {
            System.err.println("Error sending message: "+e.getMessage());
        }
    }

    private String processMessage(String message) {
        String response;
        //test
        if(message.equals("test")){
            response = "test success!";
        }
        else
            {RPCProcessor messageProcessor =new RPCProcessor(message);
            response= messageProcessor.rpcProcessor();
            //test
            System.out.println(response);
            }
        return response;
    }
}
