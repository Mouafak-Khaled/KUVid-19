package model.game_physics.hitbox;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import services.utils.Coordinates;
import services.utils.MathUtils;
import services.utils.Vector;

import java.util.ArrayList;

/**
 * Circular Hitbox with radius R.
 */
@JsonTypeName("circular-hitbox")
public class CircularHitbox extends Hitbox {

    private double radius;

    public CircularHitbox(double radius) {
        this.radius = radius;
    }

    @SuppressWarnings("unused")
    public CircularHitbox() {//this is needed for the save/load functionality

    }

    public double getRadius() {
        return radius;
    }

    @Override
    public boolean isInside(Coordinates entityCoords, Coordinates checkCoords) {
        checkCoords = MathUtils.applyRotation(-getRotationDegree(), entityCoords, checkCoords);
        return MathUtils.isWithinCircle(entityCoords, this.radius, checkCoords);
    }

    @Override
    public ArrayList<Coordinates> getBoundaryPoints(Coordinates entityCoords) {
        Vector arcVector = new Vector(entityCoords, new Coordinates(entityCoords.getX() + this.radius, entityCoords.getY()));
        return MathUtils.coordinatesAroundCircle(arcVector, NUMBER_OF_POINTS);
    }

    @JsonIgnore
    @Override
    public double getWidth() {
        return this.radius * 2;
    }

    @JsonIgnore
    @Override
    public double getHeight() {
        return this.radius * 2;
    }

    @Override
    public String toString() {
        return "CircularHitbox: radius = " + this.radius + ", angle = " + getRotationDegree();
    }
}
