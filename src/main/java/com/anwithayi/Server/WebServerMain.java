package com.anwithayi.Server;


import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/game")
public class WebServerMain {

    @OnOpen
    public void onOpen(Session session){
        System.out.println("Open Connection ...");
    }

    @OnClose
    public void onClose(Session session){
        System.out.println("Close Connection ...");
    }

    @OnMessage
    public void onMessage(String message, Session session)throws IOException {
        System.out.println("Message from the client: " + message);
        String session_id=session.getId();
        String response = processMessage(message, session_id);
        try{
        session.getBasicRemote().sendText(response);
        }catch(IOException e) {
            System.err.println("Error sending message: "+e.getMessage());
        }
    }

    private String processMessage(String message, String cl_id) {
        String response;
            RPCProcessor messageProcessor =new RPCProcessor(message, cl_id);
            response= messageProcessor.rpcProcessor();
        return response;
    }
}