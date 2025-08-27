package games;

import domain.Card;
import domain.Count;
import domain.player.declaration.Player;
import domain.player.implementation.PickPlayer;
import util.Reader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GoFish {
    private final int numberOfPlayers;
    private static final int NUMBER_OF_CARDS = 15;
    private final Deck deck;
    private List<Player> players;
    private final Count count;

    public GoFish(final int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        count = new Count(numberOfPlayers);
        deck = new Deck(NUMBER_OF_CARDS, initPlayers());

        startGame();
    }

    private Collection<Player> initPlayers() {
        players = new ArrayList<>(numberOfPlayers);
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new PickPlayer("Player " + (i + 1)));
        }

        return players;
    }

    private void startGame() {
        while (gameNotOver()) {
            Player currentPlayer = players.get(count.current());
            currentPlayer.print();
            Card card = selectCardToPlay();
            Player playerToAsk = players.get(Reader.askPlayer(numberOfPlayers, count.current()));

            boolean gotCards = false;
            while (playerToAsk.has(card)) {
                currentPlayer.draw(playerToAsk.play(card));
                gotCards = true;
            }

            if (!gotCards) {
                print("Go Fish!");
                Card cardAfter = deck.draw();
                print(String.format("You got %s", cardAfter.print()));
                currentPlayer.draw(cardAfter);
                if (!card.sameTo(cardAfter)) {
                    count.next();
                } else {
                    print("Keep going");
                }
            } else {
                print("Keep going!");
            }
        }

        announceWinner();
    }

    private boolean gameNotOver() {
        return players.stream().noneMatch(Player::hasNoCards)
                && deck.isNotEmpty();
    }


    private Card selectCardToPlay() {
        Player player = players.get(count.current());

        Card card;
        do {
            card = Reader.readCard();
        } while (!player.has(card));


        return card;
    }

    private void announceWinner() {
        print("Congratulations ");
        players.get(count.current()).print();
    }

    private void printPlayers() {
        players.forEach(Player::print);
    }

    private void print(Object msg) {
        System.out.println(msg);
    }

}
