package player;

import map.Tile;

public class Environment implements Sensor {
    public Tile north;
    public Tile east;
    public Tile south;
    public Tile west;

    @Override
    public Tile getNorth() {
        return north;
    }

    @Override
    public Tile getEast() {
        return east;
    }

    @Override
    public Tile getSouth() {
        return south;
    }

    @Override
    public Tile getWest() {
        return west;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "north=" + north +
                ", east=" + east +
                ", south=" + south +
                ", west=" + west +
                '}';
    }
}
