package domain.player;

import domain.Card;
import domain.enums.Value;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class DropPlayer implements Player {
    private final Set<Card> hand;
    private final String name;


    public DropPlayer(String name) {
        this.hand = new TreeSet<>();
        this.name = validName(name);
    }

    private String validName(String name) {
        if (name == null || name.isEmpty() || name.isBlank()) {
            throw new IllegalArgumentException(String.format("Given name: %s is not valid!", name));
        }

        return name;
    }

    @Override
    public void draw(Card card) {
        hand.add(card);
    }

    @Override
    public boolean has(Card card) {
        return this.hand.contains(card);
    }

    @Override
    public boolean has(Value value) {
        return hand.stream().anyMatch(card -> value.equals(card.getValue()));
    }

    @Override
    public boolean hasNoCards() {
        return this.hand.isEmpty();
    }

    @Override
    public Card play(Card card) {
        this.hand.remove(card);
        return card;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
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

        DropPlayer player = (DropPlayer) o;

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
