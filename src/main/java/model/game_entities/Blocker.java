package model.game_entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import model.game_building.Configuration;
import model.game_building.GameBundle;
import model.game_building.GameConstants;
import model.game_entities.enums.EntityType;
import model.game_entities.enums.SuperType;
import model.game_physics.hitbox.Hitbox;
import model.game_physics.path_patterns.PathPattern;
import model.game_running.CollisionVisitor;
import model.game_running.runnables.CollisionRunnable;
import services.utils.Coordinates;
import services.utils.MathUtils;

/**
 * Blocker: Handles the Blocker game object.
 */
public class Blocker extends AutonomousEntity {

    private double blockingRadius;
    private double explosionRadius;
    private Hitbox blockingHitbox;
    private Hitbox explodingHitbox;

    private boolean exploded; // Might change to a different implementation.


    public Blocker(@JsonProperty("coordinates") Coordinates coordinates,
                   @JsonProperty("hitbox") Hitbox hitbox,
                   @JsonProperty("blockingHitbox") Hitbox blockingHitbox,
                   @JsonProperty("explodingHitbox") Hitbox explodingHitbox,
                   @JsonProperty("pathPattern") PathPattern pathPattern,
                   @JsonProperty("entityType") EntityType type) {
        super(coordinates, hitbox, pathPattern, type);
        this.superType = SuperType.BLOCKER;

        this.blockingRadius = Configuration.getInstance().getUnitL() * GameConstants.BLOCKER_BLOCKING_RADIUS;
        this.explosionRadius = Configuration.getInstance().getUnitL() * GameConstants.BLOCKER_EXPLOSION_RADIUS;

        this.blockingHitbox = blockingHitbox;
        this.explodingHitbox = explodingHitbox;

        exploded = false;
    }

    @SuppressWarnings("unused")
    public Blocker() {//this is needed for the save/load functionality
    }

    public double getBlockingRadius() {
        return blockingRadius;
    }

    public double getExplosionRadius() {
        return explosionRadius;
    }

    public Hitbox getExplodingHitbox() {
        return this.explodingHitbox;
    }

    public Hitbox getBlockingHitbox() {
        return this.blockingHitbox;
    }

    @Override
    public boolean isCollidedWith(Entity entity) {
        return this.getExplodingHitbox().isInside(getCoordinates(), entity.getHitbox().getBoundaryPoints(entity.getCoordinates()));
    }

    public boolean isCollidedWithOriginalHitbox(Entity entity) {
        return this.getHitbox().isInside(getCoordinates(), entity.getHitbox().getBoundaryPoints(entity.getCoordinates()));
    }

    public boolean isCollidedWithBlockingHitbox(Entity entity) {
        return this.getBlockingHitbox().isInside(getCoordinates(), entity.getHitbox().getBoundaryPoints(entity.getCoordinates()));
    }

    public boolean isCollidedWithExplodingHitbox(Entity entity) {
        return this.getExplodingHitbox().isInside(getCoordinates(), entity.getHitbox().getBoundaryPoints(entity.getCoordinates()));
    }

    /**
     * Returns the amount of damage is done from a blocker to a given entity.
     *
     * @param entity The entity to calculate the damage with respect to.
     * @return The amount of damage with respect to a given entity.
     */
    public double getExplosionDamage(Entity entity) {
        double distance = MathUtils.distanceBetween(this.getCoordinates(), entity.getCoordinates());
        return Configuration.getInstance().getGameWidth() / distance;
    }

    @Override
    public void reachBoundary(CollisionRunnable collisionRunnable) {
        super.reachBoundary(collisionRunnable);
        this.exploded = true;
        collisionRunnable.BlockerBoundaryBehavior(this);
    }

    @Override
    public void saveState(GameBundle.Builder builder) {
        builder.addEntity(this);
    }

    public boolean isExploded() {
        return this.exploded;
    }

    public void setExploded(boolean isExploded) {
        this.exploded = isExploded;
    }

    @Override
    public String toString() {
        return "Blocker{" +
                "blockingRadius=" + blockingRadius +
                ", explosionRadius=" + explosionRadius +
                ", type=" + getEntityType() +
                '}';
    }

    // visitor pattern. Double delegation

    /**
     * handle collision with atom
     * @param visitor
     * @param atom
     */
    @Override
    public void collideWith(CollisionVisitor visitor, Atom atom) {
        visitor.handleCollision(this, atom);
    }

    /**
     * handle collision with blocker
     * @param visitor
     * @param blocker
     */
    @Override
    public void collideWith(CollisionVisitor visitor, Blocker blocker) {
        visitor.handleCollision(this, blocker);
    }

    /**
     * handle collision with molecules
     * @param visitor
     * @param molecule
     */
    @Override
    public void collideWith(CollisionVisitor visitor, Molecule molecule) {
        visitor.handleCollision(this, molecule);
    }

    /**
     * handle collision with powerup
     * @param visitor
     * @param powerup
     */
    @Override
    public void collideWith(CollisionVisitor visitor, Powerup powerup) {
        visitor.handleCollision(this, powerup);
    }

    /**
     * handle collision with shooter
     * @param visitor
     * @param shooter
     */
    @Override
    public void collideWith(CollisionVisitor visitor, Shooter shooter) {
        visitor.handleCollision(this, shooter);
    }

    /**
     * apply the visitor pattern to handle collisions between entities and the blocker object
     * @param visitor
     * @param entity
     */
    @Override
    public void acceptCollision(CollisionVisitor visitor, Entity entity) {
        entity.collideWith(visitor, this);
    }
}
