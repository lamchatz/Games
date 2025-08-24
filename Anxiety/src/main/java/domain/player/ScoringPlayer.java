package domain.player;

import domain.Card;

import java.util.Collection;

public interface ScoringPlayer extends Player {
    void gather(Collection<Card> cards);

    void jackPot(Card card);

    int totalScore();
}
