package player;

import map.Tile;

public interface Sensor {
    Tile getNorth();

    Tile getEast();

    Tile getSouth();

    Tile getWest();
}
