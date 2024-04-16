package com.anwithayi.Server;

    import java.io.BufferedReader;
    import java.io.InputStreamReader;
    import java.io.PrintWriter;
    import java.net.Socket;
    import java.io.IOException;

    public class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private Client cl;


        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            this.cl = new Client();
            cl.setWriter(out);
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String response = processMessage(inputLine);
                    out.println(response);
                }
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port or listening for a connection");
                System.out.println(e.getMessage());
            } finally {
                try {
                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String processMessage(String message) {
            String response;

                RPCProcessor messagProcessor =new RPCProcessor(message,cl);
                // Process the message from the client
                response= messagProcessor.rpcProcessor();

            // }
            return response;
        }
        private void disconnectClient() {
            // 从GlobalContext中移除客户端
            GlobalContext.getInstance().removeItem(cl.getUsername());
            // 关闭Socket连接
            try {
                clientSocket.close();
            } catch (IOException e) {
                // Log exception or handle it as needed
            }
        }
    }


