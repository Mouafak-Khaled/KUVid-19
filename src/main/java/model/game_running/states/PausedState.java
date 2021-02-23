package model.game_running.states;

import model.game_building.GameConstants;
import model.game_entities.enums.ShieldType;
import model.game_running.RunningMode;
import org.apache.log4j.Logger;

public class PausedState implements GameState {

    private final RunningMode runningMode;
    private static Logger logger;

    public PausedState(RunningMode runningMode) {
        this.runningMode = runningMode;
        logger = Logger.getLogger(this.getClass().getName());
    }


    @Override
    public void showSavedSessions() {
        runningMode.getSessionLoadListener().getSavedSessions();
    }


    @Override
    public void saveGameSession() {
//        runningMode.saveGameSession();
        runningMode.getSessionSaveListener().showSaveMethodSelector();
    }


    @Override
    public void applyShield(ShieldType shieldType) {
        logger.warn("cannot apply shields while the game is paused");
    }

    @Override
    public void rotateShooter(int direction) {
        logger.warn("cannot rotate shooter while the game is paused");
    }

    @Override
    public void moveShooter(int direction) {
        logger.info("cannot move shooter while the game is paused");
    }

    @Override
    public void resume() {
        runningMode.applyRunningState(GameConstants.GAME_STATE_RESUMED);
        runningMode.setCurrentState(runningMode.getResumedState());
    }

    @Override
    public void pause() {
        logger.info("Game is already paused");
    }
}
