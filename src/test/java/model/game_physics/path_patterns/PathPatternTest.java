package model.game_physics.path_patterns;

import org.junit.jupiter.api.Test;
import services.utils.Coordinates;
import services.utils.Velocity;

import static org.junit.jupiter.api.Assertions.*;

class PathPatternTest {

    @Test
    void nextPosition() {
        // test a straight pattern
        StraightPattern pattern = new StraightPattern(new Coordinates(1, 1), new Velocity(2, 3));
        assertEquals(pattern.nextPosition(), new Coordinates(3, 4));

        // corner case when initial coordinates are 0, 0
        pattern = new StraightPattern(new Coordinates(0, 0), new Velocity(3, 3));
        assertEquals(pattern.nextPosition(), new Coordinates(3, 3));

        // corner case when speeds are 0, 0
        pattern = new StraightPattern(new Coordinates(5, 5), new Velocity(0, 0));
        assertEquals(pattern.nextPosition(), new Coordinates(5, 5));

        // corner case when initial coordinates are negative
        pattern = new StraightPattern(new Coordinates(-2, -2), new Velocity(2, 2));
        assertEquals(pattern.nextPosition(), new Coordinates(0, 0));

        // corner case when velocity is negative
        pattern = new StraightPattern(new Coordinates(1, 2), new Velocity(-2, -3));
        assertEquals(pattern.nextPosition(), new Coordinates(-1, -1));

        // test multiple iterations
        pattern = new StraightPattern(new Coordinates(2, 2), new Velocity(1, 1));
        assertEquals(pattern.nextPosition(), new Coordinates(3, 3));
        assertEquals(pattern.nextPosition(), new Coordinates(4, 4));
        assertEquals(pattern.nextPosition(), new Coordinates(5, 5));

        // changing the coordinates
        pattern = new StraightPattern(new Coordinates(2, 2), new Velocity(1, 1));
        pattern.setCurrentCoords(new Coordinates(5, 5));
        assertEquals(pattern.nextPosition(), new Coordinates(6, 6));

        // changing current coordinates with multiple iterations
        pattern = new StraightPattern(new Coordinates(-1, -1), new Velocity(2, 0));
        pattern.setCurrentCoords(new Coordinates(5, 5));
        assertEquals(pattern.nextPosition(), new Coordinates(7, 5));
        assertEquals(pattern.nextPosition(), new Coordinates(9, 5));
        assertEquals(pattern.nextPosition(), new Coordinates(11, 5));

        // additional tests are found in StraightPatternTest and ZigzagPatternTest
    }
}