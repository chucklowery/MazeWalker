package map;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MapLoader {

    public GameBoard load(File file){
        try(FileInputStream stream = new FileInputStream(file)) {
            return load(stream);
        } catch (IOException io) {
            throw new IllegalStateException("Unable to load game board " + file.getName());
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
            return new GameBoard(new Tile[0][0], "Unknown");

        Pattern pattern = Pattern.compile("([0-9]+) ([0-9]+)(.+)?");
        Matcher matcher = pattern.matcher(s);
        int height = 0;
        int width = 0;
        String title = "Unknown";
        if(matcher.matches()) {
            height = Integer.parseInt(matcher.group(1));
            width = Integer.parseInt(matcher.group(2));
            title = matcher.group(3).trim();
        }

        Tile[][] tiles = parseTiles(reader, height, width);

        return new GameBoard(tiles, title);
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
