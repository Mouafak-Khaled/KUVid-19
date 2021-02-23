package model.game_running.states;

import model.game_building.GameConstants;
import model.game_entities.enums.ShieldType;
import model.game_running.RunningMode;
import org.apache.log4j.Logger;

public class ResumedState implements GameState {
    private static Logger logger;
    private final RunningMode runningMode;

    public ResumedState(RunningMode runningMode) {
        logger = Logger.getLogger(ResumedState.class.getName());
        this.runningMode = runningMode;
    }

    @Override
    public void saveGameSession() {
        logger.warn("Cannot save session while the game is running. pause the game first.");
    }

    @Override
    public void showSavedSessions() {
        logger.warn("Cannot load session while the game is running. pause the game first.");
    }

    @Override
    public void applyShield(ShieldType shieldType) {
        runningMode.getShieldHandler().applyShield(shieldType);
    }

    @Override
    public void rotateShooter(int direction) {
        runningMode.getShooter().rotate(direction);
    }

    @Override
    public void moveShooter(int direction) {
        runningMode.getShooter().setMovementState(direction);
    }

    @Override
    public void resume() {
        logger.info("Game is already resumed");
    }

    @Override
    public void pause() {
        runningMode.applyRunningState(GameConstants.GAME_STATE_PAUSED);
        runningMode.setCurrentState(runningMode.getPausedState());
    }
}
