package ru.mipt.bit.platformer.logic.command.impl;

import ru.mipt.bit.platformer.logic.command.Command;
import ru.mipt.bit.platformer.logic.command.CommandContext;
import ru.mipt.bit.platformer.model.World;

public class ToggleHealthStatCommand implements Command {
    @Override
    public void execute(World world, CommandContext context) {
        world.toggleHealthStats();
    }
}
