package domain.effects;

import Anxiety;
import domain.Card;

public class ChangeCategory implements Effect{
    @Override
    public void apply(Anxiety game, Card card) {
        game.changeCategory(card);
    }
}
