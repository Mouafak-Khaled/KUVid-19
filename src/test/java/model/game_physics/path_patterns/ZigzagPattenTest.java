package model.game_physics.path_patterns;

import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Test;
import services.utils.Coordinates;
import services.utils.Velocity;

import static org.junit.jupiter.api.Assertions.*;

class ZigzagPattenTest {

    @Test
    void basicZigZag() {
        BasicConfigurator.configure();

        // create a simple Zigzag pattern
        Coordinates initCoords = new Coordinates(0,0);
        Velocity rightv = new Velocity(1, 1);
        Velocity leftv = new Velocity(-2, 2);
        ZigzagPatten zigzag = new ZigzagPatten(rightv, leftv, 2, 3);
        zigzag.setCurrentCoords(initCoords);

        // first step in right diagonal
        assertEquals(zigzag.nextPosition(), new Coordinates(1, 1));
        // second step in right diagonal
        assertEquals(zigzag.nextPosition(), new Coordinates(2, 2));
        // first step in left diagonal
        assertEquals(zigzag.nextPosition(), new Coordinates(0, 4));
        // second step in left diagonal
        assertEquals(zigzag.nextPosition(), new Coordinates(-2, 6));
        // third step in left diagonal
        assertEquals(zigzag.nextPosition(), new Coordinates(-4, 8));
        // fist step in right diagonal
        assertEquals(zigzag.nextPosition(), new Coordinates(-3, 9));
        // second step in right diagonal
        assertEquals(zigzag.nextPosition(), new Coordinates(-2, 10));

    }

    @Test
    void symmetricZigzag(){
        Coordinates initCoords = new Coordinates(0,0);
        // create a symmetric zigzag pattern where left diagonal is identical to right diagonal
        ZigzagPatten zigzag = new ZigzagPatten(new Velocity(2, 2), 2);
        zigzag.setCurrentCoords(initCoords);

        // first step in right diagonal
        assertEquals(zigzag.nextPosition(), new Coordinates(2, 2));
        // second step in right diagonal
        assertEquals(zigzag.nextPosition(), new Coordinates(4, 4));
        // first step in left diagonal
        assertEquals(zigzag.nextPosition(), new Coordinates(2, 6));
        // second step in left diagonal
        assertEquals(zigzag.nextPosition(), new Coordinates(0, 8));

        // after any moves of multiplicity of 4, the x coordinates should be 0 (As the zigzag is symmetric and iteration
        // size is 2)
        for(int i = 1;i<=10;i++){
            Coordinates coords = zigzag.nextPosition();
            if(i % 4 == 0){
                assertEquals(coords.getX(), 0);
            }
        }

    }

    @Test
    void startWithLeftDiagonal(){
        BasicConfigurator.configure();

        // create a simple Zigzag pattern
        Coordinates initCoords = new Coordinates(0,0);
        Velocity rightv = new Velocity(-2, 2);
        Velocity leftv = new Velocity(1, 1);
        ZigzagPatten zigzag = new ZigzagPatten(rightv, leftv, 3, 2, false);
        zigzag.setCurrentCoords(initCoords);

        // first step in left diagonal
        assertEquals(zigzag.nextPosition(), new Coordinates(1, 1));
        // second step in left diagonal
        assertEquals(zigzag.nextPosition(), new Coordinates(2, 2));
        // first step in right diagonal
        assertEquals(zigzag.nextPosition(), new Coordinates(0, 4));
        // second step in right diagonal
        assertEquals(zigzag.nextPosition(), new Coordinates(-2, 6));
        // third step in right diagonal
        assertEquals(zigzag.nextPosition(), new Coordinates(-4, 8));
        // fist step in left diagonal
        assertEquals(zigzag.nextPosition(), new Coordinates(-3, 9));
        // second step in left diagonal
        assertEquals(zigzag.nextPosition(), new Coordinates(-2, 10));
    }
}