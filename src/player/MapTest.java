//package player;
//
//import map.Tile;
//import org.hamcrest.core.Is;
//import org.junit.Assert;
//import org.junit.Test;
//import player.ChucksPlayer.Cell;
//
//import static org.hamcrest.core.Is.is;
//import static org.junit.Assert.*;
//import static player.ChucksPlayer.Map.shiftRight;
//
//public class MapTest {
//
//    @Test
//    public void test() {
//
//        ChucksPlayer.Map map = new ChucksPlayer.Map();
//
//        map.mark(Direction.NORTH, Tile.LAND);
//        map.move(Direction.NORTH);
//        map.mark(Direction.NORTH, Tile.LAND);
//        map.move(Direction.NORTH);
//        map.mark(Direction.NORTH, Tile.LAND);
//        map.move(Direction.NORTH);
//        map.mark(Direction.NORTH, Tile.LAND);
//        map.move(Direction.NORTH);
//        map.mark(Direction.NORTH, Tile.WATER);
//        map.move(Direction.NORTH);
//        map.mark(Direction.NORTH, Tile.WATER);
//        map.move(Direction.NORTH);
//        map.mark(Direction.NORTH, Tile.WATER);
//        map.move(Direction.NORTH);
//        map.mark(Direction.NORTH, Tile.WATER);
//        map.move(Direction.NORTH);
//        map.mark(Direction.NORTH, Tile.WATER);
//        map.move(Direction.NORTH);
//        map.mark(Direction.NORTH, Tile.WATER);
//        map.move(Direction.NORTH);
//        map.mark(Direction.NORTH, Tile.WATER);
//        map.move(Direction.NORTH);
//        map.mark(Direction.NORTH, Tile.WATER);
//        map.move(Direction.NORTH);
//        System.out.println(map.toString());
//
//    }
//
//    @Test
//    public void testShiftRight() {
//        Cell cell = new Cell(Tile.LAND);
//
//        Cell[] result = shiftRight(new Cell[]{cell}, 1);
//        Assert.assertThat(result, is(new Cell[]{null, cell}));
//    }
//
//}