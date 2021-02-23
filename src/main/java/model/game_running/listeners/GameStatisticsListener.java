package model.game_running.listeners;

/**
 * a listener to notify the statistics window UI of any change to the statistics
 */
public interface GameStatisticsListener {
    void onHealthChanged(double health);

    void onTimerChanged(String currentTime);

    void onScoreChanged(double score);

    void onProjectileCountChange();

    void onShieldsCountChange();
}
