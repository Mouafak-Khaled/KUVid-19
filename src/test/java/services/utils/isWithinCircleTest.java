package services.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class isWithinCircleTest {


    @Test
    void isInsideCircle() {
        ///////////// BLACK BOX TESTS

        Coordinates centerCoordinates = new Coordinates(4,4);

        // pointCoordinates is null
        assertThrows(NullPointerException.class, () -> {
           MathUtils.isWithinCircle(centerCoordinates, 3.5, null);
        });

        Coordinates pointCoordinates = new Coordinates(4,4);

        // centerCoordinates is null
        assertThrows(NullPointerException.class, () -> {
            MathUtils.isWithinCircle(null, 3.5, pointCoordinates);
        });

        // pointCoordinate is in circle
        assertTrue(MathUtils.isWithinCircle(centerCoordinates, 3.5, pointCoordinates));

        // pointCoordinate is outside circle
        pointCoordinates.setX(7);
        pointCoordinates.setY(3);
        centerCoordinates.setX(3);
        centerCoordinates.setY(3);
        assertFalse(MathUtils.isWithinCircle(centerCoordinates, 3, pointCoordinates));


        ///////////// GLASS BOX TESTS

        // pointCoordinate exactly on the edge of the circle
        pointCoordinates.setX(6);
        pointCoordinates.setY(3);
        centerCoordinates.setX(3);
        centerCoordinates.setY(3);
        assertTrue(MathUtils.isWithinCircle(centerCoordinates, 3, pointCoordinates));

        // radius is 0
        assertFalse(MathUtils.isWithinCircle(centerCoordinates,0,pointCoordinates));

    }
}