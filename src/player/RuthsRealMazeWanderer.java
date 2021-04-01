package player;

import map.Tile;

import java.util.*;

public class RuthsRealMazeWanderer implements MazeWanderer {
    Grid map = new Grid();
    Location current = new Location(0, 0);
    List<Direction> directions = Collections.emptyList();

    @Override
    public Direction move(Sensor sensor) {
        map.add(sensor.getNorth(), current.north());
        map.add(sensor.getEast(), current.east());
        map.add(sensor.getSouth(), current.south());
        map.add(sensor.getWest(), current.west());

        if (directions.isEmpty()) {
            List<Location> destinations = map.findUnexploredTilesWithAdjacentPassableNeighbors();
            if (destinations.isEmpty()) {
                directions = map.findDirectionsToEnd(current);
                if (directions == null) {
                    directions = map.findDirectionsToLastPassableTileUnexplored(current);
                }
            } else {
                System.out.println(current);
                System.out.println("GoTo");
                System.out.println(destinations.get(0));
                directions = map.findPath(current, destinations.get(0));
                if (directions == null) {
                    directions = map.findDirectionsToEnd(current);
                }
                System.out.println(directions);
            }
        }

        if (directions == null) {
            directions = map.findDirectionsToLastPassableTileUnexplored(current);
        }
        Direction direction = directions.remove(0);

        switch (direction) {
            case NORTH:
                current = current.north();
                break;
            case EAST:
                current = current.east();
                break;
            case SOUTH:
                current = current.south();
                break;
            case WEST:
                current = current.west();
                break;
        }

        return direction;
    }

    static class Grid {

        HashMap<Location, Tile> graph = new HashMap<>();

        Grid() {
            graph.put(new Location(0, 0), Tile.STARTING_LOCATION);
        }

        void add(Tile tile, Location location) {
            graph.put(location, tile);
        }

        Location findTopLeftCorner() {
            int minx = 0;
            int maxy = 0;
            for (Location location : graph.keySet()) {
                if (location.getX() < minx) {
                    minx = location.x;
                }
                if (location.getY() > maxy) {
                    maxy = location.y;
                }
            }
            return new Location(minx, maxy);

        }

        Location findBottomRightCorner() {
            int maxx = 0;
            int miny = 0;
            for (Location location : graph.keySet()) {
                if (location.getX() > maxx) {
                    maxx = location.x;
                }
                if (location.getY() < miny) {
                    miny = location.y;
                }
            }
            return new Location(maxx, miny);
        }

        List<Location> unexploredTiles() {
            ArrayList<Location> unexplored = new ArrayList<>();
            Location topLeftCorner = findTopLeftCorner();
            Location bottomRightCorner = findBottomRightCorner();
            for (int y = topLeftCorner.getY(); y >= bottomRightCorner.getY(); y--) {
                for (int x = topLeftCorner.getX(); x <= bottomRightCorner.getX(); x++) {
                    Tile current = graph.get(new Location(x, y));
                    if (current == null) {
                        unexplored.add(new Location(x, y));
                    }
                }
            }
            return unexplored;
        }

        List<Location> findPassableNeighbors(Location current) {
            ArrayList<Location> passable = new ArrayList<>();
            Tile north = graph.get(current.north());
            Tile south = graph.get(current.south());
            Tile east = graph.get(current.east());
            Tile west = graph.get(current.west());

            if (north != null && north.isPassable()) {
                passable.add(current.north());
            }
            if (south != null && south.isPassable()) {
                passable.add(current.south());
            }
            if (east != null && east.isPassable()) {
                passable.add(current.east());
            }
            if (west != null && west.isPassable()) {
                passable.add(current.west());
            }
            return passable;
        }

        boolean hasPassableNeighbors(Location current) {
            return !findPassableNeighbors(current).isEmpty();
        }

        List<Location> findUnexploredTilesWithAdjacentPassableNeighbors() {
            List<Location> unknowns = unexploredTiles();
            unknowns.removeIf(unknown -> !hasPassableNeighbors(unknown));
            return unknowns;

        }

        List<Direction> findPath(Location start, Location end) {
            return findPath(start, end, new HashSet<>());
        }

        private LinkedList<Direction> findPath(Location start, Location end, Set<Location> visited) {
            if (start.equals(end)) {
                return new LinkedList<>();
            }
            if (start.isAjacent(end) && graph.get(end) == null) {
                return new LinkedList<>();
            }

            visited.add(start);
            LinkedList<Direction> bestPath = null;
            for (Location location : findPassableNeighbors(start)) {
                if (visited.contains(location)) {
                    continue;
                }
                LinkedList<Direction> subPath = findPath(location, end, visited);
                if (bestPath == null || (subPath != null && bestPath.size() >= subPath.size())) {
                    if (subPath != null) {
                        bestPath = subPath;
                        bestPath.addFirst(start.getDirection(location));
                    }
                } else {
                    visited.remove(location);
                }
            }
            return bestPath;


        }

        @Override
        public String toString() {
            String gridRepresentation = "";
            Location topLeftCorner = findTopLeftCorner();
            Location bottomRightCorner = findBottomRightCorner();
            for (int y = topLeftCorner.getY(); y >= bottomRightCorner.getY(); y--) {
                for (int x = topLeftCorner.getX(); x <= bottomRightCorner.getX(); x++) {
                    Tile current = graph.get(new Location(x, y));
                    if (current == null) {
                        gridRepresentation += "U";
                    } else {
                        gridRepresentation += current.getSymbol();
                    }
                }
                gridRepresentation += "\n";
            }
            return gridRepresentation.trim();
        }

        public List<Direction> findDirectionsToEnd(Location start) {
            Location topLeftCorner = findTopLeftCorner();
            Location bottomRightCorner = findBottomRightCorner();
            for (int y = topLeftCorner.getY(); y >= bottomRightCorner.getY(); y--) {
                for (int x = topLeftCorner.getX(); x <= bottomRightCorner.getX(); x++) {
                    Tile current = graph.get(new Location(x, y));
                    if (current != null && current.equals(Tile.ENDING_LOCATION)) {
                        return findPath(start, new Location(x, y));
                    }
                }
            }
            return null;
        }

        public List<Direction> findDirectionsToLastPassableTileUnexplored(Location start) {
            Location topLeftCorner = findTopLeftCorner();
            Location bottomRightCorner = findBottomRightCorner();
            for (int i = topLeftCorner.getX(); i <= bottomRightCorner.getX(); i++) {
                Tile current = graph.get(new Location(i, topLeftCorner.getY()));
                if (current != null && current.equals(Tile.LAND)) {
                    return findPath(start, new Location(i, topLeftCorner.getY()));
                }
            }
            for (int i = topLeftCorner.getX(); i <= bottomRightCorner.getX(); i++) {
                Tile current = graph.get(new Location(i, bottomRightCorner.getY()));
                if (current != null && current.isPassable()) {
                    return findPath(start, new Location(i, bottomRightCorner.getY()));
                }
            }
            for (int j = topLeftCorner.getY(); j >= bottomRightCorner.getY(); j--) {
                Tile current = graph.get(new Location(topLeftCorner.getX(), j));
                if (current != null && current.equals(Tile.LAND)) {
                    return findPath(start, new Location(topLeftCorner.getX(), j));
                }
            }
            for (int j = topLeftCorner.getY(); j >= bottomRightCorner.getY(); j--) {
                Tile current = graph.get(new Location(bottomRightCorner.getX(), j));
                if (current != null && current.equals(Tile.LAND)) {
                    return findPath(start, new Location(bottomRightCorner.getX(), j));
                }

            }
            return null;
        }

    }

    static class Location {
        int x;
        int y;

        Location(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        Direction getDirection(Location b) {
            if (x > b.getX()) {
                return Direction.WEST;
            }
            if (x < b.getX()) {
                return Direction.EAST;
            }
            if (y > b.getY()) {
                return Direction.SOUTH;
            }
            if (y < b.getY()) {
                return Direction.NORTH;
            }
            return null;
        }

        Location north() {
            return new Location(x, y + 1);
        }

        Location south() {
            return new Location(x, y - 1);
        }

        Location east() {
            return new Location(x + 1, y);
        }

        Location west() {
            return new Location(x - 1, y);
        }

        boolean isAjacent(Location loc) {
            return Math.sqrt(Math.pow(x - loc.getX(), 2) + Math.pow(y - loc.getY(), 2)) < 1.1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Location location = (Location) o;

            if (x != location.x) return false;
            return y == location.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public String toString() {
            return "Location{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }


}
