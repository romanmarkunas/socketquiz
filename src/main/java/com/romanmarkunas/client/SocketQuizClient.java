package com.romanmarkunas.client;

import java.io.*;
import java.net.Socket;

class SocketQuizClient {

    private static final String serverErr = "Error. Launching new game...";
    private static final String connectionErr = "Unable to connect to server. Reconnecting...";

    private BufferedReader consoleIn;

    private PrintWriter out;
    private BufferedReader in;


    SocketQuizClient() {

        consoleIn = new BufferedReader(new InputStreamReader(System.in));
    }


    void start() {

        obtainConnection();

        while (true) {

            getNewQuiz();
            guessWord();
        }
    }


    private void obtainConnection() {

        boolean obtained = false;

        while (!obtained) {

            try {
                Socket socket = new Socket("localhost", 4444);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                obtained = true;
            }
            catch (IOException e) {

                System.out.println(connectionErr);

                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException exc) {}
            }
        }
    }

    private void getNewQuiz() {

        boolean success = false;

        while (!success) {

            String[] tokens = getServerResponse("new").split(":");

            if ("new".equals(tokens[0]) && tokens.length == 2) {

                System.out.println("What is: " + tokens[1] + "?");
                success = true;
            }
        }
    }

    private void guessWord() {

        boolean exit = false;

        while (!exit) {

            String serverMessage = getServerResponseWithConsoleRead("guess:");
            String[] tokens = serverMessage.split(":");

            if ("guess".equals(tokens[0])) {

                if ("correct".equals(tokens[1])) {

                    System.out.println("Your guess is correct!");
                    exit = true;
                }
                else {
                    System.out.println("Please try again");
                }
            }
            else {
                System.out.println(serverErr);
                exit = true;
            }
        }
    }

    private String getServerResponse(String request) {

        boolean success = false;
        String serverResponse = null;

        while (!success) {

            try {
                out.println(request);

                if ((serverResponse = in.readLine()) == null) {

                    obtainConnection();
                }
                else {
                    success = true;
                }
            }
            catch (IOException e) {

                obtainConnection();
            }
        }

        return serverResponse;
    }

    private String getServerResponseWithConsoleRead(String prepend) {

        boolean success = false;
        String serverMessage = null;

        while (!success) {

            try {
                serverMessage = getServerResponse(prepend + consoleIn.readLine());
                success = true;
            }
            catch (IOException e) {}
        }

        return serverMessage;
    }
}
