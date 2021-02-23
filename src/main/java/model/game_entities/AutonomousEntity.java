package model.game_entities;

import model.game_building.GameBundle;
import model.game_entities.enums.EntityType;
import model.game_physics.hitbox.Hitbox;
import model.game_physics.path_patterns.PathPattern;
import model.game_running.runnables.CollisionRunnable;
import services.utils.Coordinates;

/**
 * AutonomousEntity: a Superclass for all the game objects that move autonomously
 */
public abstract class AutonomousEntity extends Entity {

    private EntityType entityType;
    private PathPattern pathPattern;

    public AutonomousEntity(Coordinates coordinates, Hitbox hitbox, PathPattern pathPattern, EntityType entityType) {
        super(coordinates, hitbox);
        this.pathPattern = pathPattern;
        this.pathPattern.setCurrentCoords(coordinates);
        this.entityType = entityType;
    }

    public AutonomousEntity(){}

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setPathPattern(PathPattern pathPattern) {
        this.pathPattern = pathPattern;
    }

    public PathPattern getPathPattern() {
        return this.pathPattern;
    }

    @Override
    public void setCoordinates(Coordinates coordinates) {
        super.setCoordinates(coordinates);
        // update the path pattern coordinates
        this.pathPattern.setCurrentCoords(coordinates);
    }

    /**
     * move: moves the corresponding object according to the path variable.
     */
    public void move() {
        setCoordinates(this.getPathPattern().nextPosition());
    }

    public void reachBoundary(CollisionRunnable collisionRunnable) {
        collisionRunnable.defaultBoundaryBehaviour(this);
    }

    public abstract void saveState(GameBundle.Builder builder);
}
