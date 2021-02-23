package model.game_physics.path_patterns;

import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Test;
import services.utils.Coordinates;
import services.utils.Velocity;

import static org.junit.jupiter.api.Assertions.*;

class StraightPatternTest {
    @Test
    void nextPosition() {
        BasicConfigurator.configure();
        // create a StraightPattern
        Coordinates coords = new Coordinates(1, 1);
        StraightPattern pattern = new StraightPattern(coords, new Velocity(2, 3));

        coords = pattern.nextPosition();
        assertEquals(coords, new Coordinates(3, 4));

        coords = pattern.nextPosition();
        assertEquals(coords, new Coordinates(5, 7));

        for(int i = 0; i < 10;i++){
            coords = pattern.nextPosition();
        }
        assertEquals(coords, new Coordinates(25, 37));

    }

    @Test
    void reflect(){
        // create a StraightPattern
        Coordinates coords = new Coordinates(1, 1);
        StraightPattern pattern = new StraightPattern(coords, new Velocity(2, 3));
    }
}