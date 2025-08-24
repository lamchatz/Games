package effects;

import domain.Card;
import games.Anxiety;

public interface Effect {
    default void apply(Anxiety game, Card card) {
        game.play(card);
        game.advanceToNextPlayer();
    }
}
