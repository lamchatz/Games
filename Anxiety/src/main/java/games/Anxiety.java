package games;

import domain.Card;
import domain.CardEffects;
import domain.enums.Category;
import domain.enums.Value;
import domain.player.DropPlayer;
import domain.player.Player;
import effects.*;
import util.Reader;

import java.util.*;

public class Anxiety {
    private static final Effect DRAW_TWO = new DrawTwo();
    private static final Effect DRAW_FOUR = new DrawFour();
    private static final Card FOLD_CARD = new Card(Value.FOLD, Category.FOLD);
    private static final int NUMBER_OF_CARDS = 7;
    private final int NUMBER_OF_PLAYERS;
    private final CardEffects cardEffects;
    private final Deck deck;
    private final Deque<Card> played;
    private Deque<Player> players;
    private Category lastPlayedCategory;
    private int cardsToDraw;

    Anxiety(int numberOfPlayers) {
        if (numberOfPlayers * NUMBER_OF_CARDS > 52) {
            throw new IllegalStateException("Too many players or cards dealt to players. Max 52!");
        }

        this.NUMBER_OF_PLAYERS = numberOfPlayers;
        this.cardEffects = new CardEffects();
        this.played = new ArrayDeque<>();
        this.deck = new Deck(NUMBER_OF_CARDS, initPlayers());

        createCardEffects();

        playFirstCard();
        startGame();
    }


    private Collection<Player> initPlayers() {
        this.players = new ArrayDeque<>(NUMBER_OF_PLAYERS);
        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            players.offer(new DropPlayer("Player " + (i + 1)));
        }

        return this.players;
    }

    private void playFirstCard() {
        Card cardPlayed = deck.draw();
        this.played.push(cardPlayed);
        this.lastPlayedCategory = cardPlayed.getCategory();
    }

    private void startGame() {
        while (gameNotOver()) {
            printLastPlayed();
            Card card;
            if (cardsToDraw > 0) {
                card = resolveDrawPenalty();
            } else {
                card = selectCardToPlay();
            }

            cardEffects.get(card).apply(this, card);
        }

        announceWinner();
    }

    private boolean gameNotOver() {
        for (Player player : players) {
            if (player.hasNoCards()) {
                return false;
            }
        }
        return true;
    }

    private Card resolveDrawPenalty() {
        Card card;
        if (hasDrawCard() && Reader.readDraw()) {
            do {
                print("Please play your draw card.");
                card = selectCardToPlay();
            } while (!isDrawCard(card));
        } else {
            drawCards(cardsToDraw);
            cardsToDraw = 0;
            card = selectCardToPlay();
        }

        return card;
    }

    private boolean hasDrawTwoCard() {
        Optional<Value> drawTwo = cardEffects.getValueWith(DRAW_TWO);
        return drawTwo.isPresent() && players.element().has(drawTwo.get());
    }

    private boolean hasDrawFourCard() {
        Optional<Value> drawFour = cardEffects.getValueWith(DRAW_FOUR);
        return drawFour.isPresent() && players.element().has(drawFour.get());
    }

    private boolean hasDrawCard() {
        return hasDrawTwoCard() || hasDrawFourCard();
    }

    private boolean isDrawCard(Card card) {
        return cardEffects.getValueWith(DRAW_TWO)
                .map(card::hasValue)
                .orElseGet(() ->
                        cardEffects.getValueWith(DRAW_FOUR)
                                .filter(card::hasValue)
                                .isPresent()
                );

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

    private void announceWinner() {
        print("Congratulations ");
        players.getLast().print();
    }

    private void reshuffleDeck() {
        Card lastPlayed = played.pop();
        int size = played.size();

        if (size < 1) {
            print("Not enough cards played to re-deal the deck. It is a draw!");
            System.exit(1);
        }

        deck.shuffle(new ArrayList<>(played));
        played.clear();
        played.push(lastPlayed);
    }

    private void createCardEffects() {
        cardEffects.add(Value.ACE, new ChangeCategory());
        cardEffects.add(Value.TWO, new ReverseOrder());
        cardEffects.add(Value.FOUR, DRAW_FOUR);
        cardEffects.add(Value.SEVEN, DRAW_TWO);
        cardEffects.add(Value.EIGHT, new PlayAgain());
        cardEffects.add(Value.NINE, new SkipNext());
        cardEffects.add(Value.FOLD, new Fold());
    }

    public void play(Card card) {
        players.element().play(card);
        this.played.push(card);
        this.lastPlayedCategory = card.getCategory();
    }

    public void advanceToNextPlayer() {
        players.offer(players.remove());
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

    public void draw(int cardsToDraw) {
        advanceToNextPlayer();
        this.cardsToDraw += cardsToDraw;
    }

    public void skipNextPlayer() {
        advanceToNextPlayer();
        advanceToNextPlayer();
    }

    public void reverseOrder() {
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

    //TODO losing cards
    public void drawCards(final int numberOfCards) {
        if (deck.size() < numberOfCards) {
            reshuffleDeck();
        }

        if (deck.size() < numberOfCards) {
            print("Not enough cards played to re-deal the deck. It is a draw!");
            System.exit(1);
        }

        Player player = players.element();
        for (int i = 0; i < numberOfCards; i++) {
            player.draw(deck.draw());
        }
    }

    private void printDeck() {
        deck.print();
    }

    private void printPlayers() {
        players.forEach(Player::print);
    }

    private void printLastPlayed() {
        printEmptyLines();

        Card card = this.played.element();

        if (card.hasValue(Value.ACE)) {
            print(String.format("Last played: %s (%s)", card.print(), lastPlayedCategory));
        } else {
            print(String.format("Last played: %s", card.print()));
        }
    }

    private void printEmptyLines() {
        for (int i = 0; i < 50; i++) {
            print("\n");
        }
    }

    private void print(Object msg) {
        System.out.println(msg);
    }

}
