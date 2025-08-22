package org.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class Player {
    private final Collection<Card> hand;
    private final String name;

    Player(String name) {
        this.hand = new ArrayList<>(7);
        this.name = Objects.requireNonNull(name);
    }

    public Collection<Card> getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    public void draw(Card card) {
        hand.add(card);
    }

    public boolean has(Card card) {
        return this.hand.contains(card);
    }

    public boolean has(Value value) {
        return hand.stream().anyMatch(card -> value.equals(card.getValue()));
    }

    public boolean hasNoCards() {
        return this.hand.isEmpty();
    }

    public void play(Card card) {
        this.hand.remove(card);
    }

    public void print() {
        System.out.println(name);

        System.out.println(
                hand.stream()
                        .map(Card::print)
                        .collect(Collectors.joining(","))
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Player{");
        sb.append("hand=").append(hand);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
