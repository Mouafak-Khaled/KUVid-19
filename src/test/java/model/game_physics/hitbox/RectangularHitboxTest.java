package model.game_physics.hitbox;

import org.junit.jupiter.api.Test;
import services.utils.Coordinates;

import static org.junit.jupiter.api.Assertions.*;

class RectangularHitboxTest {

    @Test
    void isInside() {
        Coordinates ownerCoordinates = new Coordinates(2,1);
        RectangularHitbox rectHitbox = new RectangularHitbox(4, 2);

        assertTrue(rectHitbox.isInside(ownerCoordinates, new Coordinates(4,0)));
        assertTrue(rectHitbox.isInside(ownerCoordinates, new Coordinates(1,1)));
        assertFalse(rectHitbox.isInside(ownerCoordinates, new Coordinates(3,3)));
        assertFalse(rectHitbox.isInside(ownerCoordinates, new Coordinates(1,3)));

        rectHitbox.rotate(90);
        assertFalse(rectHitbox.isInside(ownerCoordinates, new Coordinates(4,0)));
        assertTrue(rectHitbox.isInside(ownerCoordinates, new Coordinates(1,3)));
        assertFalse(rectHitbox.isInside(ownerCoordinates, new Coordinates(0,0)));
        assertTrue(rectHitbox.isInside(ownerCoordinates, new Coordinates(3,1)));
        assertTrue(rectHitbox.isInside(ownerCoordinates, new Coordinates(1.9,2)));
        assertFalse(rectHitbox.isInside(ownerCoordinates, new Coordinates(0,4)));
        assertFalse(rectHitbox.isInside(ownerCoordinates, new Coordinates(1,3.5)));
        assertTrue(rectHitbox.isInside(ownerCoordinates, new Coordinates(3,0)));
        assertTrue(rectHitbox.isInside(ownerCoordinates, new Coordinates(3,-0.9)));
    }

    @Test
    void isHitboxInside() {
        Coordinates entity1Coords = new Coordinates(0,0);
        RectangularHitbox rectHitbox1 = new RectangularHitbox(4, 2);

        Coordinates entity2Coords = new Coordinates(0,2);
        RectangularHitbox rectHitbox2 = new RectangularHitbox(4, 2);
    }
}