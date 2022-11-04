package player;

import map.Tile;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class ChucksPlayer implements MazeWanderer {
    Map map = new Map();
    Stack<Direction> moves = new Stack<>();
    Queue<Direction> nextMove = new LinkedList<>();
    Stack<Direction> prefer = new Stack<>();

    @Override
    public Direction move(Sensor sensor) {
        if (!nextMove.isEmpty())
            return nextMove.poll();

        map.mark(Direction.NORTH, sensor.getNorth());
        map.mark(Direction.EAST, sensor.getEast());
        map.mark(Direction.SOUTH, sensor.getSouth());
        map.mark(Direction.WEST, sensor.getWest());

        Direction direction = getDirection();
        if (direction != null) {
            moves.push(direction);
            map.move(direction);
            prefer.push(direction);
            return direction;
        } else {
            while ((direction = getDirection()) == null && !moves.isEmpty()) {
                System.out.println("Reverse");
                Direction d = reverse(moves.pop());
                nextMove.add(d);
                map.move(d);
            }
            direction = nextMove.poll();
        }

        return direction;
    }

    private Direction reverse(Direction direction) {
        switch (direction) {
            case NORTH:
                return Direction.SOUTH;
            case EAST:
                return Direction.WEST;
            case SOUTH:
                return Direction.NORTH;
            case WEST:
                return Direction.EAST;
        }
        return null;
    }

    private Direction getDirection() {
        Direction next = findGoal();
        if (next != null)
            return next;

        if (!prefer.isEmpty())
            if(map.isPassable(prefer.peek()))
                return prefer.pop();
            else
                prefer.pop();

        double random = Math.random() * 100;
        int start = (int) (random);

        Direction[] values = Direction.values();
        Direction[] order = new Direction[values.length];
        for (int i = 0; i < values.length; i++) {
            order[i] = values[(start + i) % values.length];
        }
        for (Direction direction : order)
            if (map.isPassable(direction))
                next = direction;

        return next;
    }

    private Direction findGoal() {
        for (Direction direction : Direction.values())
            if (map.isGoal(direction))
                return direction;
        return null;
    }


    public static class Map {
        private final int GROWTH_FACTOR = 1;
        int row = 0;
        int column = 0;

        Cell[][] cells = new Cell[2][2];

        public void move(Direction direction) {
            if (direction == Direction.NORTH)
                row--;
            else if (direction == Direction.SOUTH)
                row++;
            else if (direction == Direction.EAST)
                column++;
            else if (direction == Direction.WEST)
                column--;
            cells[row][column].visited = true;
        }

        public Cell north() {
            return cells[row - 1][column];
        }

        public Cell east() {
            return cells[row][column + 1];
        }

        public Cell south() {
            return cells[row + 1][column];
        }

        public Cell west() {
            return cells[row][column - 1];
        }

        public boolean isGoal(Direction direction) {
            Cell cell = getCellByDirection(direction);
            try {
                return cell.tile.isGoal();
            } catch (Exception e) {
                return false;
            }
        }

        public boolean isPassable(Direction direction) {
            Cell cell = getCellByDirection(direction);
            try {
                return !cell.visited && cell.tile.isPassable();
            } catch (Exception e) {
                return false;
            }
        }

        private Cell getCellByDirection(Direction direction) {
            switch (direction) {
                case NORTH:
                    return north();
                case EAST:
                    return east();
                case SOUTH:
                    return south();
                case WEST:
                    return west();
            }
            return null;
        }

        public void mark(Direction direction, Tile tile) {
            if (direction == Direction.NORTH && row == 0)
                growNorth();
            if (direction == Direction.WEST && column == 0)
                growWest();
            if (direction == Direction.SOUTH && row == cells.length - 1)
                growSouth();
            if (direction == Direction.EAST && column == cells[0].length - 1)
                growEast();

            int row = 0;
            int column = 0;

            switch (direction) {
                case EAST: {
                    row = this.row;
                    column = this.column + 1;
                    break;
                }
                case NORTH: {
                    row = this.row - 1;
                    column = this.column;
                    break;
                }
                case WEST: {
                    row = this.row;
                    column = this.column - 1;
                    break;
                }
                case SOUTH: {
                    row = this.row + 1;
                    column = this.column;
                    break;
                }
            }

            Cell cell = cells[row][column];
            if (cell == null) {
                cells[row][column] = new Cell(tile);
            }
        }

        private void growEast() {
            for (int row = 0; row < cells.length; row++)
                cells[row] = Arrays.copyOf(cells[row], cells[row].length + GROWTH_FACTOR);
        }

        private void growSouth() {
            Cell[][] temp = Arrays.copyOf(this.cells, cells.length + GROWTH_FACTOR);
            for (int i = cells.length; i < temp.length; i++)
                temp[i] = new Cell[cells[0].length];
            cells = temp;
        }

        private void growWest() {
            for (int row = 0; row < cells.length; row++)
                cells[row] = shiftRight(cells[row], GROWTH_FACTOR);
            column += GROWTH_FACTOR;
        }

        private void growNorth() {
            int length = cells.length;
            Cell[][] temp = new Cell[length + GROWTH_FACTOR][];

            for (int i = 0; i < GROWTH_FACTOR; i++) {
                temp[i] = new Cell[cells[0].length];
            }
            for (int i = 0; i < cells.length; i++) {
                temp[i + GROWTH_FACTOR] = cells[i];
            }

            cells = temp;
            row += GROWTH_FACTOR;
        }

        public String toString() {
            String result = "";
            for (int row = 0; row < cells.length; row++) {
                for (int column = 0; column < cells[row].length; column++) {
                    if (cells[row][column] == null) {
                        result += " ";
                    } else {
                        result += cells[row][column].tile.getSymbol();
                    }
                }
                result += "\n";
            }
            return result;
        }

        static Cell[] shiftRight(Cell[] original, int growth_factor) {
            Cell[] temp = new Cell[original.length + growth_factor];
            System.arraycopy(original, 0, temp, growth_factor, original.length);
            return temp;
        }
    }


    static class Cell {
        Tile tile;
        boolean visited = false;

        public Cell(Tile tile) {
            this.tile = tile;
            if (tile == Tile.STARTING_LOCATION)
                visited = true;
        }

        public String toString() {
            return tile + " visited:" + visited;
        }
    }
}