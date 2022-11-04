package gui;

import map.GameBoard;
import map.Position;
import map.Tile;
import player.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class RobotController implements Runnable {

    static Map<String, Class<? extends MazeWanderer>> playerOptions = new HashMap<>();

    static {
        playerOptions.put("Chuck's Wanderer", ChucksPlayer.class);
        playerOptions.put("Chuck's 2nd Wanderer", StudentMazeWanderer.class);
        playerOptions.put("Jason's Wanderer", JasonsMazeWanderer.class);
        playerOptions.put("John's Wanderer", JohnsMazeWanderer.class);
        playerOptions.put("Parker's Wanderer", ParkerSmithsMazeWanderer.class);
        playerOptions.put("Carson's Wanderer", CarsonsMazeWanderer.class);
        playerOptions.put("Ruth's Wanderer", RuthsRealMazeWanderer.class);
        playerOptions.put("Fun Wanderer", RuthsMazeWanderer.class);
        playerOptions.put("Ruth's Other Wanderer", RuthsMazeWanderer2.class);
        playerOptions.put("Melissa's Other Wanderer", Robot.class);
        playerOptions.put("Kevin's Robot", KevinsMazeWanderer.class);

    }

    private boolean running = true;
    private boolean pause = true;

    private Class<? extends MazeWanderer> currentWandererType;
    private GameBoard gameBoard;
    private final Maze gui;
    private MazeWanderer brain;
    private Thread thread;


    public RobotController(Maze gui) {
        this.gui = gui;
        thread = new Thread(this);
        thread.start();
        currentWandererType = ChucksPlayer.class;
    }

    public Set<String> getPlayerOptions() {
        return playerOptions.keySet();
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    @Override
    public void run() {
        try {
            mainLoop();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        running = false;
    }

    private void mainLoop() {
        while (true) {
            if (!shouldContinue())
                break;

            Position current = gameBoard.getPlayerPosition();
            Direction move;
            try {
                move = brain.move(readSensors(current));
                if(move == null)
                    throw new PlayerReturnedNullDirection("");
                Position next = nextPosition(current, move);

                Tile tile = gameBoard.getTile(next.getRow(), next.getColumn());

                gameBoard.setPlayerPosition(next);
                gui.setPlayerPosition(next);

                if (!tile.isPassable()) {
                    current = next;
                    throw new PlayerOffTheGrid();
                }

                if (gameBoard.getEnd() != null) {
                    if (gameBoard.getEnd().equals(next)) {
                        gui.makerPlayerWon();
                        pause();
                        Thread.yield();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                gui.killPlayer(current);
                pause();
                continue;
            }

            sleep(50);
        }
    }

    private boolean shouldContinue() {
        if (!running)
            return false;
        if (pause)
            waitForActivity();
        return true;
    }

    private void waitForActivity() {
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private void sleep(long miilis) {
        try {
            Thread.sleep(miilis);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public void pause() {
        synchronized (this) {
            pause = true;
        }
    }

    public void resume() {
        synchronized (this) {
            pause = false;
            this.notify();
        }
    }

    public synchronized boolean isPaused() {
        return pause;
    }

    private SensorArray readSensors(Position position) {
        SensorArray sensor = new SensorArray();
        sensor.north = gameBoard.getTile(position.getRow() - 1, position.getColumn());
        sensor.east = gameBoard.getTile(position.getRow(), position.getColumn() + 1);
        sensor.south = gameBoard.getTile(position.getRow() + 1, position.getColumn());
        sensor.west = gameBoard.getTile(position.getRow(), position.getColumn() - 1);
        return sensor;
    }

    private Position nextPosition(Position position, Direction move) {
        Position newPosition;
        switch (move) {
            case NORTH:
                newPosition = new Position(position.getRow() - 1, position.getColumn());
                break;
            case EAST:
                newPosition = new Position(position.getRow(), position.getColumn() + 1);
                break;
            case SOUTH:
                newPosition = new Position(position.getRow() + 1, position.getColumn());
                break;
            case WEST:
                newPosition = new Position(position.getRow(), position.getColumn() - 1);
                break;
            default:
                newPosition = position;
        }
        return newPosition;
    }

    public void reset() {
        try {
            brain = currentWandererType.newInstance();
            resume();
        } catch (Exception ex) {

        }
    }

    public void shutdown() {
        running = false;
        thread.interrupt();
    }

    public void setPlayer(String player) {
        this.currentWandererType = playerOptions.get(player);
    }
}
