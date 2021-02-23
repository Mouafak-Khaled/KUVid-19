package model.game_entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import model.game_building.GameBundle;
import model.game_building.GameConstants;
import model.game_entities.enums.EntityType;
import model.game_entities.enums.SuperType;
import model.game_physics.hitbox.Hitbox;
import model.game_physics.path_patterns.PathPattern;
import model.game_running.CollisionVisitor;
import services.utils.Coordinates;

/**
 * Powerup: Handles the Powerup game object
 */
@JsonTypeName("powerup")
public class Powerup extends Projectile {
    private final boolean falling; // This variable indicates whether the powerup is falling or being shot.


    public Powerup(@JsonProperty("coordinates")Coordinates coordinates,
                @JsonProperty("hitbox")Hitbox hitbox,
                @JsonProperty("pathPattern")PathPattern pathPattern,
                @JsonProperty("entityType")EntityType type,
                @JsonProperty("falling") boolean falling) {
        super(coordinates, hitbox, pathPattern, type);
        superType = SuperType.POWERUP;
        this.falling = falling;
    }

//    public Powerup(){falling = true;}

    public boolean isFalling() {
        return this.falling;
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
     * handle collision with molecule
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
     * apply the visitor pattern to handle collisions between entities and the powerup object
     * @param visitor
     * @param entity
     */
    @Override
    public void acceptCollision(CollisionVisitor visitor, Entity entity) {
        entity.collideWith(visitor, this);
    }

    @Override
    public void saveState(GameBundle.Builder builder) {
        builder.addEntity(this);
    }

    @Override
    public String toString() {
        return "Powerup{" +
                "type=" + getEntityType() +
                '}';}

    @JsonIgnore
    public double getSpeedPercentage() {
        return GameConstants.DEFAULT_POWERUP_SPEED_PERCENTAGE;
    }
}
