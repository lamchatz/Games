package domain;

import domain.enums.Value;
import effects.Advance;
import effects.Effect;
import util.Reader;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CardEffects {
    private final Map<Value, Effect> effects;
    private static final Effect ADVANCE = new Advance();

    public CardEffects() {
        effects = new HashMap<>();
    }

    public void add(Value value, Effect effect) {
        if (effects.containsKey(value) && !Reader.overrideEffect(value, effects.get(value))) {
            return;
        }
        if (hasEffect(effect) && !Reader.confirm(effect)) {
            return;
        }

        effects.put(value, effect);
    }

    private boolean hasEffect(Effect effect) {
        return effects.values().stream().anyMatch(e -> e.getClass().equals(effect.getClass()));
    }

    private Effect get(Value value) {
        return effects.getOrDefault(value, ADVANCE);
    }

    public Effect get(Card card) {
        return get(card.getValue());
    }

    public Optional<Value> getValueWith(Effect effect) {
        for (Map.Entry<Value, Effect> entry : effects.entrySet()) {
            if (effect.equals(entry.getValue())) {
                return Optional.ofNullable(entry.getKey());
            }
        }

        return Optional.empty();
    }
}
