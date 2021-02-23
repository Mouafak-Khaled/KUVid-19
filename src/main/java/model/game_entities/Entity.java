package model.game_entities;

import model.game_entities.enums.SuperType;
import model.game_physics.hitbox.Hitbox;
import model.game_running.Collidable;
import org.apache.log4j.Logger;
import services.utils.Coordinates;

import java.util.ArrayList;

/**
 * Entity: a Superclass for all the game objects
 */

public abstract class Entity implements Collidable {
    protected SuperType superType;
    private Coordinates coordinates;
    private Hitbox hitbox;
    protected Logger logger = Logger.getLogger(Entity.class.getName());

    public Entity(Coordinates coordinates, Hitbox hitbox) {
        this.coordinates = coordinates;
        this.hitbox = hitbox;
    }

    public Entity() {
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }


    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public void setHitbox(Hitbox hitbox) {
        this.hitbox = hitbox;
    }

    public boolean isCollidedWith(Entity entity) {
        return this.getHitbox().isInside(getCoordinates(), entity.getHitbox().getBoundaryPoints(entity.getCoordinates()));
    }

    public abstract void move();

    public SuperType getSuperType() {
        return superType;
    }

    public ArrayList<Coordinates> getBoundaryPoints(){
        return this.getHitbox().getBoundaryPoints(getCoordinates());
    }

}
