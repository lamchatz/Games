package effects;

import domain.Card;
import main.Anxiety;

public class DrawFour implements Effect{
    @Override
    public void apply(Anxiety game, Card card) {
        game.play(card);
        game.draw(4);
    }
}
