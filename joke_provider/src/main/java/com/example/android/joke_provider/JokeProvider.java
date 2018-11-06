package com.example.android.joke_provider;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class JokeProvider {

    private List<String> jokeList = Arrays.asList(
            "Q. What’s the difference between ignorance and apathy? A. I don’t know and I don’t care.",
            "Did you hear about the semi-colon that broke the law? He was given two consecutive sentences.",
            "A conference call is the best way for a dozen people to say “bye” 300 times.",
            "How do all the oceans say hello to each other?  \nThey wave!",
            "What did one wall say to the other wall?  \nI’ll meet you at the corner!",
            "What do you call a bear with no teeth? \nA gummy bear!",
            "Where do cows go for entertainment?\nTo the moo-vies!",
            "How do you know if there’s an elephant under your bed?\nYour head hits the ceiling!",
            "What do you call a cow with no legs?\nGround beef!");

    public String getRandomJoke() {
        Random randomGenerator = new Random();
        int i = randomGenerator.nextInt(jokeList.size());
        return jokeList.get(i);
    }


}
