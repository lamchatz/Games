package main;

import domain.Card;
import domain.enums.Category;
import domain.Count;
import domain.enums.Value;
import domain.player.PickPlayer;
import domain.player.Player;
import util.Reader;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class GoFish {
    private static final int NUMBER_OF_PLAYERS = 2;
    private static final int NUMBER_OF_CARDS = 15;
    private final Deque<Card> deck;
    private final List<Player> players;
    private Count count;

    public GoFish() {
        this.deck = new ArrayDeque<>();
        this.players = new ArrayList<>(NUMBER_OF_PLAYERS);
        this.count = new Count(NUMBER_OF_PLAYERS);

        createDeck();
        initPlayers();

        dealInitialCards();
        startGame();
    }

    private void createDeck() {
        List<Card> tempList = new ArrayList<>(52);
        for (Value val : Value.playableValues()) {
            for (Category category : Category.playableValues()) {
                tempList.add(new Card(val, category));
            }
        }

        Collections.shuffle(tempList);
        deck.addAll(tempList);
    }

    private void initPlayers() {
        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            players.add(new PickPlayer("Player " + (i + 1)));
        }
    }

    private void dealInitialCards() {
        for (int i = 0; i < NUMBER_OF_CARDS; i++) {
            for (Player player : players) {
                player.draw(deck.pop());
            }
        }
    }

    private void startGame() {
        while (gameNotOver()) {
            Player currentPlayer = players.get(count.current());
            currentPlayer.print();
            Card card = selectCardToPlay();
            Player playerToAsk = players.get(Reader.askPlayer(NUMBER_OF_PLAYERS, count.current()));

            boolean gotCards = false;
            while (playerToAsk.has(card)) {
                currentPlayer.draw(playerToAsk.play(card));
                gotCards = true;
            }

            if (!gotCards) {
                print("Go Fish!");
                Card cardAfter = deck.pop();
                print(String.format("You got %s", cardAfter.print()));
                currentPlayer.draw(cardAfter);
                if (!card.sameTo(cardAfter)) {
                    count.next();
                }
            } else {
                print("Keep going!");
            }
        }
    }

    private boolean gameNotOver() {
        return players.stream().noneMatch(Player::hasNoCards)
                || !deck.isEmpty();
    }


    private Card selectCardToPlay() {
        Player player = players.get(count.current());

        Card card;
        do {
            card = Reader.readCard();
        } while (!player.has(card));


        return card;
    }

    private void printPlayers() {
        players.forEach(Player::print);
    }

    private void print(Object msg) {
        System.out.println(msg);
    }

}
