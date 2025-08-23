package main;

import domain.Card;
import domain.Player;
import domain.enums.Category;
import domain.enums.Value;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class GoFish {
    private static final int NUMBER_OF_PLAYERS = 2;
    private static final int NUMBER_OF_CARDS = 4;

    private final Deque<Card> deck;
    private final Deque<Player> players;

    public GoFish() {
        this.deck = new ArrayDeque<>();
        this.players = new ArrayDeque<>();
    }

    private void createDeck() {
        List<Card> tempList = new ArrayList<>(52);
        for (Value val : Value.playableValues()) {
            for (Category category : Category.playableValues()) {
                tempList.add(new Card(val, category));
            }
        }

        Collections.shuffle(tempList);
        deck.clear();

        deck.addAll(tempList);
    }
}
