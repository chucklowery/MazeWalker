package gui;

public class GameProperties {
    private boolean running = false;
    private boolean shouldPause = false;

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean shouldPause() {
        return shouldPause;
    }

    public void pause() {
        shouldPause = true;
    }

    public void resume() {
        shouldPause = false;
    }
}
