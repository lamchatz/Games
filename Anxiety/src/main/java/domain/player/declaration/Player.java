package domain.player.declaration;

import domain.Card;
import domain.enums.Value;

public interface Player {
    void draw(Card card);
    Card play(Card card);
    boolean has(Card card);
    boolean has(Value value);
    boolean hasNoCards();
    int numberOfCards();
    String getName();
    void print();
}
