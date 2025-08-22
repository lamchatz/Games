package org.example.effects;

import org.example.Anxiety;
import org.example.Card;

public class Fold implements Effect{

    @Override
    public void apply(Anxiety game, Card card) {
        game.fold();
    }
}
