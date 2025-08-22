package org.example.effects;

import org.example.Anxiety;
import org.example.Card;

public class SkipNext implements Effect{
    @Override
    public void apply(Anxiety game, Card card) {
        game.play(card);
        game.skipNextPlayer();
    }
}
