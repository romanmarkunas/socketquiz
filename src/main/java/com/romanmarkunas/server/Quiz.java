package com.romanmarkunas.server;

import java.util.Random;

class Quiz {

    private final String[] answers;
    private String hint;


    Quiz(String[] synonyms) {

        int hintIndex = new Random().nextInt(synonyms.length);
        int answersIndex = 0;

        hint = "";
        answers = new String[synonyms.length - 1];

        for (int i = 0; i < synonyms.length; i++) {

            if (i == hintIndex) {

                hint = synonyms[i];
            }
            else {
                answers[answersIndex] = synonyms[i];
                answersIndex++;
            }
        }
    }


    String getHint() { return hint; }

    boolean correctGuess(String guess) {

        for (String answer : answers) {

            if (guess.equals(answer)) {

                return true;
            }
        }

        return false;
    }
}
