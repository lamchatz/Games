package games;

import domain.Card;
import domain.enums.Category;
import domain.enums.Value;
import domain.player.Player;

import java.util.*;

public class Deck {
    private final Deque<Card> cards;

    Deck(int numberOfCards, Collection<Player> players) {
        this.cards = new ArrayDeque<>();
        create();
        deal(numberOfCards, players);
    }

    void create() {
        List<Card> tempList = new ArrayList<>(52);

        for (Value val : Value.playableValues()) {
            for (Category category : Category.playableValues()) {
                tempList.add(new Card(val, category));
            }
        }

        shuffle(tempList);
    }

    void shuffle(List<Card> tempList) {
        Collections.shuffle(tempList);
        cards.clear();

        cards.addAll(tempList);
        tempList.clear();
    }

    Card pop() {
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

    private void deal(int numberOfCards, Collection<Player> players) {
        for (int i = 0; i < numberOfCards; i++) {
            for (Player player : players) {
                player.draw(cards.pop());
            }
        }
    }


}
