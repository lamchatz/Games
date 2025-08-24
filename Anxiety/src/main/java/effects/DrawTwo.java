package effects;

import domain.Card;
import games.Anxiety;

public class DrawTwo implements Effect{
    @Override
    public void apply(Anxiety game, Card card) {
        game.play(card);
        game.draw(2);
    }
}
