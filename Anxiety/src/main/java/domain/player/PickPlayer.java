package domain.player;

import domain.Card;
import domain.enums.Value;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PickPlayer implements Player {
    private final Map<Value, Set<Card>> hand;
    private final String name;
    private int size = 0;

    public PickPlayer(String name) {
        this.name = validName(name);
        this.hand = new HashMap<>();
        for (Value value : Value.playableValues()) {
            hand.put(value, new HashSet<>(4));
        }
    }

    private String validName(String name) {
        if (name == null || name.isEmpty() || name.isBlank()) {
            throw new IllegalArgumentException(String.format("Given name: %s is not valid!", name));
        }

        return name;
    }

    @Override
    public void draw(Card card) {
        Set<Card> cards = this.hand.get(card.getValue());
        int cardsSize = cards.size();

        if (cardsSize == 3) {
            cards.clear();
            this.hand.remove(card.getValue());
            size -= 3;
            return;
        }

        cards.add(card);
        size++;
    }

    @Override
    public boolean has(Card card) {
        return has(card.getValue());
    }

    @Override
    public boolean has(Value value) {
        return !this.hand.get(value).isEmpty();
    }

    @Override
    public boolean hasNoCards() {
        return size == 0;
    }

    @Override
    public Card play(Card card) {
        return remove(card);
    }

    private Card remove(Card card) {
        size--;
        for (Iterator<Card> it = hand.get(card.getValue()).iterator(); it.hasNext(); ) {
            Card c = it.next();
            if (c.sameTo(card)) {
                it.remove();
                return c;
            }
        }

        throw new IllegalStateException("The card was not found");
    }

    @Override
    public String getName() {
        return name;
    }

    //TODO maybe change card print order? go with value first
    @Override
    public void print() {
        System.out.println(name);

        hand.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String cardList = entry.getValue().stream()
                            .map(Card::print)
                            .collect(Collectors.joining(", "));
                    System.out.println(entry.getKey() + ": " + cardList);
                });
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof PickPlayer that)) return false;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
