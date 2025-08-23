package domain.effects;

import Anxiety;
import domain.Card;

public class DrawFour implements Effect{
    @Override
    public void apply(Anxiety game, Card card) {
        game.play(card);
        game.draw(4);
    }
}
