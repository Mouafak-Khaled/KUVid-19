package services.utils;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static java.lang.Math.*;

public class MathUtils {
    /**
     * Takes a point and rotates it by a given angle.
     *
     * @param angle          the rotation angle.
     * @param rotationCenter the rotation center
     * @param coordinates    the coordinates of the object to rotate.
     * @return a new Coordinates object with the rotated x and y.
     */
    public static Coordinates applyRotation(double angle, Coordinates rotationCenter, Coordinates coordinates) {
        angle = Math.toRadians(angle);
        Vector vector = new Vector(new Coordinates((coordinates.getX() - rotationCenter.getX()), (coordinates.getY() - rotationCenter.getY())));

        double rotatedX = (float) (cos(angle) * vector.getX() - sin(angle) * vector.getY()) + rotationCenter.getX();
        double rotatedY = (float) (sin(angle) * vector.getX() + cos(angle) * vector.getY()) + rotationCenter.getY();

        return new Coordinates(rotatedX, rotatedY);
    }

    /**
     * Takes a vector and gives an inverted version of this vector.
     *
     * @param originalVector a Vector to be inverted.
     * @return an inverted Vector based on the original input Vector.
     */
    public static Vector inverseVector(Vector originalVector) {
        double x_0 = originalVector.getPositionCoordinate().getX();
        double y_0 = originalVector.getPositionCoordinate().getY();
        double x_1 = originalVector.getOriginCoordinate().getX();
        double y_1 = originalVector.getOriginCoordinate().getY();

        double newX = (2 * x_1 - x_0);
        double newY = (2 * y_1 - y_0);

        Coordinates invertedCoordinates = new Coordinates(newX, newY);
        return new Vector(originalVector.getOriginCoordinate(), invertedCoordinates);
    }

    /**
     * Checks if a point with coordinates is inside a rectangle.
     *
     * @param cornerVector A vector from the center of the rectangle to one of its corners.
     * @param point        The coordinates of a point to be tested.
     * @return true if the coordinates are inside the rectangle defined by the given vector. False otherwise.
     */
    public static boolean isWithinRectangle(Vector cornerVector, Coordinates point) {
        Vector invertedCornerVector = inverseVector(cornerVector);
        Coordinates cornerCoordinates = cornerVector.getPositionCoordinate();
        Coordinates invertedCornerCoordinates = invertedCornerVector.getPositionCoordinate();

        double x1 = cornerCoordinates.getX();
        double y1 = cornerCoordinates.getY();
        double x2 = invertedCornerCoordinates.getX();
        double y2 = invertedCornerCoordinates.getY();

        return (point.getX() <= x1 && point.getX() >= x2) && (point.getY() <= y1 && point.getY() >= y2);
    }

    /**
     * Checks if a point with coordinates is inside a circle.
     *
     * @param radius the radius of a circle.
     * @param point  The coordinates of a point to be tested.
     * @return True if the coordinates are inside the circle defined by the given vector. False otherwise.
     */
    public static boolean isWithinCircle(Coordinates centerCoordinates, double radius, Coordinates point) {
        // EFFECTS: If centerCoordinates is null
        // or point is null throws nullPointerException
        // If (x of the point - x of the center coordinate)^2 + (y of the point - y of the center coordinate)^2 is less than the radius^2 return true
        // else return false.
        if (centerCoordinates == null || point == null)
            throw new NullPointerException("MathUtils.isWithinCircle");

        double x = point.getX();
        double y = point.getY();

        double x_0 = centerCoordinates.getX();
        double y_0 = centerCoordinates.getY();

        // If the radius is zero nothing can be inside the circle
        if (radius == 0)
            return false;

        return pow((x - x_0), 2) + pow((y - y_0), 2) <= pow(radius, 2);
    }

    /**
     * Given a Vector object, returns its magnitude.
     *
     * @param vector A Vector object.
     * @return Magnitude of the Vector object.
     */
    public static double vectorMagnitude(Vector vector) {
        double x_1 = vector.getOriginCoordinate().getX();
        double x_2 = vector.getPositionCoordinate().getX();

        double y_1 = vector.getOriginCoordinate().getY();
        double y_2 = vector.getPositionCoordinate().getY();

        return sqrt(pow((x_1 - x_2), 2) + pow((y_1 - y_2), 2));
    }

    /**
     * Given a point and origin coordinates, returns the amount of translation required to translate a point to a given origin.
     *
     * @param point  a given point.
     * @param origin The origin coordinates.
     * @return The required amount of translation.
     */
    public static Coordinates translationAmount(Coordinates point, Coordinates origin) {
        double translatedAmountX = point.getX() - origin.getX();
        double translatedAmountY = point.getY() - origin.getY();
        return new Coordinates(translatedAmountX, translatedAmountY);
    }

    /**
     * Translates a point according to given amount.
     *
     * @param point       a given point.
     * @param translation the translation amount.
     * @return returns the coordinates of the translated point.
     */
    public static Coordinates translate(Coordinates point, Coordinates translation) {
        double translatedAmountX = point.getX() + translation.getX();
        double translatedAmountY = point.getY() + translation.getY();
        return new Coordinates(translatedAmountX, translatedAmountY);
    }

    /**
     * Given a vector representing a rectangle and the number of points to be generated, generates the specified number of points according to the given coordinates.
     *
     * @param vector         a vector that represents a rectangle.
     * @param numberOfPoints number of points on each side of the rectangle.
     * @return a list of coordinates on the boundary of the rectangle.
     */
    public static ArrayList<Coordinates> getRectangularBoundaryCoordinates(Vector vector, int numberOfPoints) {
        Vector invertedCornerVector = MathUtils.inverseVector(vector);
        Coordinates cornerCoordinates = vector.getPositionCoordinate();
        Coordinates invertedCornerCoordinates = invertedCornerVector.getPositionCoordinate();

        double x2 = cornerCoordinates.getX();
        double y2 = cornerCoordinates.getY();
        double x1 = invertedCornerCoordinates.getX();
        double y1 = invertedCornerCoordinates.getY();

        Coordinates[] topCoordinates = topCoordinates(numberOfPoints, x1, x2, y2);
        Coordinates[] bottomCoordinates = topCoordinates(numberOfPoints, x1, x2, y1);
        Coordinates[] leftCoordinates = sideCoordinates(numberOfPoints, x1, y1, y2);
        Coordinates[] rightCoordinates = sideCoordinates(numberOfPoints, x2, y1, y2);

        ArrayList<Coordinates> allCoordinates = new ArrayList<>(Arrays.asList(topCoordinates));
        allCoordinates.addAll(Arrays.asList(bottomCoordinates));
        allCoordinates.addAll(Arrays.asList(leftCoordinates));
        allCoordinates.addAll(Arrays.asList(rightCoordinates));

        return allCoordinates;
    }

    /**
     * Given the x and y coordinates that can represent a rectangle, and the number of points to be generated, generates the specified number of points according to the given coordinates.
     *
     * @param numberOfPoints The number of points to generate.
     * @param x1             the first x coordinate
     * @param x2             the second x coordinate
     * @param y2             the first y coordinate
     * @return a list of coordinates on the top or the bottom side of the rectangle.
     */
    private static Coordinates[] topCoordinates(int numberOfPoints, double x1, double x2, double y2) {
        Coordinates[] coordinatesArray = new Coordinates[numberOfPoints + 1];
        double length = Math.abs(x2 - x1);
        for (int i = 0; i <= numberOfPoints; i++) {
            coordinatesArray[i] = new Coordinates(x1 + (i * length / numberOfPoints), y2);
        }
        return coordinatesArray;
    }

    /**
     * Given the x and y coordinates that can represent a rectangle, and the number of points to be generated, generates the specified number of points according to the given coordinates.
     *
     * @param numberOfPoints The number of points to generate.
     * @param x1             the first x coordinate
     * @param y1             the second x coordinate
     * @param y2             the first y coordinate
     * @return a list of coordinates on the left or the right side of the rectangle.
     */
    private static Coordinates[] sideCoordinates(int numberOfPoints, double x1, double y1, double y2) {
        Coordinates[] coordinatesArray = new Coordinates[numberOfPoints + 1];
        double length = Math.abs(y2 - y1);
        for (int i = 0; i <= numberOfPoints; i++) {
            coordinatesArray[i] = new Coordinates(x1, y1 + (i * length / numberOfPoints));
        }
        return coordinatesArray;
    }

    /**
     * Given vector that represents a circle and a number of points, returns a list of coordinates containing the specified number of points on the circumference of the circle.
     *
     * @param arcVector      the vector representing a circle.
     * @param numberOfPoints The number of points to generate.
     * @return an array of coordinates around the circumference of the specified circle.
     */
    public static ArrayList<Coordinates> coordinatesAroundCircle(Vector arcVector, int numberOfPoints) {
        Coordinates[] pointsCoordinates = new Coordinates[numberOfPoints];
        double angle = 0.0;
        for (int i = 0; i < numberOfPoints; i++) {
            pointsCoordinates[i] = applyRotation(angle, arcVector.getOriginCoordinate(), arcVector.getPositionCoordinate());
            angle += (double) 360 / numberOfPoints;
        }
        return new ArrayList<>(Arrays.asList(pointsCoordinates));
    }

    /**
     * @param center Coordinates that will be translated
     * @param x      amount for center x Coordinates to be translated
     * @param y      amount for center y Coordinates to be translated
     * @return drawing Coordinates
     */
    public static Coordinates drawingCoordinates(Coordinates center, double x, double y) {
        return new Coordinates(center.getX() - x / 2, center.getY() - y / 2);
    }

    /**
     * Given an angle, returns its complement.
     *
     * @param angle an angle in radians.
     * @return The complement of a given angle.
     */
    public static double angleComplement(double angle) {
        return Math.toRadians(90 - angle);
    }

    /**
     * Given two Y components of a vector, returns a new Y component where the new Y component is the sum of the two given Y components.
     *
     * @param yFirst  the first given Y component.
     * @param ySecond the two given Y component.
     * @param angle   the angle of the new Y component.
     * @return a new Y component where the new Y component is the sum of the two given Y components.
     */
    public static int getCompositeYComponent(int yFirst, int ySecond, double angle) {
        return (int) ((yFirst + ySecond) * Math.sin(angle));
    }

    /**
     * Given two X components of a vector, returns a new X component where the new X component is the sum of the two given X components.
     *
     * @param radius the first given X component.
     * @param height the second given X component.
     * @param angle  the angle of the new X component.
     * @return a new X component where the new X component is the sum of the two given X components.
     */
    public static int getCompositeXComponent(int radius, int height, double angle) {
        return (int) ((radius + height) * Math.cos(angle));
    }

    /**
     * Given two points, returns the distance between them.
     *
     * @param firstPoint  The coordinates of the first point.
     * @param secondPoint The coordinates of the second point.
     * @return The distance between two given points.
     */
    public static double distanceBetween(Coordinates firstPoint, Coordinates secondPoint) {
        double newX = Math.abs(firstPoint.getX() - secondPoint.getX());
        double newY = Math.abs(firstPoint.getY() - secondPoint.getY());

        return Math.abs(Math.sqrt((Math.pow(newX, 2) + Math.pow(newY, 2))));
    }

    /**
     * Given an array of integers, returns a random element from this array.
     *
     * @param array array of integers.
     * @return a random element from the given array.
     */
    public static int chooseFrom(int[] array) {
        Random random = new Random();
        int randomIndex = random.nextInt(array.length);
        return array[randomIndex];
    }

    /**
     * Given an array of doubles, returns a random element from this array.
     *
     * @param array array of doubles.
     * @return a random element from the given array.
     */
    public static double chooseFrom(double[] array) {
        Random random = new Random();
        int randomIndex = random.nextInt(array.length);
        return array[randomIndex];
    }

    /**
     * truncate a double with two decimals and convert it to string
     * @param toBeTruncated the number to be truncated
     * @return the number given with a maximum of 2 decimal points as string
     */
    public static String truncateByTwo(double toBeTruncated) {
        return new DecimalFormat("#.##").format(toBeTruncated);
    }

}
