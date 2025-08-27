package domain;

import domain.player.declaration.ScoringPlayer;

import java.util.HashSet;
import java.util.Set;

public class Team {
    private final int numberOfMembers;
    private final String name;
    private final Set<ScoringPlayer> members;


    public Team(String name, int numberOfMembers) {
        this.name = name;
        this.numberOfMembers = numberOfMembers;
        members = new HashSet<>(numberOfMembers);
    }

    public void add(ScoringPlayer player) {
        if (numberOfMembers <= members.size()) {
            throw new IllegalStateException(String.format("Too many players in team: %s, max number %d", name, numberOfMembers));
        }

        members.add(player);
    }

    public int getTotalScore() {
        return members.stream().mapToInt(ScoringPlayer::totalScore).sum();
    }

    public boolean has(ScoringPlayer player) {
        return members.contains(player);
    }


    public void print() {
        System.out.printf("Name: %s %nPlayers:%n", name);
        members.forEach(ScoringPlayer::print);
    }

}
