package model.game_space;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.game_building.Configuration;
import model.game_building.GameConstants;
import model.game_running.listeners.GameStatisticsListener;
import org.apache.log4j.Logger;

//todo: can be a controller for shooter, projectile container, and blender.
public class Player {
    private String username;
    private double health;
    private double score;
    private final GameTimer timer;
    private final GameStatistics statistics;
    private static Logger logger;

    public Player(String username, GameStatisticsListener statisticsListener) {
        this.username = username;
        health = GameConstants.DEFAULT_HEALTH;
        score = 0;
        timer = new GameTimer(10);
        statistics = new GameStatistics(statisticsListener);
        logger = Logger.getLogger(this.getClass().getName());
    }

    @SuppressWarnings("unused")
    public Player() { //this is needed for the save/load functionality
        timer = new GameTimer(10);
        statistics = new GameStatistics(null); // listener to be set later
        logger = Logger.getLogger(this.getClass().getName());
    }

    public void setStatisticsListener(GameStatisticsListener listener) {
        statistics.setStatisticsListener(listener);
        statistics.updateHealth(health);
        statistics.updateTimer(timer.getCurrentTimer());
        statistics.updateScore(score);
    }

    public String getUsername() {
        return username;
    }

    @SuppressWarnings("unused")
    public double getHealth() {
        //needed for serialization
        return health;
    }

    public double getScore() {
        return score;
    }

    @SuppressWarnings("unused")
    public GameTimer getTimer() {
        //needed for serialization
        return timer;
    }

    @JsonIgnore
    public GameStatisticsListener getStatisticsListener() {
        return this.statistics.getStatisticsListener();
    }

    public void updateOwnedProjectiles() {
        statistics.changeProjectileCount();
    }


    public void changeShieldCount() {
        statistics.changeShieldCount();
    }

    public boolean loseHealth(double damageAmount) {
        health -= damageAmount;
        if (health < 0)
            health = 0;
        statistics.updateHealth(health);
        return health <= 0;
    }

    public boolean updateTime(int amountInMillis) {
        timer.decrease(amountInMillis);
        statistics.updateTimer(timer.getCurrentTimer());
        return timer.getRemainingTimeMillis() >= 1;
    }

    public void incrementScore(double scoreIncrement) { //assuming the score will only be incremented by 1.
        this.score += scoreIncrement;
        logger.info("SCORE INCREMENT" + scoreIncrement);
        statistics.updateScore(this.score);
    }

    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", health=" + health +
                ", score=" + score +
                ", timer=" + timer +
                '}';
    }
}
