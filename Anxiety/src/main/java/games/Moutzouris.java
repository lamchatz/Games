package games;

import domain.Count;
import domain.player.declaration.Player;
import domain.player.implementation.MoutzourisPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Moutzouris {
    private final int numberOfPlayers;
    private List<Player> players;
    private final Count count;

    public Moutzouris(final int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        count = new Count(numberOfPlayers);
        Deck.dealCardsTo(initPlayers());
        startGame();
    }

    private Collection<Player> initPlayers() {
        players = new ArrayList<>(numberOfPlayers);
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new MoutzourisPlayer("Player " + (i + 1)));
        }

        return players;
    }

    private void startGame() {
        printPlayers();

        while (gameNotOver()) {
            Player player = players.get(count.current());
            player.draw(players.get(count.previous()).play(null));
        }

        announceWinner();
    }

    private boolean gameNotOver() {
        long playersWithOneCard = players.stream()
                .filter(p -> p.numberOfCards() == 1)
                .count();

        long playersWithNoCards = players.stream()
                .filter(Player::hasNoCards)
                .count();

        // false only if exactly one player has 1 card AND the rest have none
        return !(playersWithOneCard == 1 && playersWithNoCards == players.size() - 1);
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
