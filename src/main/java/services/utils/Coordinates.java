package services.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.awt.*;

public class Coordinates {

    private double X, Y;

    public Coordinates(double x, double y) {
        X = x;
        Y = y;
    }

    public Coordinates() {

    }
    public Coordinates rotate(Coordinates rotationCenter, double angle){
        return MathUtils.applyRotation(angle, rotationCenter, this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordinates) {
            return ((Coordinates) obj).getX() == this.getX() && ((Coordinates) obj).getY() == this.getY();
        }
        return super.equals(obj);
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public void setX(double x) {
        X = x;
    }

    public void setY(double y) {
        Y = y;
    }

    @Override
    public String toString() {
        return "( " + X + ", " + Y + ")";
    }

    @JsonIgnore
    public Point getPoint() {
        return new Point((int) X, (int) Y);
    }
}
