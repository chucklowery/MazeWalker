package sample;

import gui.Maze;
import javafx.application.Application;

public class Main {

    public static void main(String[] args) {
        //Maze fileExplorer = new Maze(new GameBoard(new Tile[0][0]));

        Application.launch(Maze.class, args);
    }
}
