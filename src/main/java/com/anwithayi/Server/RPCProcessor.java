package com.anwithayi.Server;

import java.util.Arrays;
import java.util.Set;

public class RPCProcessor {
    private String input;
    private String[] inputParts;
    private Client cl;
    private  String response=null;

    public RPCProcessor(String input, Client client){
        this.input = input;
        this.cl = client;
        inputParts = processString(input);
        System.out.println(Arrays.toString(inputParts));
    }

    public String rpcProcessor(){

        switch (inputParts[0]){
            case "checkUsername":
                response = addUser();
                break;
            case "clientOnline":
                response = getOnlineCLients();
                break;
            case "quit":
                response = disconnect();
                break;
            case "guess":
                response = guess();
                break;
            case "score":
                response = getScore();
                break;
            case "pass":
                response = pass();
                break;
            case "isGameEnded":
                response = checkGameStatus();
                break;
            case "startNewGame":
                response = startNewGame();
                break;
            case "myGameEnded":
                response = hasEnded();
                break;
            default:
                response ="Unknown RPC call: " + input;
                break;
        }
        return response;
    }

    public String  startNewGame(){
        GlobalContext.getInstance().startNewGame();
        return "game starts";
    }

    public String hasEnded(){
        Boolean allGameEnded = GlobalContext.getInstance().playerEnded(cl.getUsername(),true);
        if(allGameEnded)
        {
            return "AllEnd";
        }
        else
            return "wait";
    }
    public String addUser(){
        //if there is a third item in the array
        //if there is no second item in the array
        if(inputParts.length!=2)
        {
            return "username can't have space.";
        }
        else
        {
            String username = inputParts[1];

            if(GlobalContext.getInstance().keyFound(username))
            {
                return "Username is taken.";
            }
            else
            {
                cl.setUsername(username);
                GlobalContext.getInstance().addItem(username,cl);
                GlobalContext.getInstance().playerGuessed(username, false);
                GlobalContext.getInstance().playerEnded(cl.getUsername(),false);
                return "Username OK";
            }
        }
    }

    public String getOnlineCLients(){
        Set<String> onlineClient = GlobalContext.getInstance().getOnlineClients();
        StringBuilder sb = new StringBuilder();
        sb.append("Players online: ");
        if (onlineClient.isEmpty()) {
        sb.append("None");
        } else {
            int index = 0;
            for (String client : onlineClient) {
                sb.append(client);
                if (index < onlineClient.size() - 1) {
                    sb.append(", "); // Add a comma and space except after the last client
                }
                index++;
            }
        }
        return sb.toString();
    }

     private String disconnect(){

        GlobalContext.getInstance().removeItem(cl.getUsername());


        return "removed";
     }

     private String guess(){

            if(inputParts.length!=2)
            {
                return "space found";
            }

            else if(cl.hasGuessed())
               {
                 return "has guessed";
               }
            else if(!isInteger(inputParts[1])||Integer.valueOf(inputParts[1])<100||Integer.valueOf(inputParts[1])>1000)
            {
                return "number out of range";
            }

            else
            {
                String guess = inputParts[1];
                String response = GlobalContext.getInstance().guess(cl.getUsername(), guess);
                GlobalContext.getInstance().playerGuessed(cl.getUsername(),true);

                return "guess recorded " + response;

            }
     }


    //for fethching the current score of the client
    private String getScore(){
        return String.valueOf(cl.getScore());
    }


     private String pass(){

        GlobalContext.getInstance().playerGuessed(cl.getUsername(), true);

        cl.setHasGuessed(true);
        cl.setLastGuessedNum(null);
        cl.setLastCorrectlyGuessedNum(0);

    return "waiting for other players";
     }

     private String[] processString(String input) {
        // Split the input string by spaces
        String[] words = input.split(" ");

        // Return the array containing the words
        return words;
    }

    private String checkGameStatus(){

        return GlobalContext.getInstance().checkAllPlayersGuessed();
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true; // String can be converted to an integer
        } catch (NumberFormatException e) {
            return false; // String cannot be converted to an integer
        }
    }
}
