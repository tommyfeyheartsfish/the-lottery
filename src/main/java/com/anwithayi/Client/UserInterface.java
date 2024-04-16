package com.anwithayi.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class UserInterface{
    private BufferedReader stdIn;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private ClientContext newClient;

    public UserInterface(Socket socket) throws IOException {
        this.socket = socket;
        this.stdIn = new BufferedReader(new InputStreamReader(System.in));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.newClient =new ClientContext();
    }

    public void start() {

        try {

            printWelcomeMessage();
            String username = promptForUsername();
            out.println("clientOnline"); // Send the username to the server

            // Handle server response
            String serverResponse = in.readLine();
            System.out.println("Welcome "+username+"!");
            System.out.println(serverResponse);
            System.out.println();

            String command=null;
            do{
                command = promptForCommand();

            }while(!command.equalsIgnoreCase("quit"));

        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String promptForUsername() throws IOException {
        String username = null;
        boolean isValid = false;

        while (!isValid) {
            System.out.print("Enter your username (not a command): ");
            username = stdIn.readLine();

            //Check if the username is a reserved command.

            if (username.equalsIgnoreCase("quit") || username.equalsIgnoreCase("exit")) {
                System.out.println("Username cannot be a command. Please try again.");
                continue;
            }

            // Send the username to the server for validation
            out.println("checkUsername " + username); // Assuming the server expects a specific format for checking usernames
            String response = in.readLine();

            if ("Username is taken.".equals(response)) {
                System.out.println("Username is already taken. Please try again.");
            } else if ("Username OK".equals(response)) {
                isValid = true; // Exit the loop if the server indicates the username is not taken
                newClient.setUsername(username);//set the username in the client context
            } else {
                // Handle unexpected server response
                System.out.println("Unexpected server response: " + response);
            }
        }
        return username;
    }

    private String promptForCommand() throws IOException{
        String command=null;
        boolean commandVaild=false;

        while(!commandVaild)
        {   if(newClient.getStatus())
            {
                System.out.print("Choose your next command: cheatsheet/pass/check score/quit ");
            }
            else{
                System.out.print("Choose your next command: cheatsheet/guess [3-digit-number]/pass/check score/quit ");
            }
            command=stdIn.readLine();
            String[] commandParts=processString(command);
            switch(commandParts[0]){
                case "cheatsheet":
                    printCommandLineHelp();
                    commandVaild=true;
                    break;
                case "guess":
                    commandVaild = guess(command);
                    break;
                case "pass":
                    commandVaild=pass(command);
                    break;
                case "check":
                    commandVaild=score(command);
                    break;
                case "quit":
                    quit(command);
                    commandVaild=true;
                    break;
                default:
                    System.out.println("Invalid command.");;
                    break;
            }
        }
        return command;
    }
    private String[] processString(String s) {
        // Split the input string by spaces
        String[] words = s.split(" ");

        // Return the array containing the words
        return words;
    }
    private boolean pass(String cli)throws IOException{
        boolean cliValid = false;
        out.println(cli);
        String response = in.readLine();
        cliValid = true;
        //the guesses made by other players
        System.out.println(response);
        //check if the game is end
        //restart a game
        GameEnd();

        return cliValid;
    }

    private boolean score(String cli)throws IOException{
        boolean cliValid =false;
        out.println("score");
        String response = in.readLine();
        newClient.setScore(Integer.valueOf(response));
        cliValid = true;
        System.out.println(response);
        return cliValid;
    }


    private boolean guess(String cli)throws IOException{
        boolean guessValid=false;

        out.println(cli);
        String response = in.readLine();
        String responses[] = processString(response);
        if ("space found".equals(response)) {
            System.out.println();
            System.out.println("more than one number detected.");
        }
        else if("number".equals(responses[0])&&"out".equals(responses[1])){
                System.out.println();
                System.out.println("please try again with a 3-digit-number.");
            }
        else if("has guessed".equals(response)){
            System.out.println();
            System.out.println("You have made the guessed in this round already.");
        }
        else if ("guess".equals(responses[0])&&"recorded".equals(responses[1])) {
            guessValid = true;
            String answer = responses[2];
            int NumOfCorrectGuessed = Integer.valueOf(responses[3]);
            int score = Integer.valueOf(responses[4]);
            newClient.setScore(score);
            newClient.setLastCorrectlyGuessedNum(NumOfCorrectGuessed);
            newClient.setStatus(guessValid);
            System.out.println();
            System.out.println("You guessed " + NumOfCorrectGuessed + " numbers correctly!\n" +
                                "The answer is "+ answer + ".\n" +
                                "You get "+score+ " points this round!\n\n");
        } else {
            // Handle unexpected server response
            System.out.println("Unexpected server response for guess: " + response);
        }

        if(guessValid){
            GameEnd();

            return guessValid;
        }
        else
            return guessValid;
    }

    //after guess are made, check if other clients has made the guess/ the game has ended. if so, print out the message, if not, wait.

    public void startNewGame()throws IOException
        {
            out.println("myGameEnded");
            String reply = in.readLine();
            if(reply.equals("AllEnd"))
            {
                out.println("startNewGame");
                System.out.println(in.readLine());
            }
            else if(reply.equals("wait"))
            {
                System.out.println("waiting");
            }else {
                // Handle unexpected server response
                System.out.println("Unexpected server response for start new game: " + reply);
            }
        }
    private void GameEnd()throws IOException{
            boolean gameHasEnd=false;

            while(!gameHasEnd)
            {
                out.println("isGameEnded");
                String ranking = in.readLine();
                if (ranking == null) {
                    // The connection might have been closed; handle appropriately
                    System.err.println("Connection closed by the server.");
                    break; // Exit the loop or perform additional cleanup
                }
                if(ranking.startsWith("Ranki"))
                {
                    System.out.println(ranking);
                    System.out.println("This round ends.");
                    gameHasEnd=true;
                    startNewGame();
                    newClient.setStatus(false);
                    System.out.println();


                }
                else
                {
                    //TEST
                    System.out.print("waiting---");
                    try {
                        Thread.sleep(1000); // Sleep for 1 second before checking again
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restore the interrupted status
                        System.err.println("Interrupted while waiting to check game end status");
                    }
                }
                }

            }


    private void quit(String cli)throws IOException{
        out.println("quit");
        String response = in.readLine();
        if ("removed".equals(response)) {
            System.out.println();
            System.out.println("goodbye "+newClient.getUsername());
        }
    }

    public void printWelcomeMessage() {
        String welcomeMessage =
            "Welcome!\n\nThis is a simple multiplayer number-guessing game.\n" +
            "Each player online will have one chance to guess a three-digit number in each round.\n" +
            "The player who guesses the most digits correctly will be the winner of the round.\n\n" +
            "Let's play!";
        System.out.println();
        System.out.println("--- -- ---- ---- --- -- --- --- ---- -- -- -- -- -- - -- --- --- ----");
        System.out.println();
        System.out.println(welcomeMessage);
        System.out.println();
        System.out.println("-- ---- --- -- -- - - - ---- ---- ---- -- --- ------ ---- --- -------");
    }

    public void printCommandLineHelp(){
        String commandLine=
            "guess [3-digit-number]   make a guess on a number, it will return the amount of digits you correctly guessed and how much your just scored \n\n"+
            "pass                     pass this round\n\n"+
            "check score                    print your current score\n\n"+
            "quit                     disconnect youself from the server\n\n";
            System.out.println();
            System.out.println("--- -- ---- ---- --- -- --- --- ---- -- -- -- -- -- - -- --- --- ----");
            System.out.println();
            System.out.println(commandLine);
            System.out.println();
            System.out.println("-- ---- --- -- -- - - - ---- ---- ---- -- --- ------ ---- --- -------");
    }
}


