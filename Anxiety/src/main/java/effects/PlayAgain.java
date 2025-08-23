package effects;

import Anxiety;
import domain.Card;

public class PlayAgain implements Effect{
    @Override
    public void apply(Anxiety game, Card card) {
        game.play(card);
        //doNothing
    }
}
