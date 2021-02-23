package services.utils;


//TODO complete documentation

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Immutable representation of a vector
 */
public class Vector {

    private Coordinates originCoordinate, positionCoordinate;

    public Vector(Coordinates originCoordinate, Coordinates positionCoordinate) {
        this.originCoordinate = originCoordinate;
        this.positionCoordinate = positionCoordinate;
    }

    public Vector() {
    }

    /**
     * Constructor for vectors with default original coordinates of 0, 0
     *
     * @param positionCoordinate position coordinate
     */
    public Vector(Coordinates positionCoordinate) {
        this(new Coordinates(0, 0), positionCoordinate);
    }

    public Vector(double x, double y) {
        this(new Coordinates(x, y));
    }

    public Vector(double x, double y, double x1, double y1) {
        this(new Coordinates(x, y), new Coordinates(x1, y1));
    }

    public Coordinates getPositionCoordinate() {
        return positionCoordinate;
    }

    public Coordinates getOriginCoordinate() {
        return originCoordinate;
    }

    @JsonIgnore
    public double getX() {
        return positionCoordinate.getX() - originCoordinate.getX();
    }

    @JsonIgnore
    public double getY() {
        return positionCoordinate.getY() - originCoordinate.getY();
    }

    @JsonIgnore
    public double getMagnitude() {
        return MathUtils.vectorMagnitude(this);
    }

    public Vector rotateVector(double angle) {
        return new Vector(originCoordinate, MathUtils.applyRotation(angle, this.originCoordinate, this.positionCoordinate));
    }

    /**
     * Get the dot product with Vector B
     *
     * @param vector to apply dot product with
     * @return the dot product
     */
    public double dot(Vector vector) {
        return vector.getX() * this.getX() + vector.getY() * this.getY();
    }

    /**
     * Scale the vector by factor
     *
     * @param factor which will be used for scaling
     * @return a scaled vector with the scaling factor given
     */
    public Vector scale(double factor) {
        return new Vector(originCoordinate, new Coordinates(
                originCoordinate.getX() + getX() * factor,
                getOriginCoordinate().getY() + getY() * factor));
    }

    /**
     * Take the reverse of the vector
     *
     * @return a reversed vector
     */
    public Vector reverse() {
        return new Vector(this.originCoordinate, new Coordinates(getOriginCoordinate().getX() - getX(),
                getOriginCoordinate().getY() - getY()));
    }

    /**
     * Subtract B from the vector (i.e A-B)
     *
     * @param vector to be subtracted from
     * @return the value of the subtraction of vector from this
     */
    public Vector subtract(Vector vector) {
        // translate vector B with amount of vector A, then reverse
        vector = vector.translate(new Vector(
                getPositionCoordinate().getX() - vector.getOriginCoordinate().getX(),
                getPositionCoordinate().getY() - vector.getOriginCoordinate().getY()));
        return new Vector(originCoordinate, vector.reverse().getPositionCoordinate());
    }

    public Vector translate(Vector B) {
        return new Vector(getOriginCoordinate().getX() + B.getX(), getOriginCoordinate().getY() + B.getY(),
                getPositionCoordinate().getX() + B.getX(), getPositionCoordinate().getY() + B.getY());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector) {
            return this.getPositionCoordinate().equals(((Vector) obj).getPositionCoordinate()) &&
                    this.getOriginCoordinate().equals(((Vector) obj).getOriginCoordinate());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Vector(" +
                "originCoordinate=" + originCoordinate +
                ", positionCoordinate=" + positionCoordinate +
                ')';
    }
}
