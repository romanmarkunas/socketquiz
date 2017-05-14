package com.romanmarkunas.server;

import java.io.*;
import java.net.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

class SocketQuizServer {

    private final CountDownLatch preloadingSignal;
    private final Dictionary dictionary;
    private final ExecutorService threadPool;
    private final String file;

    private AtomicBoolean online = new AtomicBoolean();


    SocketQuizServer(CountDownLatch preloadingSignal, Dictionary dictionary, ExecutorService threadPool, String file) {

        this.preloadingSignal = preloadingSignal;
        this.dictionary = dictionary;
        this.threadPool = threadPool;
        this.file = file;
    }


    void start() {

        online.set(true);

        try (ServerSocket serverSocket = new ServerSocket(4444)) {

            dictionary.loadFrom(file);
            preloadingSignal.await();

            while (online.get()) {

                Socket socket = serverSocket.accept();
                threadPool.submit(new QuizProcess(dictionary, socket));
            }
        }
        catch (IOException | InterruptedException e) {

            e.printStackTrace();
        }
    }

    void stop() {

        online.set(false);
    }
}
