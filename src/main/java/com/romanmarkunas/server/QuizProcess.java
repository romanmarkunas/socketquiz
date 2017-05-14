package com.romanmarkunas.server;

import java.io.*;
import java.net.Socket;

class QuizProcess implements Runnable {

    private final Socket socket;
    private final Dictionary dictionary;

    private Quiz currentQuiz = null;


    QuizProcess(Dictionary dictionary, Socket socket) {

        this.dictionary = dictionary;
        this.socket = socket;
    }


    @Override
    public void run() {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {

            String message;

            while ((message = in.readLine()) != null) {

                replyToClient(message, out);
            }
        }
        catch (IOException e) {}

        try {
            socket.close();
        }
        catch (IOException e) {}
    }


    private void replyToClient(String clientMessage, PrintWriter out) {

        String[] tokens = clientMessage.split(":");

        if ("new".equals(tokens[0])) {

            currentQuiz = new Quiz(dictionary.getRandomSynonymSet());
            out.println("new:" + currentQuiz.getHint());
        }
        else if ("guess".equals(tokens[0]) && tokens.length == 2 && currentQuiz != null) {

            if (currentQuiz.correctGuess(tokens[1])) {

                currentQuiz = null;
                out.println("guess:correct");
            }
            else {
                out.println("guess:wrong");
            }
        }
        else {
            out.println("err");
        }
    }
}
