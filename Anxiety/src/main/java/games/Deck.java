package games;

import domain.Card;
import domain.enums.Category;
import domain.enums.Value;
import domain.player.declaration.Player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class Deck {
    private final Deque<Card> cards;

    Deck() {
        cards = new ArrayDeque<>(52);
    }

    Deck(int numberOfCards, Collection<? extends Player> players) {
        this();
        populate();
        deal(numberOfCards, players);
    }

    private void populate() {
        List<Card> tempList = new ArrayList<>(52);

        for (Value val : Value.playableValues()) {
            for (Category category : Category.playableValues()) {
                tempList.add(new Card(val, category));
            }
        }

        shuffle(tempList);
    }

    private Deque<Card> dealPlayedCards() {
        if (size() == 0) {
            return new ArrayDeque<>();
        }

        Deque<Card> played = new ArrayDeque<>(4);
        for (int i = 0; i < 4 && isNotEmpty(); i++) {
            played.push(cards.pop());
        }

        return played;
    }

    void prepareForNewGame(int numberOfCards, Collection<? extends Player> players, Deque<Card> played) {
        cards.clear();
        populate();
        deal(numberOfCards, players);
        played.addAll(dealPlayedCards());
    }

    void deal(int numberOfCards, Collection<? extends Player> players) {
        for (int i = 0; i < numberOfCards; i++) {
            for (Player player : players) {
                if (cards.isEmpty()) {
                    break;
                }
                player.draw(draw());
            }
        }
    }

    void shuffle(List<Card> tempList) {
        tempList.addAll(cards);
        Collections.shuffle(tempList);
        cards.clear();

        cards.addAll(tempList);
    }

    Card draw() {
        return cards.pop();
    }

    int size() {
        return cards.size();
    }

    boolean isNotEmpty() {
        return !cards.isEmpty();
    }

    void print() {
        for (Card card : cards) {
            System.out.println(card.print());
        }
    }

    static void dealCardsTo(Collection<? extends Player> players) {
        Deck d = new Deck();
        d.populateWithNoFigures();
        d.dealAll(players);
    }

    private void populateWithNoFigures() {
        List<Card> tempList = new ArrayList<>(41);

        for (Value val : Value.noFigures()) {
            for (Category category : Category.playableValues()) {
                tempList.add(new Card(val, category));
            }
        }

        tempList.add(new Card(Value.KING, Category.SPADES));

        shuffle(tempList);
    }

    private void dealAll(Collection<? extends Player> players) {
        while (!cards.isEmpty()) {
            for (Player player : players) {
                if (cards.isEmpty()) {
                    break; // stop if deck ran out during this round
                }
                player.draw(draw());
            }
        }
    }
}
