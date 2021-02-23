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
 * Atom: Handles the Atom game object.
 */
@JsonTypeName("atom")
public class Atom extends Projectile {

    private final double efficiency;
    private final int numberOfProtons;
    private final int numberOfNeutrons;

    public Atom(@JsonProperty("coordinates") Coordinates coordinates,
                @JsonProperty("hitbox") Hitbox hitbox,
                @JsonProperty("pathPattern") PathPattern pathPattern,
                @JsonProperty("entityType") EntityType type,
                @JsonProperty("efficiency") Double efficiency,
                @JsonProperty("numberOfProtons") Integer numberOfProtons,
                @JsonProperty("numberOfNeutrons") Integer numberOfNeutrons
    ) {
        super(coordinates, hitbox, pathPattern, type);
        superType = SuperType.ATOM;

        this.efficiency = efficiency;

        this.numberOfProtons = numberOfProtons;
        this.numberOfNeutrons = numberOfNeutrons;
    }

    public double getEfficiency() {
        return this.efficiency;
    }

    public int getNumberOfNeutrons() {
        return this.numberOfNeutrons;
    }

    public int getNumberOfProtons() {
        return this.numberOfProtons;
    }

    @JsonIgnore
    public double getAtomSpeedPercentage() {
        return GameConstants.ATOM_SPEED_PERCENTAGE;
    }

    @JsonIgnore
    @Override
    public double getSpeedPercentage() {
        //MODIFIES:
        //EFFECTS: Since this is an unshielded atom, the percentage of the speed is 100%
        return GameConstants.DEFAULT_ATOM_SPEED_PERCENTAGE;
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
     * apply the visitor pattern to handle collisions between entities and the atom object
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
        return "Atom{" +
                "type=" + getEntityType() +
                '}';
    }
}
