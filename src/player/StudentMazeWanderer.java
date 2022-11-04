package player;


import map.Tile;

import java.util.Stack;

public class StudentMazeWanderer implements MazeWanderer {
    Cell[][] map = new Cell[1000][1000];
    Stack<Direction> moves = new Stack<>();
    int column = 500, row = 500;

    public StudentMazeWanderer() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                map[i][j] = new Cell();
            }
        }
    }

    @Override
    public Direction move(Sensor sensor) {
        map[row][column].visited = true;
        updateMap(sensor);

        Direction direction;
        if (hasAvailableMove()) {
            direction = nextAvailableMove();
            moves.push(direction);
        } else
            direction = reverse(moves.pop());

        moveDirection(direction);
        return direction;
    }

    private void moveDirection(Direction direction) {
        if (direction.equals(Direction.NORTH))
            row++;
        if (direction.equals(Direction.SOUTH))
            row--;
        if (direction.equals(Direction.EAST))
            column++;
        if (direction.equals(Direction.WEST))
            column--;
    }

    private Direction reverse(Direction direction) {
        switch (direction) {
            case NORTH:
                return Direction.SOUTH;
            case EAST:
                return Direction.WEST;
            case SOUTH:
                return Direction.NORTH;
            default:
                return Direction.EAST;
        }
    }

    private Direction nextAvailableMove() {
        if (north().isFree())
            return Direction.NORTH;
        if (south().isFree())
            return Direction.SOUTH;
        if (east().isFree())
            return Direction.EAST;
        if (west().isFree())
            return Direction.WEST;
        return null;
    }

    private boolean hasAvailableMove() {
        return nextAvailableMove() != null;
    }

    private void updateMap(Sensor sensor) {
        north().tile = sensor.getNorth();
        south().tile = sensor.getSouth();
        east().tile = sensor.getEast();
        west().tile = sensor.getWest();
    }

    private static class Cell {
        public Tile tile;
        public boolean visited = false;

        public boolean isFree() {
            return !visited & tile.isPassable();
        }
    }

    private Cell west() {
        return map[row][column - 1];
    }

    private Cell east() {
        return map[row][column + 1];
    }

    private Cell south() {
        return map[row - 1][column];
    }

    private Cell north() {
        return map[row + 1][column];
    }
}