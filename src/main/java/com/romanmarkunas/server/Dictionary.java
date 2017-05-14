package com.romanmarkunas.server;

import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;

class Dictionary {

    private final CountDownLatch preloadSignal;

    private final Random rn = new Random();
    private final Object listLock = new Object();
    private final List<String[]> synonyms;


    Dictionary(CountDownLatch preloadSignal) {

        this.preloadSignal = preloadSignal;
        synonyms = new ArrayList<>(100);
    }


    void loadFrom(String file) throws IOException {

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {

            String line;
            String[] synonymSet;

            while ((line = in.readLine()) != null) {

                synonymSet = line.split(",");

                synchronized (listLock) {

                    synonyms.add(synonymSet);
                }

                preloadSignal.countDown();
            }
        }
    }

    String[] getRandomSynonymSet() {

        String[] synonymSet;
        String[] temp;

        synchronized (listLock) {

            temp = synonyms.get(rn.nextInt(synonyms.size()));
        }

        synonymSet = new String[temp.length];
        System.arraycopy(temp, 0, synonymSet, 0, temp.length);

        return synonymSet;
    }
}
