package effects;

import domain.Card;
import games.Anxiety;

public class PlayAgain implements Effect{
    @Override
    public void apply(Anxiety game, Card card) {
        game.play(card);
    }
}
