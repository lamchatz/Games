package org.example.effects;

import org.example.Anxiety;
import org.example.Card;

public class DrawTwo implements Effect{
    @Override
    public void apply(Anxiety game, Card card) {
        //game.drawCards(2);
        game.play(card);
        game.drawTwo();
    }
}
