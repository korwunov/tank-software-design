package ru.mipt.bit.platformer.logic.command;

import ru.mipt.bit.platformer.model.World;

public interface Command {
    void execute(World world, CommandContext context);
}
