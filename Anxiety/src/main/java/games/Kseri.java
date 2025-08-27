package games;

import domain.Card;
import domain.Team;
import domain.enums.Value;
import domain.player.declaration.Player;
import domain.player.declaration.ScoringPlayer;
import domain.player.implementation.GatherPlayer;
import util.Reader;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Kseri {
    private static final int NUMBER_OF_CARDS = 4;
    private static final String LAST_PLAYER_TO_GATHER_WAS_PLAYER_HE_GATHERS_NOW = "Last player to gather was %s. He gathers now";
    private static final String WE_GO_AGAIN = "We go again!";
    private static final String PLEASE_SELECT_A_CARD_FROM_YOUR_HAND = "Please select a card from your hand.";
    private static final String CONGRATULATIONS = "Congratulations";
    private static final String PLAYER = "Player ";
    private final Deck deck;
    private final Deque<Card> played;
    private final List<Team> teams;
    private Deque<ScoringPlayer> players;
    private ScoringPlayer lastPlayerToGather;
    private Team leadingTeam;
    private int maxTeamScore;
    private final int numberOfPlayers;
    private final int targetScore;
    private final int numberOfTeams;
    private boolean gameNotOver;


    Kseri(final int numberOfPlayers, final int numberOfTeams, final int targetScore) {
        if (numberOfPlayers < 2) {
            throw new IllegalArgumentException("Not sufficient players for Kseri, at least 2 needed");
        }

        if (numberOfTeams < 2) {
            throw new IllegalArgumentException("Not sufficient teams for Kseri, at least 2 needed");
        }

        if (numberOfPlayers % numberOfTeams != 0) {
            throw new IllegalArgumentException(String.format("Teams must have the same number of players. You've given %d teams and %d players", numberOfTeams, numberOfPlayers));
        }

        if (targetScore < 25) {
            throw new IllegalArgumentException("Target score should be above 25");
        }

        gameNotOver = true;

        this.numberOfPlayers = numberOfPlayers;
        this.numberOfTeams = numberOfTeams;
        this.targetScore = targetScore;

        players = new ArrayDeque<>(numberOfPlayers);
        teams = new ArrayList<>(numberOfTeams);
        played = new ArrayDeque<>();
        deck = new Deck();
        deck.prepareForNewGame(NUMBER_OF_CARDS, initPlayers(), played);

        createTeams();
        startGame();
    }

    private Collection<ScoringPlayer> initPlayers() {
        players = new ArrayDeque<>(numberOfPlayers);
        for (int i = 0; i < numberOfPlayers; i++) {
            players.offer(new GatherPlayer(PLAYER + (i + 1)));
        }

        return players;
    }

    private void createTeams() {
        int teamPlayers = numberOfPlayers / numberOfTeams;

        for (int i = 0; i < numberOfTeams; i++) {
            teams.add(new Team("Team " + (i + 1), teamPlayers));
        }

        int j = 0;
        while (j < teamPlayers) {
            for (Team team : teams) {
                ScoringPlayer player = players.remove();
                players.offer(player);
                team.add(player);
            }
            j++;
        }

    }

    private void startGame() {
        do {
            startRound();
            if (gameNotOver) {
                restart();
            }
        } while (gameNotOver);

        announceWinner();
    }

    private void startRound() {
        while (gameNotOver()) {
            printPlayedCards();
            ScoringPlayer player = players.element();
            Card card = selectCardToPlay(player);
            Card lastPlayed = played.peek();

            if (canCollect(lastPlayed, card)) {
                collect(player, card, jackpotPredicate(card));
            } else {
                played.push(card);
            }

            if (playersOutOfCards()) {
                deck.deal(NUMBER_OF_CARDS, players);
            }

            advanceToNextPlayer();
        }

        gatherScraps();
        attributePointsForMostCards();
    }

    private boolean gameNotOver() {
        return deck.isNotEmpty() || !playersOutOfCards();
    }

    private boolean playersOutOfCards() {
        return players.stream().allMatch(Player::hasNoCards);
    }

    private Card selectCardToPlay(Player player) {
        player.print();
        Card card = Reader.readCard();
        while (!player.has(card)) {
            print(PLEASE_SELECT_A_CARD_FROM_YOUR_HAND);
            player.print();
            card = Reader.readCard();
        }

        player.play(card);
        return card;
    }

    private static boolean canCollect(Card lastPlayed, Card card) {
        return lastPlayed != null && (lastPlayed.sameTo(card) || card.hasValue(Value.JACK));
    }

    private Predicate<Card> jackpotPredicate(Card card) {
        if (card.hasValue(Value.JACK)) {
            return this::isJackJackpot;
        } else {
            return this::isNormalJackpot;
        }
    }

    private boolean isJackJackpot(Card card) {
        return played.size() == 1 && played.peek().hasValue(Value.JACK);
    }

    private boolean isNormalJackpot(Card card) {
        return played.size() == 1;
    }

    private void collect(ScoringPlayer player, Card card, Predicate<Card> jackpotRule) {
        if (jackpotRule.test(card)) {
            player.jackPot(card);
        } else {
            played.push(card);
            player.gather(played);
        }

        updateMaxScore(player);
        lastPlayerToGather = player;
    }

    private void advanceToNextPlayer() {
        players.offer(players.remove());
    }

    private void gatherScraps() {
        if (lastPlayerToGather == null) {
            print("Somehow nobody gathered anything... so nobody gets the points");
            played.clear();
        } else {
            print(String.format(LAST_PLAYER_TO_GATHER_WAS_PLAYER_HE_GATHERS_NOW, lastPlayerToGather.getName()));
            lastPlayerToGather.gather(played);

            updateMaxScore(lastPlayerToGather);
        }
    }

    private void attributePointsForMostCards() {
        players.stream().max(Comparator.comparingInt(ScoringPlayer::totalCardsGathered)).ifPresent(player -> {
            player.addMostCardsGatheredPoints();
            updateMaxScore(player);
        });

    }

    private void updateMaxScore(ScoringPlayer player) {
        played.clear();

        Optional<Team> teamOptional = teams.stream().filter(t -> t.has(player)).findFirst();

        if (teamOptional.isEmpty()) {
            throw new IllegalStateException(String.format("Player %s does not belong to any team for what ever reason", player.getName()));
        }
        Team team = teamOptional.get();

        int teamTotalScore = team.getTotalScore();
        if (teamTotalScore > maxTeamScore) {
            maxTeamScore = teamTotalScore;
            leadingTeam = team;

            gameNotOver = targetScore > teamTotalScore;
        }
    }

    private void restart() {
        printPlayerScores();
        print(WE_GO_AGAIN);

        deck.prepareForNewGame(NUMBER_OF_CARDS, players, played);
    }

    private void announceWinner() {
        print(CONGRATULATIONS);
        leadingTeam.print();
    }

    private void print(Object msg) {
        System.out.println(msg);
    }

    private void printPlayers() {
        players.forEach(Player::print);
    }

    private void printPlayerScores() {
        for (ScoringPlayer player : players) {
            print(player.getName() + ": " + player.totalScore());
        }
    }

    private void printPlayedCards() {
        print("Cards played");
        print(
                played.stream()
                        .map(Card::print)
                        .collect(Collectors.joining(","))
        );

        print("");
    }
}
