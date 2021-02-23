package services.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Immutable representation for velocity
 */
public class Velocity {

    private Vector velocityVector;

    public Velocity(double xv, double yv) {
        this.velocityVector = new Vector(new Coordinates(xv, yv));
    }

    public Velocity(Vector velocityVector) {
        this.velocityVector = velocityVector;
    }

    @SuppressWarnings("unused")
    public Velocity(){//this is needed for the save/load functionality
    }

    @JsonIgnore
    public double getXv() {
        return velocityVector.getX();
    }

    @JsonIgnore
    public double getYv() {
        return velocityVector.getY();
    }

    public Vector getVelocityVector() {
        return velocityVector;
    }

    public Velocity reflect(Vector n){
        return new Velocity(n.scale(n.dot(getVelocityVector()) * 2).subtract(getVelocityVector()).reverse());
    }

    @Override
    public String toString() {
        return "Velocity{" +
                "velocityVector=" + velocityVector +
                '}';
    }
}
