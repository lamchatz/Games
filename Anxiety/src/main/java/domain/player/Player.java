package domain.player;

import domain.Card;
import domain.enums.Value;

public interface Player {
    void draw(Card card);
    boolean has(Card card);
    boolean has(Value value);
    boolean hasNoCards();
    Card play(Card card);
    void print();
}
