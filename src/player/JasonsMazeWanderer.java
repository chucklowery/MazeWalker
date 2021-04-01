package player;

import map.Tile;

import java.util.HashSet;
import java.util.Objects;
import java.util.Stack;

public class JasonsMazeWanderer implements MazeWanderer {

    public class Vertex {

        public Vertex north;
        public Vertex south;
        public Vertex east;
        public Vertex west;
        public Tile tile;
        public boolean visited;

        public Vertex(Tile tile) {
            this.tile = tile;
            visited = false;
        }

        public Vertex() {
            visited = true;
        }

        public void vist() {
            visited = true;
        }
    }
    private Vertex current;
    // Track previous moves, from origin to current location.
    private Stack<Direction> path = new Stack<>();

    private boolean backtracking = false;

    public JasonsMazeWanderer() {
        current = new Vertex(Tile.STARTING_LOCATION);
        current.vist();
    }

    public Direction invert(Direction dir) {
        if (dir == Direction.SOUTH) {
            current = current.north;
            return Direction.NORTH;
        } else if (dir == Direction.NORTH) {
            current = current.south;
            return Direction.SOUTH;
        } else if (dir == Direction.EAST) {
            current = current.west;
            return Direction.WEST;
        } else if (dir == Direction.WEST) {
            current = current.east;
            return Direction.EAST;
        } else {
            return null;
        }
    }

    @Override
    public Direction move(Sensor sensor) {

        // If we are back tracking, do the back tracking.
        Direction dir = foundEnd(sensor);
        if (Objects.equals(dir, null)) {
            dir = findOpen(sensor);
            return dir;
            //  end normal pathing
        } else {
            return dir;
        }
    }

    private void mapSensorReading(Sensor sensor) {
        if(Objects.equals(current.north, null)) {
            current.north = new Vertex(sensor.getNorth());
            current.north.south = current;
        }

        if(Objects.equals(current.south, null)) {
            current.south = new Vertex(sensor.getSouth());
            current.south.north = current;
        }

        if(Objects.equals(current.west, null)) {
            current.west = new Vertex(sensor.getWest());
            current.west.east = current;
        }

        if(Objects.equals(current.east, null)) {
            current.east = new Vertex(sensor.getEast());
            current.east.west = current;
        }
    }

    private Direction foundEnd(Sensor sensor) {

        Tile south = sensor.getSouth();
        Tile east = sensor.getEast();
        Tile west = sensor.getWest();
        Tile north = sensor.getNorth();

        if(south.getSymbol() == 'E') {
            return Direction.SOUTH;
        } else if(east.getSymbol() == 'E') {
            return Direction.EAST;
        } else if(west.getSymbol() == 'E') {
            return Direction.WEST;
        } else if(north.getSymbol() == 'E') {
            return Direction.NORTH;
        } else {
            return null;
        }
    }

    private Direction findOpen(Sensor sensor) {
        mapSensorReading(sensor);
        Tile south = current.south.tile;
        Tile east = current.east.tile;
        Tile west = current.west.tile;
        Tile north = current.north.tile;

        Direction dir;
        // In priority order, S->E->W->N return the first open, passable, and not visited direction
        // Return null if no direction available.
        if (south.isPassable() && !current.south.visited) {
            current = current.south;
            current.vist();
            path.add(Direction.SOUTH);
            backtracking = false;
            dir = Direction.SOUTH;
        } else if (east.isPassable() && !current.east.visited) {
            current = current.east;
            current.vist();
            path.add(Direction.EAST);
            backtracking = false;
            dir = Direction.EAST;
        } else if (west.isPassable() && !current.west.visited) {
            current = current.west;
            current.vist();
            path.add(Direction.WEST);
            backtracking = false;
            dir = Direction.WEST;
        } else if (north.isPassable() && !current.north.visited ) {
            current = current.north;
            current.vist();
            path.add(Direction.NORTH);
            backtracking = false;
            dir = Direction.NORTH;
        } else {
            return invert(path.pop());
        }
        return dir;
    }
}
