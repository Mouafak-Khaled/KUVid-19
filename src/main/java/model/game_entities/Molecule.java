package model.game_entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.game_building.GameBundle;
import model.game_building.GameConstants;
import model.game_entities.enums.EntityType;
import model.game_entities.enums.MoleculeStructure;
import model.game_entities.enums.SuperType;
import model.game_physics.hitbox.Hitbox;
import model.game_physics.path_patterns.PathPattern;
import model.game_running.CollisionVisitor;
import services.utils.Coordinates;

/**
 * Molecule: Handles the Molecule game object.
 */
public class Molecule extends AutonomousEntity {

    private MoleculeStructure structure;

    public Molecule(@JsonProperty("coordinates") Coordinates coordinates,
                    @JsonProperty("hitbox") Hitbox hitbox,
                    @JsonProperty("pathPattern") PathPattern pathPattern,
                    @JsonProperty("entityType") EntityType type,
                    @JsonProperty("structure") MoleculeStructure structure) {
        super(coordinates, hitbox, pathPattern, type);
        superType = SuperType.MOLECULE;
        this.structure = structure;
    }

    public void setStructure(MoleculeStructure structure) {
        this.structure = structure;
    }

    public MoleculeStructure getStructure() {
        return structure;
    }

    @JsonIgnore
    public double getRotationDegree() {
        return this.getHitbox().getRotationDegree();
    }

    public void spin() {
        this.getHitbox().rotate(GameConstants.SPINNING_SPEED);
    }

    @JsonIgnore
    public boolean isSpinnable() {
        return structure == MoleculeStructure.LINEAR_SPINNING;
    }

    @Override
    public void move() {
        super.move();
        if (isSpinnable())
            spin();
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
     * apply the visitor pattern to handle collisions between entities and the molecule object
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
        return "Molecule{" +
                "type=" + getEntityType() +
                ", structure=" + structure +
                '}';
    }
}
