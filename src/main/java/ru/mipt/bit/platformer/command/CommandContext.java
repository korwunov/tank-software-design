package ru.mipt.bit.platformer.command;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Player;
import ru.mipt.bit.platformer.model.World;

import java.util.HashSet;
import java.util.Set;

public class CommandContext {
    private final Set<GridPoint2> reservedCells = new HashSet<>();

    public CommandContext(World world) {
        for (Player tank : world.getAllTanks()) {
            reservedCells.add(tank.getPlayerCoordinates());
        }
    }

    public boolean isFree(GridPoint2 fromPosition, Direction direction) {
        GridPoint2 candidate = fromPosition.add(direction.getX(), direction.getY());
        return !reservedCells.contains(candidate);
    }

    public void reserveCell(GridPoint2 cell) {
        reservedCells.add(new GridPoint2(cell));
    }
}
