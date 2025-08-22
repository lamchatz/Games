package org.example;

import org.example.effects.CardEffects;
import org.example.effects.ChangeCategory;
import org.example.effects.DrawTwo;
import org.example.effects.Fold;
import org.example.effects.PlayAgain;
import org.example.effects.SkipNext;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class Anxiety {
    private static final Card FOLD_CARD = new Card(Value.FOLD, Category.FOLD);
    private static final int NUMBER_OF_PLAYERS = 2;
    private static final int NUMBER_OF_CARDS = 7;
    private final CardEffects cardEffects;
    private final Deque<Card> deck;
    private final Deque<Card> played;
    private final Deque<Player> players;
    private Category lastPlayedCategory;
    private boolean drawTwo;
    private int cardsToDraw;

    Anxiety() {
        this.deck = new ArrayDeque<>();
        this.played = new ArrayDeque<>();
        this.players = new ArrayDeque<>();
        this.cardEffects = new CardEffects();

        createCardEffects();
        createDeck();
        initPlayers();

        dealInitialCards();
        //printDeck();
        //printPlayers();
        startGame();
    }

    private void createDeck() {
        for (Value val : Value.playableValues()) {
            for (Category category : Category.playableValues()) {
                this.deck.push(new Card(val, category));
            }
        }

        shuffle();
    }

    private void createCardEffects() {
        cardEffects.add(Value.ACE, new ChangeCategory());
        cardEffects.add(Value.SEVEN, new DrawTwo());
        cardEffects.add(Value.EIGHT, new PlayAgain());
        cardEffects.add(Value.NINE, new SkipNext());
        cardEffects.add(Value.FOLD, new Fold());
    }

    private void shuffle() {
        List<Card> tempList = new ArrayList<>(deck);
        Collections.shuffle(tempList);
        deck.clear();

        for (Card card : tempList) {
            deck.push(card);
        }
    }

    private void initPlayers() {
        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            players.offer(new Player("Player " + (i + 1)));
        }
    }

    private void dealInitialCards() {
        for (int i = 0; i < NUMBER_OF_CARDS; i++) {
            for (Player player : players) {
                player.draw(deck.pop());
            }
        }

        Card cardPlayed = deck.pop();
        this.played.push(cardPlayed);
        this.lastPlayedCategory = cardPlayed.getCategory();
    }

    public void drawCards(final int numberOfCards) {
        if (deck.size() < numberOfCards) {
            reshuffleDeck();
        }

        Player player = players.element();
        for (int i = 0; i < numberOfCards; i++) {
            player.draw(deck.pop());
        }
    }

    private void announceWinner() {
        print("Congratulations ");
        players.getLast().print();
    }

    private void startGame() {
        while (gameNotOver()) {
            printLastPlayed();
            Card card = selectCardToPlay();

            cardEffects.get(card).apply(this, card);
        }

        announceWinner();
    }

    public void play(Card card) {
        players.element().play(card);
        this.played.push(card);
        this.lastPlayedCategory = card.getCategory();
    }

    public void advanceToNextPlayer() {
        players.offer(players.remove());
    }

    private Card selectCardToPlay() {
        Player player = players.element();

        Card card = readCard(player);

        Card lastPlayedCard = played.element();

        while (!canPlayCard(card)) {
            print(String.format("You can not play %s on top of %s", card.print(), lastPlayedCard.print()));
            card = readCard(player);
        }

        return card;
    }

    private Card readCard(Player player) {
        player.print();
        Card c = Reader.readCard();
        while (!player.has(c) && !FOLD_CARD.sameTo(c)) {
            print("please select a card from your hand.");
            print(player);
            c = Reader.readCard();
        }

        return c;
    }

    private boolean canPlayCard(Card card) {
        return Value.ACE.equals(card.getValue())
                || (card.getCategory().equals(lastPlayedCategory))
                || card.sameTo(played.peek())
                || FOLD_CARD.sameTo(card);
    }

    private boolean gameNotOver() {
        for (Player player : players) {
            if (player.hasNoCards()) {
                return false;
            }
        }
        return true;
    }

    public void fold() {
        drawCards(1);
        printLastPlayed();
        Card card = selectCardToPlay();
        if (!FOLD_CARD.sameTo(card)) {
            cardEffects.get(card).apply(this, card);
        } else {
            advanceToNextPlayer();
        }
    }

    public void drawTwo() {
        advanceToNextPlayer();
        drawCards(2);
    }

    private void reshuffleDeck() {
        if (deck.size() <= 1) {
            print("RESHUFFLING");
            Card lastPlayed = played.pop();
            int size = played.size();

            if (size < 1) {
                throw new IllegalStateException("Not enough cards played to re-deal the deck");
            }

            for (int i = 0; i < size; i++) {
                deck.push(played.pop());
            }

            played.push(lastPlayed);
            shuffle();
        }
    }

    public void skipNextPlayer() {
        advanceToNextPlayer();
        advanceToNextPlayer();
    }

    private void reverseOrder() {
        Deque<Player> temp = new ArrayDeque<>();

        while (!players.isEmpty()) {
            temp.push(players.poll());
        }
        while (!temp.isEmpty()) {
            players.offer(temp.pop());
        }
    }

    public void changeCategory(Card card) {
        this.played.push(card);
        this.players.element().play(card);
        lastPlayedCategory = Reader.readCategory();
        advanceToNextPlayer();
    }

    private void printDeck() {
        deck.forEach(Card::print);
    }

    private void printPlayers() {
        players.forEach(Player::print);
    }

    private void printLastPlayed() {
        Card card = this.played.element();

        if (card.hasValue(Value.ACE)) {
            print(String.format("Last played: %s (%s)",card.print(), lastPlayedCategory));
        } else {
            print(String.format("Last played: %s",card.print()));
        }
    }
    private void print(Object msg) {
        System.out.println(msg);
    }

}
