package com.romanmarkunas.server;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class SocketQuizServerLauncher {

    private static final String USAGE_NOTE =
            "Correct usage: java -jar <executable> <lines to preload before starting service> <dictionary file location>";


    public static void main(String[] args) {

        exitOnBadArgs(args);
        int preloadLines = getPreloadLines(args[0]);
        String file = args[1];
        exitOnWrongParameter(preloadLines);

        CountDownLatch preloadingSignal = new CountDownLatch(preloadLines);
        SocketQuizServer server = new SocketQuizServer(preloadingSignal, new Dictionary(preloadingSignal),
                                                        Executors.newCachedThreadPool(), file);
        server.start();
    }


    private static void exitOnBadArgs(String[] args) {

        if (args.length != 2) {

            System.out.println(USAGE_NOTE);
            System.exit(1);
        }
    }

    private static int getPreloadLines(String arg) {

        int lines;

        try {
            lines = Integer.parseInt(arg);
        }
        catch (NumberFormatException e) {

            lines = -1;
        }

        return lines;
    }

    private static void exitOnWrongParameter(int preloadLines) {

        if (preloadLines <= 0) {

            System.out.println(USAGE_NOTE);
            System.exit(1);
        }
    }
}
