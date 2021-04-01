package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import map.GameBoard;
import map.MapLoader;
import map.Position;
import map.Tile;

import java.io.IOException;

public class Maze extends Application {
    private double SQUARE_SIZE = 32;

    static Color[] squareColors = new Color[Tile.values().length];
    BorderPane borderPane = new BorderPane();

    private GameBoard gameBoard;
    GridPane board = new GridPane();
    Label[][] accessCounts;
    HBox controlBar = new HBox();

    RobotController robotController;
    Button start = new Button();
    Button stop = new Button();
    Button pause = new Button();
    ComboBox mapSelection;
    ComboBox playerSelection;
    Label DEAD = new Label("X");


    static {
        squareColors[Tile.LAND.ordinal()] = Color.SANDYBROWN;
        squareColors[Tile.WATER.ordinal()] = Color.BLUE;
        squareColors[Tile.STARTING_LOCATION.ordinal()] = Color.WHITE;
        squareColors[Tile.ENDING_LOCATION.ordinal()] = Color.GREEN;
    }

    Circle player = new Circle(SQUARE_SIZE / 2 - 4, Color.BLACK);


    public void init() throws Exception {
        robotController = new RobotController(this);
        board = new GridPane();
        loadMap("maps/welcome.map");
        DEAD.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        DEAD.setTextFill(Color.RED);

    }

    private void loadMap(String name) {
        gameBoard = laodGameBoard(name);
        gameBoard.setPlayerPosition(gameBoard.getStart());

        initGUIBoard();
        if (gameBoard.getStart() != null)
            board.add(player, gameBoard.getStart().getColumn(), gameBoard.getStart().getRow());
    }

    private void initGUIBoard() {
        board.getChildren().clear();
        configureGrid(gameBoard.getHeight());

        accessCounts = new Label[gameBoard.getHeight()][gameBoard.getWidth()];
        for (int row = 0; row < gameBoard.getHeight(); row++) {
            for (int col = 0; col < gameBoard.getWidth(); col++) {
                board.add(new Rectangle(SQUARE_SIZE, SQUARE_SIZE, squareColors[gameBoard.getTile(row, col).ordinal()]), col, row);
                accessCounts[row][col] = new Label("");
                accessCounts[row][col].setTextFill(Color.DARKBLUE);
                board.add(accessCounts[row][col], col, row);
            }
        }
    }

    private GameBoard laodGameBoard(String name) {
        try {
            return new MapLoader().load(Thread.currentThread().getContextClassLoader().getResourceAsStream(name));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void configureGrid(int height) {
        for (int i = 0; i < 32; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(SQUARE_SIZE);
            rowConstraints.setPrefHeight(SQUARE_SIZE);
            rowConstraints.setMaxHeight(SQUARE_SIZE);
            rowConstraints.setValignment(VPos.CENTER);
            board.getRowConstraints().add(rowConstraints);

            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setMinWidth(SQUARE_SIZE);
            colConstraints.setMaxWidth(SQUARE_SIZE);
            colConstraints.setPrefWidth(SQUARE_SIZE);
            colConstraints.setHalignment(HPos.CENTER);
            board.getColumnConstraints().add(colConstraints);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ObservableList<String> options = FXCollections.observableArrayList("One", "Two", "Three", "Four", "Five", "Seven");
        mapSelection = new ComboBox(options);
        playerSelection = new ComboBox(FXCollections.observableArrayList(robotController.getPlayerOptions()));

        pause.setText(" Pause ");
        pause.setDisable(true);

        start.setText(" Start ");
        stop.setText(" Stop ");
        stop.setDisable(true);

        stop.setOnAction(e -> {
            start.setDisable(false);
            playerSelection.setDisable(false);
            mapSelection.setDisable(false);
            robotController.pause();
        });

        start.setOnAction(e -> {
            mapSelection.setDisable(true);
            start.setDisable(true);
            pause.setDisable(false);
            pause.setVisible(true);
            stop.setDisable(false);
            playerSelection.setDisable(true);

            String player = (String) playerSelection.getValue();

            robotController.setPlayer(player);
            loadMap(getMap(mapSelection));
            robotController.setGameBoard(gameBoard);
            robotController.reset();

        });

        pause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (robotController.isPaused()) {
                    robotController.resume();

                } else {
                    robotController.pause();
                }
            }
        });
        controlBar.getChildren().add(mapSelection);
        controlBar.getChildren().add(playerSelection);
        controlBar.getChildren().add(start);
        controlBar.getChildren().add(pause);
        controlBar.getChildren().add(stop);
        borderPane.setTop(controlBar);
        borderPane.setCenter(board);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Maze");


        primaryStage.show();
    }

    private String getMap(ComboBox mapSelection) {
        String value = (String) mapSelection.getValue();
        String mapName;
        if (value == null || value.isEmpty()) {
            mapName = "maps/map4.map";
        } else if (value == "One") {
            mapName = "maps/map1.map";
        } else if (value == "Two") {
            mapName = "maps/map2.map";
        } else if (value == "Three") {
            mapName = "maps/map3.map";
        } else if (value == "Four") {
            mapName = "maps/map4.map";
        } else if (value == "Five") {
            mapName = "maps/map5.map";
        } else if(value == "Seven") {
            mapName = "maps/map7.map";
        } else {
            mapName = "maps/map4.map";
        }
        return mapName;
    }

    public void setPlayerPosition(Position newPosition) {
        Platform.runLater(() -> {
            board.getChildren().remove(player);
            board.add(player, newPosition.getColumn(), newPosition.getRow());
            Label label = accessCounts[newPosition.getRow()][newPosition.getColumn()];

            if (label.getText() == null || label.getText().isEmpty()) {
                label.setText("1");
            } else {
                label.setText(String.valueOf(Integer.parseInt(label.getText()) + 1));
            }
        });
    }


    public ImageView getImage() {
        Image image = new Image("gui/sprites-1.jpg");
        ImageView pic = new ImageView();
        pic.setFitHeight(659);
        pic.setFitWidth(626);
        pic.preserveRatioProperty();
        pic.setY(0);
        pic.setY(0);
        pic.setViewport(new Rectangle2D(-30, 2, 62, 62));
        pic.setImage(image);
        return pic;
    }

    public void makerPlayerWon() {
        enable();
    }

    private void enable() {
        Platform.runLater(() -> {
            start.setDisable(false);
            mapSelection.setDisable(false);
            stop.setDisable(true);
            pause.setDisable(true);
            playerSelection.setDisable(false);
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        robotController.shutdown();
    }

    public void killPlayer(Position newPosition ) {
        Platform.runLater(() -> {
            System.out.println("Killing Player");
            enable();
            board.getChildren().remove(player);

            board.add(DEAD, newPosition.getColumn(), newPosition.getRow());


            //Label label = accessCounts[newPosition.getRow()][newPosition.getColumn()];
            //label.setText("X");
        });
    }
}