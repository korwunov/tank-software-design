package ru.mipt.bit.platformer.logic.command.impl;

import ru.mipt.bit.platformer.logic.command.Command;
import ru.mipt.bit.platformer.logic.command.CommandContext;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.model.World;

public class ShootCommand implements Command {
    private final Player player;

    public ShootCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute(World world, CommandContext context) {
        if (!player.isAlive()) return;
        Bullet bullet = player.shoot();
        world.addBullet(bullet);
    }
}
