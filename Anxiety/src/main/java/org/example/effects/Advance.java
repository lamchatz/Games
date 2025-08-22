package org.example.effects;

import org.example.Anxiety;
import org.example.Card;

public class Advance implements Effect{
    @Override
    public void apply(Anxiety game, Card card) {
        Effect.super.apply(game, card);
    }
}
