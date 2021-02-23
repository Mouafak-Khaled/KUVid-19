package model.game_physics.hitbox;


import com.fasterxml.jackson.annotation.JsonTypeName;
import services.utils.Coordinates;
import services.utils.MathUtils;
import services.utils.Vector;

import java.util.ArrayList;
import java.util.stream.Collectors;

import java.util.ArrayList;

/**
 * Representation for a rectangular hitbox.
 */
@JsonTypeName("rectangular-hitbox")
public class RectangularHitbox extends Hitbox {

    private double width;
    private double height;

    @SuppressWarnings("unused")
    RectangularHitbox() {//this is needed for the save/load functionality
    }

    public RectangularHitbox(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public ArrayList<Coordinates> getBoundaryPoints(Coordinates entityCoords) {
        Coordinates cornerCoords = new Coordinates(entityCoords.getX() + getWidth() / 2, entityCoords.getY() + getHeight() / 2);
        Vector cornerVector = new Vector(entityCoords, cornerCoords);
        ArrayList<Coordinates> pts = MathUtils.getRectangularBoundaryCoordinates(cornerVector, NUMBER_OF_POINTS);
        // apply rotation to all coordinates
        return new ArrayList<>(pts.stream().map(c -> c.rotate(entityCoords, getRotationDegree())).collect(Collectors.toList()));
    }

    @Override
    public boolean isInside(Coordinates entityCoords, Coordinates checkCoords) {
        checkCoords = MathUtils.applyRotation(-getRotationDegree(), entityCoords, checkCoords);
        Coordinates cornerCoords = new Coordinates(entityCoords.getX() + getWidth() / 2, entityCoords.getY() + getHeight() / 2);
        Vector cornerVector = new Vector(entityCoords, cornerCoords);
        return MathUtils.isWithinRectangle(cornerVector, checkCoords);
    }

    @Override
    public String toString() {
        return "RectangularHitbox: height = " + this.getHeight() + ", width = " + this.getWidth() + ", angle = " + getRotationDegree();
    }
}
