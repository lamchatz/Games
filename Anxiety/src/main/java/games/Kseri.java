package games;

import domain.Card;
import domain.player.GatherPlayer;
import domain.player.Player;
import domain.player.ScoringPlayer;
import util.Reader;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.stream.Collectors;

public class Kseri {
    private static final int NUMBER_OF_CARDS = 4;
    private static final int NUMBER_OF_PLAYERS = 2;
    private final Deck deck;
    private final Deque<Card> played;
    private Deque<ScoringPlayer> players;
    private ScoringPlayer lastPlayerToGather;

    Kseri () {
        this.deck = new Deck(NUMBER_OF_CARDS, initPlayers());
        this.played = deck.dealPlayedCards();

        startGame();
    }

    private Collection<ScoringPlayer> initPlayers() {
        this.players = new ArrayDeque<>(NUMBER_OF_PLAYERS);
        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            players.offer(new GatherPlayer("Player " + (i + 1)));
        }

        return this.players;
    }

    //TODO add Jack
    //TODO make use of effects?
    private void startGame() {
        while (gameNotOver()) {
            printPlayedCards();
            ScoringPlayer player = this.players.element();
            Card card = selectCardToPlay(player);
            Card lastPlayed = played.peek();

            if (lastPlayed != null && card.sameTo(lastPlayed)) {
                collect(player, card);
            } else {
                this.played.push(card);
            }

            if (playersOutOfCards()) {
                played.addAll(deck.dealPlayedCards());
                deck.deal(NUMBER_OF_CARDS, players);
            }

            advanceToNextPlayer();
        }

        print(String.format("Last player to gather was %s. he gathers now", lastPlayerToGather.getName()));
        lastPlayerToGather.gather(played);

        for (ScoringPlayer player : players) {
            System.out.println(player.totalScore());
        }



    }

    private void collect(ScoringPlayer player, Card card) {
        if (played.size() == 1) {
            player.jackPot(card);
        } else {
            player.gather(played);
        }
        lastPlayerToGather = player;
        played.clear();
    }

    private boolean playersOutOfCards() {
        return players.stream().allMatch(Player::hasNoCards);
    }

    private boolean gameNotOver() {
        return deck.isNotEmpty();
    }

    private Card selectCardToPlay(Player player) {
        player.print();
        Card card = Reader.readCard();
        while (!player.has(card)) {
            print("please select a card from your hand.");
            player.print();
            card = Reader.readCard();
        }

        player.play(card);
        return card;
    }

    void advanceToNextPlayer() {
        players.offer(players.remove());
    }

    private void print(Object msg) {
        System.out.println(msg);
    }

    private void printPlayers() {
        players.forEach(Player::print);
    }

    private void printPlayedCards() {
        System.out.println("Cards played");
        System.out.println(
                played.stream()
                        .map(Card::print)
                        .collect(Collectors.joining(","))
        );

        System.out.println();
    }
}
