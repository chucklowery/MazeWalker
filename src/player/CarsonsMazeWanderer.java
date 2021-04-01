package player;

import map.Tile;

import java.util.*;

public class CarsonsMazeWanderer implements MazeWanderer {
    private Stack<Direction> availableMoves = new Stack<>();
    private Stack<Direction> currentPath = new Stack<>();
    private LinkedList<TileData> visitedTiles = new LinkedList<>();

    private boolean initilizer = true;
    private int x = 0;
    private int y = 0;

    private Tile north;
    private Tile south;
    private Tile east;
    private Tile west;


    @Override
    public Direction move(Sensor sensor) {
        this.north = sensor.getNorth();
        this.south = sensor.getSouth();
        this.east = sensor.getEast();
        this.west = sensor.getWest();


        Direction possibleEnd = checkIfAtEnd();
        if (possibleEnd != null){
            System.out.println("This move has reached the end!");
            return possibleEnd;
        }

        if (initilizer == true){
            initilizer = false;
            TileData root = new TileData(0,0);
            visitedTiles.add(root);
        }

        if (!visitedTiles.get(visitedTiles.indexOf(new TileData(x, y))).passed)
            visitedTiles.get(visitedTiles.indexOf(new TileData(x, y))).setPassed();



        if (!findAvailableMoves()) {
            if (!noNewMoves()) {
                if (!currentPath.empty()) {
                    return getOpposite(this.currentPath.pop());
                }

            }else{
                return untouchedTiles();
            }
        }

        if (availableMoves.empty()){
            System.out.println("No more moves");
            return null;
        }

        currentPath.push(availableMoves.peek());
        incrementCurrentCoord(availableMoves.peek());
        return availableMoves.pop();
    }
    
    private boolean findAvailableMoves() {
        boolean availableMoves = false;

        if (north.isPassable() && (!visitedTiles.contains(new TileData(x, y+1)))){
            this.availableMoves.push(Direction.NORTH);
            this.visitedTiles.add(new TileData(x, y+1));
            if (!availableMoves )
                availableMoves = true;
        }

        if (south.isPassable() && (!visitedTiles.contains(new TileData(x, y-1)))){
            this.availableMoves.push(Direction.SOUTH);
            this.visitedTiles.add(new TileData(x, y-1));
            if (!availableMoves )
                availableMoves = true;
        }

        if (east.isPassable() && (!visitedTiles.contains(new TileData(x+1, y)))){
            this.availableMoves.push(Direction.EAST);
            this.visitedTiles.add(new TileData(x+1, y));
            if (!availableMoves )
                availableMoves = true;
        }

        if (west.isPassable() && (!visitedTiles.contains(new TileData(x-1, y)))){
            this.availableMoves.push(Direction.WEST);
            this.visitedTiles.add(new TileData(x-1, y));
            if (!availableMoves)
                availableMoves = true;
        }

        return availableMoves;
    }

    private boolean noNewMoves(){
        boolean determineIfStillBacktracking = false;

        if (north.isPassable() && (!visitedTiles.get(visitedTiles.indexOf(new TileData(x, y+1))).passed))
            determineIfStillBacktracking = true;

        if (south.isPassable() && (!visitedTiles.get(visitedTiles.indexOf(new TileData(x, y-1))).passed))
            determineIfStillBacktracking = true;

        if (east.isPassable() && (!visitedTiles.get(visitedTiles.indexOf(new TileData(x+1, y))).passed))
            determineIfStillBacktracking = true;

        if (west.isPassable() && (!visitedTiles.get(visitedTiles.indexOf(new TileData(x-1, y))).passed))
            determineIfStillBacktracking = true;


        return determineIfStillBacktracking;
    }

    private Direction getOpposite(Direction lastDirection) {
        if (lastDirection.equals(Direction.NORTH)) {
            y--;
            return Direction.SOUTH;
        }

        if (lastDirection.equals(Direction.SOUTH)){
            y++;
            return Direction.NORTH;
        }
        if (lastDirection.equals(Direction.EAST)) {
            x--;
            return Direction.WEST;
        }
        if (lastDirection.equals(Direction.WEST)) {
            x++;
            return Direction.EAST;
        }
        return null;
    }

    private void incrementCurrentCoord(Direction nextMove){
        if (nextMove.equals(Direction.NORTH))
            this.y ++;
        else if (nextMove.equals(Direction.SOUTH))
            this.y--;
        else if (nextMove.equals(Direction.EAST))
            this.x++;
        else if (nextMove.equals(Direction.WEST))
            this.x--;
    }

    private Direction checkIfAtEnd(){
        if (this.north.getSymbol() == 'E')
            return Direction.NORTH;
        if (this.south.getSymbol() == 'E')
            return Direction.SOUTH;
        if (this.east.getSymbol() == 'E')
            return Direction.EAST;
        if (this.west.getSymbol() == 'E')
            return Direction.WEST;
        return null;
    }

    private Direction untouchedTiles(){
        if (north.isPassable() && (!visitedTiles.get(visitedTiles.indexOf(new TileData(x, y+1))).passed)) {
            this.currentPath.push(Direction.NORTH);
            y++;
            return (Direction.NORTH);
        }
        if (south.isPassable() && (!visitedTiles.get(visitedTiles.indexOf(new TileData(x, y-1))).passed)){
            this.currentPath.push(Direction.SOUTH);
            y--;
            return (Direction.SOUTH);
        }

        if (east.isPassable() && (!visitedTiles.get(visitedTiles.indexOf(new TileData(x+1, y))).passed)) {
            this.currentPath.push(Direction.EAST);
            x++;
            return (Direction.EAST);
        }

        if (west.isPassable() && (!visitedTiles.get(visitedTiles.indexOf(new TileData(x-1, y))).passed)) {
            this.currentPath.push(Direction.WEST);
            x--;
            return (Direction.WEST);
        }
        return null;
    }


    /////////////////////////////////////////////////// Inner Class
    private class TileData{
       public int x;
       public int y;
       public boolean passed = false;

       TileData(int x, int y){
           this.x = x;
           this.y = y;
       }

       public int getX(){
           return this.x;
       }

       public int getY(){
           return  this.y;
       }

       public boolean getPassed(){
           return this.passed;
       }

       public void setPassed(){
           this.passed = true;
       }

        @Override
        public boolean equals(Object obj) {
            if (((TileData)obj).x == this.x && ((TileData)obj).y == this.y)
                return true;
            return false;
        }
    }



}