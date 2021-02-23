package model.game_physics.hitbox;

import org.junit.jupiter.api.Test;
import services.utils.Coordinates;

import static org.junit.jupiter.api.Assertions.*;

class CircularHitboxTest {

    @Test
    void isInside() {
        Coordinates ownerCoordinates = new Coordinates(0,0);
        Coordinates centerCoordinates = new Coordinates(0,0);
        CircularHitbox rectHitbox = new CircularHitbox(3);

        Coordinates point = new Coordinates(3,3);

        assertFalse(rectHitbox.isInside(ownerCoordinates, point));

        point = new Coordinates(3,0);
        assertTrue(rectHitbox.isInside(ownerCoordinates, point));

        point = new Coordinates(3,2);
        assertFalse(rectHitbox.isInside(ownerCoordinates, point));

        point = new Coordinates(2,2);
        assertTrue(rectHitbox.isInside(ownerCoordinates, point));
    }

}