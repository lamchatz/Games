package games;

import domain.Card;
import domain.Count;
import domain.player.PickPlayer;
import domain.player.Player;
import util.Reader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GoFish {
    private final int NUMBER_OF_PLAYERS;
    private static final int NUMBER_OF_CARDS = 15;
    private final Deck deck;
    private List<Player> players;
    private final Count count;

    public GoFish(final int  numberOfPlayers) {
        this.NUMBER_OF_PLAYERS = numberOfPlayers;
        this.count = new Count(NUMBER_OF_PLAYERS);
        this.deck = new Deck(NUMBER_OF_CARDS, initPlayers());

        startGame();
    }

    private Collection<Player> initPlayers() {
        this.players = new ArrayList<>(NUMBER_OF_PLAYERS);
        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            players.add(new PickPlayer("Player " + (i + 1)));
        }

        return this.players;
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
