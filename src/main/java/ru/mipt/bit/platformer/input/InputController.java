package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.Direction;

import java.util.Collection;
import java.util.Optional;

public interface InputController {
    Collection<InputEvent> poll();
}
