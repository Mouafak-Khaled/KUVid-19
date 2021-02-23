package services.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

    @Test
    void rotateVector() {
        double epsilon = 0.001;

        Vector vector = new Vector(new Coordinates(0,0), new Coordinates(2,2));

        Vector rotatedVector = new Vector(new Coordinates(0,0), new Coordinates(-2,2));

        vector = vector.rotateVector(90);
        assertTrue(vector.getX() - rotatedVector.getX() <= epsilon && vector.getY() - rotatedVector.getY() <= epsilon);

        rotatedVector = new Vector(new Coordinates(0,0), new Coordinates(2,2));
        vector = vector.rotateVector(-90);
        assertTrue(vector.getX() - rotatedVector.getX() <= epsilon && vector.getY() - rotatedVector.getY() <= epsilon);
    }

    @Test
    void reverseVector(){
        assertEquals(new Vector(-1, -1), (new Vector(1, 1)).reverse());
        assertEquals(new Vector(1, 1, -1, -1), (new Vector(1, 1, 3, 3)).reverse());
    }

    @Test
    void translateVector(){
        assertEquals(new Vector(1, 1, 3, 3), (new Vector(2, 2)).translate(new Vector(1, 1)));
        assertEquals(new Vector(0, 3, 1,4 ), (new Vector(0, 1, 1, 2)).translate(new Vector(0, 2)));
    }

    @Test
    void scaleVector(){
        assertEquals(new Vector(3, 3), (new Vector(1, 1)).scale(3));
    }

    @Test
    void SubtractVector(){
        Vector A = new Vector(1, 0), B = new Vector(0, 1);
        assertEquals(new Vector(1, -1), A.subtract(B));
    }
}