package effects;

import domain.Card;
import main.Anxiety;

public interface Effect {
    default void apply(Anxiety game, Card card) {
        game.play(card);
        game.advanceToNextPlayer();
    }
}
