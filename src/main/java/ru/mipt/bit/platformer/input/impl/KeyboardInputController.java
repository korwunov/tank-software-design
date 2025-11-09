package ru.mipt.bit.platformer.input.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.input.InputController;
import ru.mipt.bit.platformer.input.InputEvent;
import ru.mipt.bit.platformer.model.Direction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KeyboardInputController implements InputController {
    @Override
    public Collection<InputEvent> poll() {
        List<InputEvent> events = new ArrayList<>();
        if (isKeyPressed(Input.Keys.UP, Input.Keys.W)) {events.add(InputEvent.move(Direction.UP));}
        if (isKeyPressed(Input.Keys.LEFT, Input.Keys.A)) {events.add(InputEvent.move(Direction.LEFT));}
        if (isKeyPressed(Input.Keys.DOWN, Input.Keys.S)) {events.add(InputEvent.move(Direction.DOWN));}
        if (isKeyPressed(Input.Keys.RIGHT, Input.Keys.D)) {events.add(InputEvent.move(Direction.RIGHT));}
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {events.add(InputEvent.shoot());}
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {events.add(InputEvent.toggleHealth());}
        return events;
    }

    private boolean isKeyPressed(int... keys) {
        for (int key : keys) {
            if (Gdx.input.isKeyPressed(key)) {
                return true;
            }
        }
        return false;
    }
}
