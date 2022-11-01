package map;

public class GameBoard {

    String title;
    Tile[][] board;
    Position player;
    Position start;
    Position end;

    public GameBoard(Tile[][] board, String title) {
        this.board = board;
        this.title = title;
        start = findFirst(board, Tile.STARTING_LOCATION);
        end = findFirst(board, Tile.ENDING_LOCATION);
    }

    private Position findFirst(Tile[][] board, Tile first) {
        for (int row = 0; row < getHeight(); row++)
            for (int column = 0; column < getWidth(); column++)
                if (board[row][column] == first)
                    return new Position(row, column);

        return null;
    }

    public String getTitle() {
        return title;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    public int getHeight() {
        return board.length;
    }

    public int getWidth() {
        if (board.length == 0)
            return 0;
        return board[0].length;
    }

    public Tile getTile(int row, int column) {
        return board[row][column];
    }

    public void setPlayerPosition(Position p) {
        player = p;
    }

    public Position getPlayerPosition() {
        return player;
    }

    public String print() {
        StringBuilder result = new StringBuilder();
        for (int row = 0; row < getHeight(); row++) {
            for (int column = 0; column < getWidth(); column++) {
                result.append(board[row][column].getSymbol());
            }
            result.append("\n");
        }
        return result.toString();
    }
}
