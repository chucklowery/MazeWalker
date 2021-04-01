package player;

import map.Tile;

import java.util.*;

public class RuthsMazeWanderer implements MazeWanderer {
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
            List<Location> destinations = map.findUnexploredTilesWithAdjacentPassableNeighbors(current);
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
                if (directions == null || destinations.isEmpty()) {
                    directions = map.findDirectionsToEnd(current);
                }
                System.out.println(directions);
            }
        }

        if (directions == null || directions.isEmpty()) {
            directions = map.findDirectionsToLastPassableTileUnexplored(current);
        }
        if(directions == null || directions.isEmpty() ) {
            directions = new LinkedList<>();
            if(sensor.getNorth().isPassable())
                directions.add(Direction.NORTH);
            if(sensor.getEast().isPassable())
                directions.add(Direction.EAST);
            if(sensor.getSouth().isPassable())
                directions.add(Direction.SOUTH);
            if(sensor.getWest().isPassable())
                directions.add(Direction.WEST);
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
                        Location e = new Location(x, y);
                            unexplored.add(e);
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

        List<Location> findUnexploredTilesWithAdjacentPassableNeighbors(Location current) {
            List<Location> unknowns = unexploredTiles();
            //unknowns.removeIf(unknown -> !hasPassableNeighbors(unknown));
            unknowns.removeIf(unknown -> !current.isAjacent(unknown));
            return unknowns;

        }

//        List<Direction> findPath(Location start, Location end) {
//            return findPath(start, end);
//        }

         LinkedList<Direction> findPath(Location start, Location end) {
            if(start.equals(end))

            {
                return new LinkedList<>();
            }
            if(start.isAjacent(end)&&graph.get(end)==null)

            {
                return new LinkedList<>();
            }
            LinkedList<Direction> path = new LinkedList<>();
            HashMap<Location, Location> predecessor = new HashMap<>();
            HashMap<Location, Integer> distances = new HashMap<>();
            Set<Location> locations = graph.keySet();
            PriorityQueue<Weighted> pq = new PriorityQueue<>();
            boolean pathFound = false;
            for (Location location : locations) {
                if (location.equals(start)) {
                    distances.put(location, 0);
                    predecessor.put(location, location);
                }
                distances.put(location, 9999);
                predecessor.put(location, null);
            }
            pq.add(new Weighted(start, 0));
            while (!pathFound && !pq.isEmpty()) {
                Weighted current = pq.poll();
                if (current.location.equals(end)) {
                    pathFound = true;
                } else if(current.weight <= distances.getOrDefault(current.location, Integer.MAX_VALUE)) {
                    for (Location location : findPassableNeighbors(current.location)) {
                        int currentDistance = current.weight + 1;
                        if (distances.get(location) > currentDistance) {
                            predecessor.put(location, current.location);
                            distances.put(location, currentDistance);
                            pq.add(new Weighted(location, currentDistance));
                        }

                    }
                }
            }
            if(predecessor.get(end) == null) {
                return null;
            }

            LinkedList<Location> result = new LinkedList<>();
            Location iterator = end;
            while (iterator != null && !iterator.equals(start)) {
                result.addFirst(iterator);
                iterator = predecessor.get(iterator);
            }
            result.addFirst(start);


           for (int i = 1; i < result.size(); i++){
               path.add(result.get(i-1).getDirection(result.get(i)));
           }
           return path;


//            if(start.equals(end))
//
//        {
//            return new LinkedList<>();
//        }
//            if(start.isAjacent(end)&&graph.get(end)==null)
//
//        {
//            return new LinkedList<>();
//        }

//            visited.add(start);
//            LinkedList<Direction> bestPath = null;
//            for (Location location : findPassableNeighbors(start)) {
//                if (visited.contains(location)) {
//                    continue;
//                }
//                LinkedList<Direction> subPath = findPath(location, end, visited);
//                if (bestPath == null || (subPath != null && bestPath.size() > subPath.size())) {
//                    if (subPath != null) {
//                        bestPath = subPath;
//                        bestPath.addFirst(start.getDirection(location));
//                    }
//                } else {
//                    visited.remove(location);
//                }
//            }
//            return bestPath;


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

    static class Weighted implements Comparable<Weighted> {
        Location location;
        int weight;

        Weighted(Location location, int weight) {
            this.location = location;
            this.weight = weight;
        }

        @Override
        public int compareTo(Weighted o) {
            return Integer.compare(o.weight, weight) * -1;
        }
    }

}
