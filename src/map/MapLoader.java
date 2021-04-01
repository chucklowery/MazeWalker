package map;

import java.io.*;


public class MapLoader {

    public GameBoard load(File file) throws IOException {
        try(FileInputStream stream = new FileInputStream(file)) {
            return load(stream);
        }
    }

    public GameBoard load(InputStream stream) throws IOException {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            return load(reader);
        }
    }

    private GameBoard load(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        if (s == null)
            return new GameBoard(new Tile[0][0]);

        String[] dimensions = s.split(" ");
        int height = Integer.parseInt(dimensions[0]);
        int width = Integer.parseInt(dimensions[1]);

        Tile[][] tiles = parseTiles(reader, height, width);

        return new GameBoard(tiles);
    }

    private Tile[][] parseTiles(BufferedReader reader, int height, int width) throws IOException {
        Tile[][] tiles = new Tile[height][width];
        char[] buffer = new char[width];
        for(int i = 0; i < height; i++) {
            reader.read(buffer);
            reader.read();
            reader.read();
            tiles[i] = convert(buffer);
        }
        return tiles;
    }

    private Tile[] convert(char[] buffer) {
        Tile[] tiles = new Tile[buffer.length];
        for (int i = 0; i < buffer.length; i++)
            tiles[i] = Tile.bySymbol(buffer[i]);
        return tiles;
    }
}
