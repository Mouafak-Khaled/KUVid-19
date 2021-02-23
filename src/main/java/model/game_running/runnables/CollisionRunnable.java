package model.game_running.runnables;

import model.game_building.Configuration;
import model.game_building.GameConstants;
import model.game_entities.AutonomousEntity;
import model.game_entities.Blocker;
import model.game_running.CollisionVisitor;
import model.game_running.RunningMode;
import services.utils.Coordinates;
import services.utils.Vector;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * This runnable handles collisions between entities.
 */
public class CollisionRunnable extends GameRunnable {

    private final RunningMode runningMode;
    Configuration config;
    private final CollisionVisitor collisionHandler;
    private final CountDownLatch latch;

    public CollisionRunnable(RunningMode runningMode, CollisionVisitor collisionHandler) {
        super();
        this.runningMode = runningMode;
        this.latch = new CountDownLatch(0);
        this.collisionHandler = collisionHandler;
        config = Configuration.getInstance();
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                latch.await(); // if the game is paused, this latch clogs this runnable.
                for (AutonomousEntity sourceEntity : runningMode.getAutonomousEntities()) {
                    for (AutonomousEntity targetEntity : runningMode.getAutonomousEntities()) {
                        entityEntityCheck(sourceEntity, targetEntity);
                    }
                    // check if the entity collided with the shooter
                    entityShooterCheck(sourceEntity);
                    // check if the entity left the game view from and end boundary (top bottom)
                    entityEndBoundaryCheck(sourceEntity);
                    // check if the entity collided with a side boundary
                    entitySideBoundaryCheck(sourceEntity);
                }

                // TODO make the collision delay more than the movement delay
                Thread.sleep(GameConstants.GAME_THREAD_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void entityEntityCheck(AutonomousEntity sourceEntity, AutonomousEntity targetEntity) {
        if (sourceEntity == targetEntity) //don't collision check an entity with itself
            return;
        if (sourceEntity.isCollidedWith(targetEntity))
            sourceEntity.acceptCollision(collisionHandler, targetEntity);
    }

    private void entityShooterCheck(AutonomousEntity sourceEntity) {
        if (sourceEntity.isCollidedWith(runningMode.getShooter()))
            sourceEntity.acceptCollision(collisionHandler, runningMode.getShooter());
    }

    private void entityEndBoundaryCheck(AutonomousEntity sourceEntity) {
        if (sourceEntity.getCoordinates().getY() < 0 ||
                sourceEntity.getCoordinates().getY() > config.getGamePanelDimensions().height - config.getBaseHeight())
            sourceEntity.reachBoundary(this);
    }

    private void entitySideBoundaryCheck(AutonomousEntity sourceEntity) {
        ArrayList<Coordinates> coords = sourceEntity.getBoundaryPoints();
        for (Coordinates coord : coords) {
            if (coord.getX() > config.getGamePanelDimensions().width) {
                sourceEntity.getPathPattern().reflect(new Vector(new Coordinates(1, 0)));
                GameRunnable.logger.debug("[CollisionRunnable] entity collided with the left boarder");
            }
            if (coord.getX() < 0) {
                sourceEntity.getPathPattern().reflect(new Vector(new Coordinates(-1, 0)));
                GameRunnable.logger.debug("[CollisionRunnable] entity collided with the right boarder");
            }
        }
    }

    public void defaultBoundaryBehaviour(AutonomousEntity entity) {
        runningMode.removeEntity(entity);
    }

    public void BlockerBoundaryBehavior(Blocker blocker) {
        if (blocker.isCollidedWithExplodingHitbox(runningMode.getShooter())) {
            blocker.acceptCollision(collisionHandler, runningMode.getShooter());
        }
        for (AutonomousEntity entity : runningMode.getAutonomousEntities()) {
            if (blocker.isCollidedWithExplodingHitbox(entity))
                blocker.acceptCollision(collisionHandler, entity);
        }
        runningMode.removeEntity(blocker);
    }

}
