package player;

import map.Tile;

import java.util.*;

public class ParkerSmithsMazeWanderer implements  MazeWanderer{
    public Stack<Direction> availableMoves = new Stack<>();
    public Stack<Direction> previousMoves = new Stack<>();
    public CustomArray visitedTiles = new CustomArray();

    public int currentWandererX = 0;
    public int currentWandererY = 1;

    public static class CustomArray {
        CoordinatedTile[] coordinates = new CoordinatedTile[1024];
        public int size = 0;

        public void add(CoordinatedTile coordinatedTile) {
            if (this.size + 1 > coordinates.length) {
                CoordinatedTile[] newArray = new CoordinatedTile[coordinates.length * 2];
                for (int i = 0; i < coordinates.length; i++) {
                    newArray[i] = coordinates[i];
                }
            }
            coordinates[size] = coordinatedTile;
            size++;
        }

        public boolean contains(CoordinatedTile coordinatedTile) {
            for (int i = 0; i < coordinates.length; i++) {
                if (coordinates[i] != null) {
                    if (coordinates[i].xCoordinate == coordinatedTile.xCoordinate && coordinates[i].yCoordinate == coordinatedTile.yCoordinate) {
                        return true;
                    }
                }
            }
            return false;
        }

    }

    public static class CoordinatedTile {
        public int xCoordinate = -50;
        public int yCoordinate = -50;

        public CoordinatedTile() {
        }

        public CoordinatedTile(int x, int y) {
            this.xCoordinate = x;
            this.yCoordinate = y;
        }
    }

    public Direction getOpposite(Direction direction) {
        if (direction == Direction.NORTH) {
            currentWandererY++;
            return Direction.SOUTH;
        }
        else if (direction == Direction.EAST) {
            currentWandererX--;
            return direction.WEST;
        }
        else if (direction == Direction.SOUTH) {
            currentWandererY--;
            return direction.NORTH;
        }
        else if(direction == Direction.WEST) {
            currentWandererX++;
            return direction.EAST;
        }
        return null;
    }

    public Direction move(Sensor sensor) {
        System.out.println("Current X: " + currentWandererX);
        System.out.println("Current Y: " + currentWandererY);

        //update current adjacent tiles
        Tile currentNorthTile = sensor.getNorth();
        Tile currentEastTile = sensor.getEast();
        Tile currentSouthTile = sensor.getSouth();
        Tile currentWestTile = sensor.getWest();

        //check to see if the end is adjacent
        if (currentNorthTile.getSymbol() == 'E') {
            System.out.println("I have arrived at my destination");
            return Direction.NORTH;
        }
        else if (currentEastTile.getSymbol() == 'E') {
            System.out.println("I have arrived at my destination");
            return Direction.EAST;
        }
        else if (currentSouthTile.getSymbol() == 'E') {
            System.out.println("I have arrived at my destination");
            return Direction.SOUTH;
        }
        else if (currentWestTile.getSymbol() == 'E') {
            System.out.println("I have arrived at my destination");
            return Direction.WEST;
        }

        //if the end is not adjacent, use W -> S -> E -> N priority to choose the next move from available options. (Must be passable and untouched)
        if (currentNorthTile.isPassable() && visitedTiles.contains(new CoordinatedTile(currentWandererX, currentWandererY - 1)) == false) {
            this.availableMoves.push(Direction.NORTH);
        }
        if (currentEastTile.isPassable() && visitedTiles.contains(new CoordinatedTile(currentWandererX + 1, currentWandererY)) == false) {
            this.availableMoves.push(Direction.EAST);
        }
        if (currentSouthTile.isPassable() && visitedTiles.contains(new CoordinatedTile(currentWandererX, currentWandererY + 1)) == false) {
            this.availableMoves.push(Direction.SOUTH);
        }
        if (currentWestTile.isPassable() && visitedTiles.contains(new CoordinatedTile(currentWandererX - 1, currentWandererY)) == false) {
            this.availableMoves.push(Direction.WEST);
        }

        if (this.availableMoves.size() == 0) {
            //dead end, must backtrack until we can "find a child we haven't touched" - John 2017
            return getOpposite(this.previousMoves.pop());
        }
        else {
            //pop top priority move, clear the rest.
            Direction returnDirection = availableMoves.pop();
            availableMoves.clear();

            if (returnDirection == Direction.NORTH) {
                //add coordinate to visited list and update wanderer coordinate
                visitedTiles.add(new CoordinatedTile(currentWandererX, currentWandererY - 1));
                currentWandererY--;

                //add direction to move history
                this.previousMoves.push(returnDirection);

                return returnDirection;
                //rinse and repeat.
            }
            else if (returnDirection == Direction.EAST) {
                visitedTiles.add(new CoordinatedTile(currentWandererX + 1, currentWandererY));
                currentWandererX++;

                this.previousMoves.push(returnDirection);

                return returnDirection;
            }
            else if (returnDirection == Direction.SOUTH) {
                visitedTiles.add(new CoordinatedTile(currentWandererX, currentWandererY + 1));
                currentWandererY++;

                this.previousMoves.push(returnDirection);

                return returnDirection;
            }
            else if (returnDirection == Direction.WEST) {
                visitedTiles.add(new CoordinatedTile(currentWandererX - 1, currentWandererY));
                currentWandererX--;

                this.previousMoves.push(returnDirection);

                return returnDirection;
            }
        }
        //return null to make the compiler happy.
        System.out.println("Hey this shouldn't have happened");
        return null;
    }

}
