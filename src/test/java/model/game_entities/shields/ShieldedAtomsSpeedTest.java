package model.game_entities.shields;

import model.game_entities.Atom;
import model.game_entities.enums.EntityType;
import model.game_physics.hitbox.CircularHitbox;
import model.game_physics.hitbox.Hitbox;
import model.game_physics.path_patterns.PathPattern;
import model.game_physics.path_patterns.StraightPattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.utils.Coordinates;
import services.utils.Velocity;

import static org.junit.jupiter.api.Assertions.*;

public class ShieldedAtomsSpeedTest {
    Coordinates coords;
    Hitbox hitbox;
    PathPattern pathPattern;
    double delta = 0.00001;


    @BeforeEach
    void initializeVariables() {
        coords = new Coordinates(0, 0);
        hitbox = new CircularHitbox(1);
        pathPattern = new StraightPattern(coords, new Velocity(1, 1));
    }

    @Test
    void isValidAtomSpeed() {
        double efficiency1 = 1, efficiency2 = 1, efficiency3 = 1, efficiency4 = 1;
        int numberOfP1 = 1, numberOfP2 = 1, numberOfP3 = 1, numberOfP4 = 1;
        int numberOfN1 = 1, numberOfN2 = 1, numberOfN3 = 1, numberOfN4 = 1;


        Atom atom1 = new Atom(coords, hitbox, pathPattern, EntityType.ALPHA, efficiency1, numberOfN1, numberOfP1);
        Atom atom2 = new Atom(coords, hitbox, pathPattern, EntityType.BETA, efficiency2, numberOfN2, numberOfP2);
        Atom atom3 = new Atom(coords, hitbox, pathPattern, EntityType.GAMMA, efficiency3, numberOfN3, numberOfP3);
        Atom atom4 = new Atom(coords, hitbox, pathPattern, EntityType.SIGMA, efficiency4, numberOfN4, numberOfP4);

        assertEquals(atom1.getSpeedPercentage(), 1.0, delta);
        assertEquals(atom2.getSpeedPercentage(), 1.0, delta);
        assertEquals(atom3.getSpeedPercentage(), 1.0, delta);
        assertEquals(atom4.getSpeedPercentage(), 1.0, delta);
    }

    @Test
    void isValidNestedEtaShieldSpeed() {
        Coordinates c = new Coordinates(0, 0);
        Hitbox hitbox = new CircularHitbox(1);
        PathPattern pathPattern = new StraightPattern(c, new Velocity(1, 1));

        double efficiency1 = 1, efficiency2 = 1, efficiency3 = 1, efficiency4 = 1;
        int numberOfP1 = 1, numberOfP2 = 1, numberOfP3 = 1, numberOfP4 = 1;
        int numberOfN1 = 1, numberOfN2 = 1, numberOfN3 = 1, numberOfN4 = 1;


        Atom atom1 = new Atom(c, hitbox, pathPattern, EntityType.ALPHA, efficiency1, numberOfN1, numberOfP1);
        Atom atom2 = new Atom(c, hitbox, pathPattern, EntityType.BETA, efficiency2, numberOfN2, numberOfP2);
        Atom atom3 = new Atom(c, hitbox, pathPattern, EntityType.GAMMA, efficiency3, numberOfN3, numberOfP3);
        Atom atom4 = new Atom(c, hitbox, pathPattern, EntityType.SIGMA, efficiency4, numberOfN4, numberOfP4);

        assertEquals(atom1.getSpeedPercentage(), 1.0, delta);
        assertEquals(atom2.getSpeedPercentage(), 1.0, delta);
        assertEquals(atom3.getSpeedPercentage(), 1.0, delta);
        assertEquals(atom4.getSpeedPercentage(), 1.0, delta);

        atom1 = new EtaShield(atom1);
        atom2 = new EtaShield(atom2);
        atom3 = new EtaShield(atom3);
        atom4 = new EtaShield(atom4);
        assertEquals(atom1.getSpeedPercentage(), 0.95, delta);
        assertEquals(atom2.getSpeedPercentage(), 0.95, delta);
        assertEquals(atom3.getSpeedPercentage(), 0.95, delta);
        assertEquals(atom4.getSpeedPercentage(), 0.95, delta);

        atom1 = new EtaShield(atom1);
        atom2 = new EtaShield(atom2);
        atom3 = new EtaShield(atom3);
        atom4 = new EtaShield(atom4);
        assertEquals(atom1.getSpeedPercentage(), 0.9025, delta);
        assertEquals(atom2.getSpeedPercentage(), 0.9025, delta);
        assertEquals(atom3.getSpeedPercentage(), 0.9025, delta);
        assertEquals(atom4.getSpeedPercentage(), 0.9025, delta);

        atom1 = new EtaShield(atom1);
        atom2 = new EtaShield(atom2);
        atom3 = new EtaShield(atom3);
        atom4 = new EtaShield(atom4);
        assertEquals(atom1.getSpeedPercentage(), 0.857375, delta);
        assertEquals(atom2.getSpeedPercentage(), 0.857375, delta);
        assertEquals(atom3.getSpeedPercentage(), 0.857375, delta);
        assertEquals(atom4.getSpeedPercentage(), 0.857375, delta);
    }

    @Test
    void isValidNestedLotaShieldSpeed() {
        Coordinates c = new Coordinates(0, 0);
        Hitbox hitbox = new CircularHitbox(1);
        PathPattern pathPattern = new StraightPattern(c, new Velocity(1, 1));

        double efficiency1 = 1, efficiency2 = 1, efficiency3 = 1, efficiency4 = 1;
        int numberOfP1 = 1, numberOfP2 = 1, numberOfP3 = 1, numberOfP4 = 1;
        int numberOfN1 = 1, numberOfN2 = 1, numberOfN3 = 1, numberOfN4 = 1;


        Atom atom1 = new Atom(c, hitbox, pathPattern, EntityType.ALPHA, efficiency1, numberOfN1, numberOfP1);
        Atom atom2 = new Atom(c, hitbox, pathPattern, EntityType.BETA, efficiency2, numberOfN2, numberOfP2);
        Atom atom3 = new Atom(c, hitbox, pathPattern, EntityType.GAMMA, efficiency3, numberOfN3, numberOfP3);
        Atom atom4 = new Atom(c, hitbox, pathPattern, EntityType.SIGMA, efficiency4, numberOfN4, numberOfP4);

        assertEquals(atom1.getSpeedPercentage(), 1.0, delta);
        assertEquals(atom2.getSpeedPercentage(), 1.0, delta);
        assertEquals(atom3.getSpeedPercentage(), 1.0, delta);
        assertEquals(atom4.getSpeedPercentage(), 1.0, delta);

        atom1 = new LotaShield(atom1);
        atom2 = new LotaShield(atom2);
        atom3 = new LotaShield(atom3);
        atom4 = new LotaShield(atom4);
        assertEquals(atom1.getSpeedPercentage(), 0.93, delta);
        assertEquals(atom2.getSpeedPercentage(), 0.93, delta);
        assertEquals(atom3.getSpeedPercentage(), 0.93, delta);
        assertEquals(atom4.getSpeedPercentage(), 0.93, delta);

        atom1 = new LotaShield(atom1);
        atom2 = new LotaShield(atom2);
        atom3 = new LotaShield(atom3);
        atom4 = new LotaShield(atom4);
        assertEquals(atom1.getSpeedPercentage(), 0.8649, delta);
        assertEquals(atom2.getSpeedPercentage(), 0.8649, delta);
        assertEquals(atom3.getSpeedPercentage(), 0.8649, delta);
        assertEquals(atom4.getSpeedPercentage(), 0.8649, delta);


        atom1 = new LotaShield(atom1);
        atom2 = new LotaShield(atom2);
        atom3 = new LotaShield(atom3);
        atom4 = new LotaShield(atom4);
        assertEquals(atom1.getSpeedPercentage(), 0.804357, delta);
        assertEquals(atom2.getSpeedPercentage(), 0.804357, delta);
        assertEquals(atom3.getSpeedPercentage(), 0.804357, delta);
        assertEquals(atom4.getSpeedPercentage(), 0.804357, delta);
    }

    @Test
    void isValidNestedThetaShieldSpeed() {
        Coordinates c = new Coordinates(0, 0);
        Hitbox hitbox = new CircularHitbox(1);
        PathPattern pathPattern = new StraightPattern(c, new Velocity(1, 1));

        double efficiency1 = 1, efficiency2 = 1, efficiency3 = 1, efficiency4 = 1;
        int numberOfP1 = 1, numberOfP2 = 1, numberOfP3 = 1, numberOfP4 = 1;
        int numberOfN1 = 1, numberOfN2 = 1, numberOfN3 = 1, numberOfN4 = 1;


        Atom atom1 = new Atom(c, hitbox, pathPattern, EntityType.ALPHA, efficiency1, numberOfN1, numberOfP1);
        Atom atom2 = new Atom(c, hitbox, pathPattern, EntityType.BETA, efficiency2, numberOfN2, numberOfP2);
        Atom atom3 = new Atom(c, hitbox, pathPattern, EntityType.GAMMA, efficiency3, numberOfN3, numberOfP3);
        Atom atom4 = new Atom(c, hitbox, pathPattern, EntityType.SIGMA, efficiency4, numberOfN4, numberOfP4);

        assertEquals(atom1.getSpeedPercentage(), 1.0, delta);
        assertEquals(atom2.getSpeedPercentage(), 1.0, delta);
        assertEquals(atom3.getSpeedPercentage(), 1.0, delta);
        assertEquals(atom4.getSpeedPercentage(), 1.0, delta);

        atom1 = new ThetaShield(atom1);
        atom2 = new ThetaShield(atom2);
        atom3 = new ThetaShield(atom3);
        atom4 = new ThetaShield(atom4);
        assertEquals(atom1.getSpeedPercentage(), 0.91, delta);
        assertEquals(atom2.getSpeedPercentage(), 0.91, delta);
        assertEquals(atom3.getSpeedPercentage(), 0.91, delta);
        assertEquals(atom4.getSpeedPercentage(), 0.91, delta);

        atom1 = new ThetaShield(atom1);
        atom2 = new ThetaShield(atom2);
        atom3 = new ThetaShield(atom3);
        atom4 = new ThetaShield(atom4);
        assertEquals(atom1.getSpeedPercentage(), 0.8281, delta);
        assertEquals(atom2.getSpeedPercentage(), 0.8281, delta);
        assertEquals(atom3.getSpeedPercentage(), 0.8281, delta);
        assertEquals(atom4.getSpeedPercentage(), 0.8281, delta);

        atom1 = new ThetaShield(atom1);
        atom2 = new ThetaShield(atom2);
        atom3 = new ThetaShield(atom3);
        atom4 = new ThetaShield(atom4);
        assertEquals(atom1.getSpeedPercentage(), 0.753571, delta);
        assertEquals(atom2.getSpeedPercentage(), 0.753571, delta);
        assertEquals(atom3.getSpeedPercentage(), 0.753571, delta);
        assertEquals(atom4.getSpeedPercentage(), 0.753571, delta);
    }

    @Test
    void isValidNestedZetaShieldSpeed() {
        Coordinates c = new Coordinates(0, 0);
        Hitbox hitbox = new CircularHitbox(1);
        PathPattern pathPattern = new StraightPattern(c, new Velocity(1, 1));

        double efficiency1 = 1, efficiency2 = 1, efficiency3 = 1, efficiency4 = 1;
        int numberOfP1 = 1, numberOfP2 = 1, numberOfP3 = 1, numberOfP4 = 1;
        int numberOfN1 = 1, numberOfN2 = 1, numberOfN3 = 1, numberOfN4 = 1;


        Atom atom1 = new Atom(c, hitbox, pathPattern, EntityType.ALPHA, efficiency1, numberOfN1, numberOfP1);
        Atom atom2 = new Atom(c, hitbox, pathPattern, EntityType.BETA, efficiency2, numberOfN2, numberOfP2);
        Atom atom3 = new Atom(c, hitbox, pathPattern, EntityType.GAMMA, efficiency3, numberOfN3, numberOfP3);
        Atom atom4 = new Atom(c, hitbox, pathPattern, EntityType.SIGMA, efficiency4, numberOfN4, numberOfP4);

        assertEquals(atom1.getSpeedPercentage(), 1.0, delta);
        assertEquals(atom2.getSpeedPercentage(), 1.0, delta);
        assertEquals(atom3.getSpeedPercentage(), 1.0, delta);
        assertEquals(atom4.getSpeedPercentage(), 1.0, delta);

        atom1 = new ZetaShield(atom1);
        atom2 = new ZetaShield(atom2);
        atom3 = new ZetaShield(atom3);
        atom4 = new ZetaShield(atom4);
        assertEquals(atom1.getSpeedPercentage(), 0.89, delta);
        assertEquals(atom2.getSpeedPercentage(), 0.89, delta);
        assertEquals(atom3.getSpeedPercentage(), 0.89, delta);
        assertEquals(atom4.getSpeedPercentage(), 0.89, delta);

        atom1 = new ZetaShield(atom1);
        atom2 = new ZetaShield(atom2);
        atom3 = new ZetaShield(atom3);
        atom4 = new ZetaShield(atom4);
        assertEquals(atom1.getSpeedPercentage(), 0.7921, delta);
        assertEquals(atom2.getSpeedPercentage(), 0.7921, delta);
        assertEquals(atom3.getSpeedPercentage(), 0.7921, delta);
        assertEquals(atom4.getSpeedPercentage(), 0.7921, delta);

        atom1 = new ZetaShield(atom1);
        atom2 = new ZetaShield(atom2);
        atom3 = new ZetaShield(atom3);
        atom4 = new ZetaShield(atom4);
        assertEquals(atom1.getSpeedPercentage(), 0.704969, delta);
        assertEquals(atom2.getSpeedPercentage(), 0.704969, delta);
        assertEquals(atom3.getSpeedPercentage(), 0.704969, delta);
        assertEquals(atom4.getSpeedPercentage(), 0.704969, delta);
    }


}
