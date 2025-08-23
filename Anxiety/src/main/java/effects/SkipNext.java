package effects;

import Anxiety;
import domain.Card;

public class SkipNext implements Effect{
    @Override
    public void apply(Anxiety game, Card card) {
        game.play(card);
        game.skipNextPlayer();
    }
}
