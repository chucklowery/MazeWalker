package map;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class MapLoaderTest {

    @Test
    public void testLoad_givenEmptyFile_expect_emptyResult() throws IOException {
        ByteArrayInputStream rawMap = new ByteArrayInputStream("".getBytes());
        GameBoard board = new MapLoader().load(rawMap);

        assertThat(board.getHeight(), is(0));
        assertThat(board.getWidth(), is(0));
    }

    @Test
    public void testLoad_givenOneByOneMapOfLandExpectLand_expect_oneByOneArrayLand() throws IOException {
        ByteArrayInputStream rawMap = new ByteArrayInputStream("1 1\n=\n".getBytes());
        GameBoard board = new MapLoader().load(rawMap);

        assertThat(board.getHeight(), is(1));
        assertThat(board.getWidth(), is(1));

        assertThat( board.getTile(0,0), is(Tile.LAND));
    }

    @Test
    public void testLoad_givenSimpleMapContainingAllTiles_expect_TileSetLoaded() throws IOException {
        ByteArrayInputStream rawMap = new ByteArrayInputStream(("5 4\n" +
                "WWWW\n" +
                "W=SW\n" +
                "W=WW\n" +
                "W=EW\n" +
                "WWWW\n").getBytes());
        GameBoard board = new MapLoader().load(rawMap);

        assertThat(board.getHeight(), is(5));
        assertThat(board.getWidth(), is(4));

        assertThat( board.getTile(0,0), is(Tile.WATER));
        assertThat( board.getTile(1,2), is(Tile.STARTING_LOCATION));
        assertThat( board.getTile(3,2), is(Tile.ENDING_LOCATION));
    }
}