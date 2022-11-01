package map;

public enum Tile {
    WATER('W', false),
    LAND('=', true),
    STARTING_LOCATION('S', true),
    ENDING_LOCATION('E', true);
    private char symbol;

    private boolean passable;

    Tile(char symbol, boolean passable) {
        this.symbol = symbol;
        this.passable = passable;
    }

    public static Tile bySymbol(char symbol) {
        for (Tile title : Tile.values())
            if (title.symbol == symbol)
                return title;
        return null;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean isPassable() {
        return passable;
    }

    public boolean isGoal() {
        return this.equals(ENDING_LOCATION);
    }
}
