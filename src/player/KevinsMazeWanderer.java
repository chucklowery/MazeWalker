package player;

import map.Tile;

public class KevinsMazeWanderer implements MazeWanderer {

    int x = 1000, y = 1000;
    private MyTile[][] bigMap;

    public KevinsMazeWanderer() {
        this.bigMap = new MyTile[x * 2][y * 2];
        bigMap[x][y] = new MyTile(Tile.STARTING_LOCATION);
        bigMap[x][y].setVisited(true);
        bigMap[x][y].setExplored(false);
    }

    @Override
    public Direction move(Sensor sensor) {

        // build KB from sensor
        expandKB(sensor);

        if (explored()) {
            // so now way to expand from here. time to go back
            // but mark this tile as explored first
            bigMap[x][y].setExplored(true);

            // now go back to one of visited tiles
            // if no such visited tiles, return null as direction;
            Direction direction = getAnyVisitedUnExploredTile();
            return direction;
        }

        // current tile is not fully explored
        Direction direction = getAnyUnVisitedTile();
        return direction;
    }

    private Direction getAnyVisitedUnExploredTile() {

        //var list = new ArrayList<Direction>();

        if (bigMap[x - 1][y].isPassable() && !bigMap[x - 1][y].isExplored()) {
            --x;
//            list.add(Direction.NORTH);
            return Direction.NORTH;
        }

        if (bigMap[x + 1][y].isPassable() && !bigMap[x + 1][y].isExplored()) {
            ++x;
//            list.add(Direction.SOUTH);
            return Direction.SOUTH;
        }

        if (bigMap[x][y + 1].isPassable() && !bigMap[x][y + 1].isExplored()) {
            ++y;
//            list.add(Direction.EAST);
            return Direction.EAST;
        }

        if (bigMap[x][y - 1].isPassable() && !bigMap[x][y - 1].isExplored()) {
            --y;
//            list.add(Direction.WEST);
            return Direction.WEST;
        }

/*        if (list.isEmpty())
            return null;

        int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());

        Direction direction = list.get(0);
        updateRobotLocation(direction);

        return direction;*/

        return null;
    }

    private void updateRobotLocation(Direction direction) {
        if (direction == Direction.NORTH) --x;
        else if (direction == Direction.SOUTH) ++x;
        else if (direction == Direction.EAST) ++y;
        else if (direction == Direction.WEST) --y;
    }

    private Direction getAnyUnVisitedTile() {

        //var list = new ArrayList<Direction>();

        if (bigMap[x - 1][y].isPassable() && bigMap[x - 1][y].isUnvisited()) {
            bigMap[x - 1][y].setVisited(true);
            --x;
//            list.add(Direction.NORTH);
            return Direction.NORTH;
        }

        if (bigMap[x + 1][y].isPassable() && bigMap[x + 1][y].isUnvisited()) {
            bigMap[x + 1][y].setVisited(true);
            ++x;
//            list.add(Direction.SOUTH);
            return Direction.SOUTH;
        }

        if (bigMap[x][y + 1].isPassable() && bigMap[x][y + 1].isUnvisited()) {
            bigMap[x][y + 1].setVisited(true);
            ++y;
//            list.add(Direction.EAST);
            return Direction.EAST;
        }

        if (bigMap[x][y - 1].isPassable() && bigMap[x][y - 1].isUnvisited()) {
            bigMap[x][y - 1].setVisited(true);
            --y;
//            list.add(Direction.WEST);
            return Direction.WEST;
        }

        /*if (list.isEmpty())
            return null;

        int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());

        Direction direction = list.get(0);
        updateRobotLocation(direction);

        return direction;*/

        return null;
    }

    private boolean explored() {

        if (bigMap[x - 1][y].isPassable() && bigMap[x - 1][y].isUnvisited()) {
            return false;
        }

        if (bigMap[x + 1][y].isPassable() && bigMap[x + 1][y].isUnvisited()) {
            return false;
        }

        if (bigMap[x][y + 1].isPassable() && bigMap[x][y + 1].isUnvisited()) {
            return false;
        }

        if (bigMap[x][y - 1].isPassable() && bigMap[x][y - 1].isUnvisited()) {
            return false;
        }

        return true;
    }

    private void expandKB(Sensor sensor) {
        if (bigMap[x - 1][y] == null) bigMap[x - 1][y] = new MyTile(sensor.getNorth());
        if (bigMap[x + 1][y] == null) bigMap[x + 1][y] = new MyTile(sensor.getSouth());
        if (bigMap[x][y + 1] == null) bigMap[x][y + 1] = new MyTile(sensor.getEast());
        if (bigMap[x][y - 1] == null) bigMap[x][y - 1] = new MyTile(sensor.getWest());
    }

    static class MyTile {

        private Tile tile;
        private boolean visited;
        private boolean explored;

        public MyTile(Tile tile) {
            this.tile = tile;
            this.visited = false;
            this.explored = false;
        }

        public Tile getTile() {
            return tile;
        }

        public void setTile(Tile tile) {
            this.tile = tile;
        }

        public boolean isUnvisited() {
            return !visited;
        }

        public void setVisited(boolean visited) {
            this.visited = visited;
        }

        public boolean isPassable() {
            return tile.isPassable();
        }

        public boolean isExplored() {
            return explored;
        }

        public void setExplored(boolean explored) {
            this.explored = explored;
        }
    }
}
