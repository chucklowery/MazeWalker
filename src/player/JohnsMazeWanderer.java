package player;

import java.util.Stack;

/**
 * Created by johnr on 12/3/2017.
 */
public class JohnsMazeWanderer implements MazeWanderer {
    Stack<Direction> moveOpposites = new Stack<>();
    Maze maze = new Maze();

    public Direction move(Sensor sensor) {
        if (sensor.getNorth().isPassable() && !maze.checkNorth()) {
            moveOpposites.push(Direction.SOUTH);
            maze.moveNorth();
            return Direction.NORTH;
        } else if (sensor.getEast().isPassable() && !maze.checkEast()) {
            moveOpposites.push(Direction.WEST);
            maze.moveEast();
            return Direction.EAST;
        } else if (sensor.getWest().isPassable() && !maze.checkWest()) {
            moveOpposites.push(Direction.EAST);
            maze.moveWest();
            return Direction.WEST;
        } else if (sensor.getSouth().isPassable() && !maze.checkSouth()) {
            moveOpposites.push(Direction.NORTH);
            maze.moveSouth();
            return Direction.SOUTH;
        } else {
            Direction toReturn = moveOpposites.pop();
            switch (toReturn) {
                case NORTH:
                    maze.moveNorth();
                    break;
                case SOUTH:
                    maze.moveSouth();
                    break;
                case EAST:
                    maze.moveEast();
                    break;
                case WEST:
                    maze.moveWest();
                    break;
            }
            return toReturn;
        }
    }

    private class Maze {
        boolean[][] map = new boolean[20][20];
        int currentX = 10;
        int currentY = 10;
        int currentHeight = 20;
        int currentWidth = 20;

        public void Maze() {
            for(boolean[] row : map) {
                for(boolean tile : row) {
                    tile = false;
                }
            }
            map[currentY][currentX] = true;
        }

        public boolean checkNorth() {
            if (currentY - 1 < 0)
                growNorth();
            return map[currentY - 1][currentX];
        }

        public boolean checkSouth() {
            if (currentY + 1 >= currentHeight)
                growSouth();
            return map[currentY + 1][currentX];
        }

        public boolean checkEast() {
            if (currentX + 1 >= currentWidth)
                growEast();
            return map[currentY][currentX + 1];
        }

        public boolean checkWest() {
            if (currentX - 1 < 0)
                growWest();
            return map[currentY][currentX - 1];
        }

        public void growNorth() {
            boolean[][] newMap = new boolean[currentHeight + 10][currentWidth];
            int i = 10;
            for(boolean[] row : map) {
                newMap[i] = row;
                i++;
            }
            currentHeight += 10;
            currentY = 10;
            map = newMap;
        }

        public void growSouth() {
            boolean[][] newMap = new boolean[currentHeight + 10][currentWidth];
            int i = 0;
            for (boolean[] row : map) {
                newMap[i] = row;
                i++;
            }
            currentHeight += 10;
            map = newMap;
        }

        public void growEast() {
            boolean[][] newMap = new boolean[currentHeight][currentWidth + 10];
            for (int i = 0; i < currentHeight; i++) {
                for (int j = 0; j < currentWidth; j++) {
                    newMap[i][j] = map[i][j];
                }
            }
            currentWidth += 10;
            map = newMap;
        }

        public void growWest() {
            boolean[][] newMap = new boolean[currentHeight][currentWidth + 10];
            for (int i = 0; i < currentHeight; i++) {
                for (int j = 10; j < currentWidth + 10; j++) {
                    newMap[i][j] = map[i][j - 10];
                }
            }
            currentWidth += 10;
            currentX = 10;
            map = newMap;
        }

        public void moveNorth() {
            currentY--;
            map[currentY][currentX] = true;
        }

        public void moveSouth() {
            currentY++;
            map[currentY][currentX] = true;
        }

        public void moveEast() {
            currentX++;
            map[currentY][currentX] = true;
        }

        public void moveWest() {
            currentX--;
            map[currentY][currentX] = true;
        }
    }

}
