package games;

import domain.Card;
import domain.enums.Category;
import domain.enums.Value;
import domain.player.Player;

import java.util.*;

public class Deck {
    private final Deque<Card> cards;

    Deck(int numberOfCards, Collection<? extends Player> players) {
        this.cards = new ArrayDeque<>();
        create();
        deal(numberOfCards, players);
    }

    private void create() {
        List<Card> tempList = new ArrayList<>(52);

        for (Value val : Value.playableValues()) {
            for (Category category : Category.playableValues()) {
                tempList.add(new Card(val, category));
            }
        }

        shuffle(tempList);
    }

    void deal(int numberOfCards, Collection<? extends Player> players) {
        for (int i = 0; i < numberOfCards; i++) {
            for (Player player : players) {
                if (cards.isEmpty()) {
                    break;
                }
                player.draw(cards.pop());
            }
        }
    }

    void shuffle(List<Card> tempList) {
        Collections.shuffle(tempList);
        cards.clear();

        cards.addAll(tempList);
        tempList.clear();
    }

    Card top() {
        return this.cards.pop();
    }

    int size() {
        return cards.size();
    }

    void print() {
        for (Card card : cards) {
            System.out.println(card.print());
        }
    }

    boolean isNotEmpty() {
        return !cards.isEmpty();
    }

    Deque<Card> dealPlayedCards() {
        if (size() == 0) {
            return new ArrayDeque<>();
        }

        Deque<Card> played = new ArrayDeque<>(4);
        for (int i = 0; i < 4; i++) {
            played.push(cards.pop());
        }

        return played;
    }





}
