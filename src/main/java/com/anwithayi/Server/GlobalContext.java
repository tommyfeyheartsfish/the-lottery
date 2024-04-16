package com.anwithayi.Server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Semaphore;

public class GlobalContext{
        // Singleton instance
        private static volatile GlobalContext instance;
        private final Semaphore semaphore = new Semaphore(1);

        private final ConcurrentHashMap<String, Client> clientData;
        private final ConcurrentHashMap<String, Boolean> hasGuessed;
        private final ConcurrentHashMap<String, Boolean> hasReturnedEndGame;
        private final AtomicBoolean gameEnded;
        private long gameStartTime;
        private String message=null;

        private ScheduledExecutorService scheduler;

        // Method to get the singleton instance
        public static GlobalContext getInstance() {
            if (instance == null) {
                synchronized (GlobalContext.class) {
                    if (instance == null) {
                        instance = new GlobalContext();
                    }
                }
            }
            return instance;
        }

        // Private constructor
        private GlobalContext() {
            clientData = new ConcurrentHashMap<>();
            hasGuessed = new ConcurrentHashMap<>();
            hasReturnedEndGame = new ConcurrentHashMap<>();
            gameEnded = new AtomicBoolean(false);
            scheduler = Executors.newScheduledThreadPool(1);
            startGame();
        }

        public void startGame() {
            try {
                semaphore.acquire();
                gameStartTime = System.currentTimeMillis();
                gameEnded.set(false);
                // Schedule the game to end after 10 minutes
                scheduler.schedule(this::endGame, 2, TimeUnit.MINUTES);
                GameLogic.getInstance();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                semaphore.release();
            }
        }

        public void startNewGame() {

                resetGameState(); // Reset the game state for a new game
                startGame();      // Start the new game

        }     // Start the new game


        private void resetGameState() {
            try {
                semaphore.acquire();
                for (String key : hasGuessed.keySet()) {
                    hasGuessed.put(key, false);
                }
                for (String key : hasReturnedEndGame.keySet()) {
                    hasReturnedEndGame.put(key, false);
                }
                GameLogic.getInstance().setNewAnwer();
                clientData.forEach((key, client) -> {
                    client.setLastCorrectlyGuessedNum(0); // Reset the last correctly guessed number
                    client.setLastGuessedNum(null);
                client.setHasGuessed(false);
            });
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }finally{
            semaphore.release();
        }
        }

        public String guess(String clientId, String guess) {
            // Perform guessing logic, e.g., determine the number of correctly guessed digits
            int correctlyGuessedDigits = GameLogic.getInstance().correctDigits(guess); // Stub for actual check logic
            int pointGain = GameLogic.getInstance().pointGain(correctlyGuessedDigits);

            Client client = clientData.get(clientId);
            if (client != null) {
                client.setScore(pointGain);
                client.setLastGuessedNum(String.valueOf(guess));
                client.setLastCorrectlyGuessedNum(correctlyGuessedDigits);
                client.setHasGuessed(true);
                // playerGuessed(clientId);
            }
            return GameLogic.getInstance().getAnswer() +" " + correctlyGuessedDigits + " " + pointGain;
        }

        public void playerGuessed(String key, boolean played) {
            try {
                semaphore.acquire();
                hasGuessed.put(key, played);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                semaphore.release();
            }
        }

        public boolean playerEnded(String key, boolean hasEnded) {
            boolean allPlayersEnded = false;
            try {
                semaphore.acquire();
                hasReturnedEndGame.put(key, hasEnded);
                allPlayersEnded = checkAllPlayersEnded();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                semaphore.release();
            }
            return allPlayersEnded;
        }

        private boolean checkAllPlayersEnded() {
            if (hasReturnedEndGame.values().stream().allMatch(Boolean::booleanValue) ) {
               return true;
            }
            else
                return false;
        }
        public String checkAllPlayersGuessed() {
            if (hasGuessed.values().stream().allMatch(Boolean::booleanValue) ) {
               return endGame();
            }
            else
                return "waiting for other players";

        }

        public String endGame() {

            try {
                semaphore.acquire();
                    gameEnded.set(true);
                    message = printScore();

                return message;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Error: Interrupted during end game operation";
            } finally {
                semaphore.release();
            }
        }

         //for printing the scores after the game ended
        private String printScore(){
            List<Map.Entry<String, Client>> rankedClients = GlobalContext.getInstance().rankClients();
            StringBuilder sb=new StringBuilder();
            sb.append("Rankings:");
            for (int i = 0; i < rankedClients.size(); i++)
            {
                String username = rankedClients.get(i).getKey();
                int score = rankedClients.get(i).getValue().getScore();
                sb.append((i + 1) + ". " + username + " - Score: " + score+"     ");
            }
            return sb.toString();
        }

        public List<Map.Entry<String, Client>> rankClients() {
        // Sort clients by score in descending order
        return clientData.entrySet().stream()
                .sorted(Map.Entry.<String, Client>comparingByValue(Comparator.comparingInt(Client::getScore)).reversed())
                .collect(Collectors.toList());
        }

        public List<String> getPlayersWhoGuessed() {
            return clientData.values().stream()
                    .filter(Client::hasGuessed) // Filter clients who have guessed
                    .map(client -> client.getUsername() + " - Correctly Guessed Digits: " + client.getLastCorrectlyGuessedNum())
                    .collect(Collectors.toList()); // Collect results into a list
        }

        public boolean isGameEnded() {
            return gameEnded.get();
        }

        public void addItem(String key, Client value) {
                 try {
                    semaphore.acquire();
                    clientData.put(key, value);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Handle the InterruptedException
                } finally {
                    semaphore.release();
                }
            }

        public void updateItem(String key, Client newValue) {
            try {
                semaphore.acquire();
                clientData.put(key, newValue);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle the InterruptedException
            } finally {
                semaphore.release();
            }
        }

        public void removeItem(String key) {
            try {
                semaphore.acquire();
                clientData.remove(key);
                hasGuessed.remove(key);
                hasReturnedEndGame.remove(key);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                semaphore.release();
            }
        }

        public void clearItems() {
            clientData.clear();
        }

        public boolean keyFound(String key){
            boolean found;
            try {
                semaphore.acquire();
                found = clientData.containsKey(key);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                found = false;
            } finally {
                semaphore.release();
            }
            return found;
        }

        public int getOnlineClientCount() {
            return clientData.size();
        }

        public Set<String> getOnlineClients() {
            Set<String> onlineClients = null;
            try {
                semaphore.acquire();
                onlineClients = new HashSet<>(clientData.keySet());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                onlineClients = Collections.emptySet();
            } finally {
                semaphore.release();
            }
            return onlineClients;
        }
    }

