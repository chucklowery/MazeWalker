package player;

import map.Tile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class Robot implements MazeWanderer {
    public List<List<Integer>> impassible = new ArrayList<>();
    public List<List<Integer>> pointsVisited = new ArrayList<>();
    private Tile currentTile = Tile.STARTING_LOCATION;
    public List<Integer> currentLocation = new ArrayList<>();
    public List<Integer> endingLocation = new ArrayList<>();
    private int backsteps = 0;

    public Robot() {
        currentLocation.add(2);
        currentLocation.add(2);
    }

    @Override
    public Direction move(Sensor sensor) {
//        if (currentTile != Tile.ENDING_LOCATION) {
//            System.out.println("Charging Station Found!");
//            endingLocation.add(currentLocation.get(0));
//            endingLocation.add(currentLocation.get(1));
//            return Direction.WEST;
//        } else {
            if (!checkEast(sensor)) {
                if (!checkSouth(sensor)) {
                    if (!checkNorth(sensor)) {
                        if (!checkWest(sensor)) {
                            return backtrack(sensor, pointsVisited.size() - 1 - backsteps);
                        } else {
                            pointsVisited.add(moveWest(sensor));
                        }
                    } else {
                        pointsVisited.add(moveNorth(sensor));
                    }
                } else {
                    pointsVisited.add(moveSouth(sensor));
                }
            } else {
                pointsVisited.add(moveEast(sensor));
            }
            System.out.println(currentLocation.toString());
            backsteps = 0;
//        }
        return move(sensor);
    }

    public Direction backtrack(Sensor sensor, int size) {
        backsteps++;
        List<Integer> array = new ArrayList<>();
        array.add(currentLocation.get(0));
        array.add(currentLocation.get(1));

        impassible.add(array);

        currentLocation.set(0, pointsVisited.get(size).get(0));
        currentLocation.set(1, pointsVisited.get(size).get(1));

        return move(sensor);
    }

    public boolean checkEast(Sensor sensor) {
        List<Integer> tempCoord = new ArrayList<>();
        try {
            tempCoord.add(currentLocation.get(0));
            tempCoord.add(currentLocation.get(1) + 1);
            if (pointsVisited.contains(tempCoord) || !sensor.getEast().isPassable() || impassible.contains(tempCoord)) {
                if (!impassible.contains(tempCoord))
                    impassible.add(tempCoord);
                return false;
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
    public boolean checkNorth(Sensor sensor) {
        List<Integer> tempCoord = new ArrayList<>();
        try {
            tempCoord.add(currentLocation.get(0) - 1);
            tempCoord.add(currentLocation.get(1));
            if (pointsVisited.contains(tempCoord) || !sensor.getNorth().isPassable() || impassible.contains(tempCoord)) {
                if (!impassible.contains(tempCoord))
                    impassible.add(tempCoord);
                return false;
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
    public boolean checkSouth(Sensor sensor) {
        List<Integer> tempCoord = new ArrayList<>();
        try {
            tempCoord.add(currentLocation.get(0) + 1);
            tempCoord.add(currentLocation.get(1));
            if (pointsVisited.contains(tempCoord) || !sensor.getSouth().isPassable() || impassible.contains(tempCoord)) {
                if (!impassible.contains(tempCoord))
                    impassible.add(tempCoord);
                return false;
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
    public boolean checkWest(Sensor sensor) {
        List<Integer> tempCoord = new ArrayList<>();
        try {
            tempCoord.add(currentLocation.get(0));
            tempCoord.add(currentLocation.get(1) - 1);
            if (pointsVisited.contains(tempCoord) || !sensor.getWest().isPassable() || impassible.contains(tempCoord)) {
                if (!impassible.contains(tempCoord))
                    impassible.add(tempCoord);
                return false;
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public List<Integer> moveEast(Sensor sensor) {
        currentTile = sensor.getEast();
        List<Integer> newCoord = new ArrayList<>();
        currentLocation.set(1, currentLocation.get(1) + 1);

        newCoord.add(currentLocation.get(0));
        newCoord.add(currentLocation.get(1));
        return newCoord;
    }
    public List<Integer> moveNorth(Sensor sensor) {
        currentTile = sensor.getNorth();
        List<Integer> newCoord = new ArrayList<>();
        currentLocation.set(0, currentLocation.get(0) - 1);

        newCoord.add(currentLocation.get(0));
        newCoord.add(currentLocation.get(1));
        return newCoord;
    }
    public List<Integer> moveWest(Sensor sensor) {
        currentTile = sensor.getWest();
        List<Integer> newCoord = new ArrayList<>();
        currentLocation.set(1, currentLocation.get(1) - 1);

        newCoord.add(currentLocation.get(0));
        newCoord.add(currentLocation.get(1));
        return newCoord;
    }
    public List<Integer> moveSouth(Sensor sensor) {
        currentTile = sensor.getSouth();
        List<Integer> newCoord = new ArrayList<>();
        currentLocation.set(0, currentLocation.get(0) + 1);

        newCoord.add(currentLocation.get(0));
        newCoord.add(currentLocation.get(1));
        return newCoord;
    }
}