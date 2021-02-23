package model.game_physics.hitbox;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import services.utils.Coordinates;
import java.util.ArrayList;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "type") @JsonSubTypes({
        @JsonSubTypes.Type(value = RectangularHitbox.class, name = "rectangular-hitbox"),
        @JsonSubTypes.Type(value = CircularHitbox.class, name = "circular-hitbox")
})
/**
 * A Hitbox that will surrounds entities and represents its dimensions, and rotation in the game space.
 */
public abstract class Hitbox {

    protected double rotationDegree = 0;
    protected final int NUMBER_OF_POINTS = 8;

    /**
     * Rotates the Hitbox in the specified direction
     * @param angle indicating the direction of the rotation
     */
    public void rotate(double angle){
        this.rotationDegree += angle;
    }

    public double getRotationDegree(){
        return this.rotationDegree;
    }

    /**
     * checks if coordinates is inside the Hitbox
     * @param entityCoords the center of the coordinate of the entity
     * @param checkCoords a coordinates to be checked if inside the entity
     * @return true if coordinates is inside the Hitbox, otherwise returns false
     */
    public abstract boolean isInside(Coordinates entityCoords, Coordinates checkCoords);

    /**
     * get a set of sample points on the boundary of the Hitbox.
     * @param entityCoords the center coordinates of the entity which represents the center of the hitbox
     * @return a set of sample points in the boundary.
     */
    public abstract ArrayList<Coordinates> getBoundaryPoints(Coordinates entityCoords);

    public abstract double getWidth();
    public abstract double  getHeight();

    /**
     * Checks if a Hitbox is collided with a another Hitbox.
     * @param entityCoords The owner object coordinates.
     * @param checkList the target list of coordinates.
     * @return True if the owner Hitbox is collided with the target Hitbox.
     */
    public boolean isInside(Coordinates entityCoords, ArrayList<Coordinates> checkList){
        for(Coordinates checkPoint : checkList){
            if(this.isInside(entityCoords, checkPoint)){
                return true;
            }
        }
        return false;
    }

}
