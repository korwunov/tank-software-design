package ru.mipt.bit.platformer.command.impl;

import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.command.CommandContext;
import ru.mipt.bit.platformer.model.World;

public class ToggleHealthStatCommand implements Command {
    @Override
    public void execute(World world, CommandContext context) {
        world.toggleHealthStats();
    }
}
