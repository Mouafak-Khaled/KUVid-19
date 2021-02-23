package model.game_running.runnables;

import model.game_building.GameConstants;
import model.game_entities.AutonomousEntity;
import model.game_running.RunningMode;


/**
 * This runnable handles moving objets in the background
 */
public class MovementRunnable extends GameRunnable {

    private final RunningMode runningMode;

    public MovementRunnable(RunningMode runningMode) {
        super();
        this.runningMode = runningMode;
    }

    @Override
    public void run() {
        running = true; //this will be set to false from somewhere else (when the game ends).
        while (running) {
            try {
                latch.await(); // if the game is paused, this latch clogs this runnable.
                // move all entities in the space
                runningMode.getShooter().move();
                for (AutonomousEntity entity : runningMode.getAutonomousEntities())
                    entity.move();

                Thread.sleep(GameConstants.GAME_THREAD_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
