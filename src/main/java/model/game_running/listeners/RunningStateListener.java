package model.game_running.listeners;

public interface RunningStateListener {
    void onRunningStateChanged(int state);

    void onGameOver();
}
