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
import services.utils.MathUtils;
import services.utils.Velocity;

import static model.game_building.GameConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShieldsEfficiencyTest {

    Coordinates coords;
    Hitbox hitbox;
    PathPattern pathPattern;

    double stabilityConstant1, stabilityConstant2, stabilityConstant3, stabilityConstant4;
    double efficiency1, efficiency2, efficiency3, efficiency4;
    int numberOfP1, numberOfP2, numberOfP3, numberOfP4;
    int numberOfN1, numberOfN2, numberOfN3, numberOfN4;

    @BeforeEach
    void initializeVariables() {
        coords = new Coordinates(0, 0);
        hitbox = new CircularHitbox(1);
        pathPattern = new StraightPattern(coords, new Velocity(1, 1));

        stabilityConstant1 = 0.85;
        stabilityConstant2 = 0.9;
        stabilityConstant3 = 0.8;
        stabilityConstant4 = 0.7;

        numberOfP1 = 8;
        numberOfP2 = 16;
        numberOfP3 = 32;
        numberOfP4 = 64;

        int[] ALPHA_NEUTRON_VALUES = {7, 8, 9};
        int[] BETA_NEUTRON_VALUES = {15, 16, 17, 18, 21};
        int[] GAMMA_NEUTRON_VALUES = {29, 32, 33};
        int[] SIGMA_NEUTRON_VALUES = {63, 64, 67};

        numberOfN1 = MathUtils.chooseFrom(ALPHA_NEUTRON_VALUES);
        numberOfN2 = MathUtils.chooseFrom(BETA_NEUTRON_VALUES);
        numberOfN3 = MathUtils.chooseFrom(GAMMA_NEUTRON_VALUES);
        numberOfN4 = MathUtils.chooseFrom(SIGMA_NEUTRON_VALUES);

        efficiency1 = alphaEfficiency(numberOfN1);
        efficiency2 = betaEfficiency(numberOfN2);
        efficiency3 = gammaEfficiency(numberOfN3);
        efficiency4 = sigmaEfficiency(numberOfN4);
    }

    @Test
    void isCorrectAtomEfficiency() {
        Atom atom1 = new Atom(coords, hitbox, pathPattern, EntityType.ALPHA, efficiency1, numberOfN1, numberOfP1);
        Atom atom2 = new Atom(coords, hitbox, pathPattern, EntityType.BETA, efficiency2, numberOfN2, numberOfP2);
        Atom atom3 = new Atom(coords, hitbox, pathPattern, EntityType.GAMMA, efficiency3, numberOfN3, numberOfP3);
        Atom atom4 = new Atom(coords, hitbox, pathPattern, EntityType.SIGMA, efficiency4, numberOfN4, numberOfP4);

        assertEquals(atom1.getEfficiency(), efficiency1);
        assertEquals(atom2.getEfficiency(), efficiency2);
        assertEquals(atom3.getEfficiency(), efficiency3);
        assertEquals(atom4.getEfficiency(), efficiency4);
    }

    @Test
    void isCorrectEtaEfficiency() {
        Atom atom1 = new Atom(coords, hitbox, pathPattern, EntityType.ALPHA, efficiency1, numberOfN1, numberOfP1);
        Atom atom2 = new Atom(coords, hitbox, pathPattern, EntityType.BETA, efficiency2, numberOfN2, numberOfP2);
        Atom atom3 = new Atom(coords, hitbox, pathPattern, EntityType.GAMMA, efficiency3, numberOfN3, numberOfP3);
        Atom atom4 = new Atom(coords, hitbox, pathPattern, EntityType.SIGMA, efficiency4, numberOfN4, numberOfP4);
        assertEquals(atom1.getEfficiency(), efficiency1);
        assertEquals(atom2.getEfficiency(), efficiency2);
        assertEquals(atom3.getEfficiency(), efficiency3);
        assertEquals(atom4.getEfficiency(), efficiency4);

        double oldeff1 = atom1.getEfficiency();
        double oldeff2 = atom2.getEfficiency();
        double oldeff3 = atom3.getEfficiency();
        double oldeff4 = atom4.getEfficiency();

        atom1 = new EtaShield(atom1);
        atom2 = new EtaShield(atom2);
        atom3 = new EtaShield(atom3);
        atom4 = new EtaShield(atom4);
        assertTrue(
                atom1.getNumberOfNeutrons() != atom1.getNumberOfProtons() &&
                        atom1.getEfficiency() == oldeff1 * (1 + (1 - oldeff1) * Math.abs(atom1.getNumberOfNeutrons() - atom1.getNumberOfProtons()) /
                                (double) atom1.getNumberOfProtons()) ||
                        atom1.getNumberOfNeutrons() == atom1.getNumberOfProtons() &&
                                atom1.getEfficiency() == oldeff1 * (1 + (1 - oldeff1) * 0.05));

        assertTrue(
                atom2.getNumberOfNeutrons() != atom2.getNumberOfProtons() &&
                        atom2.getEfficiency() == oldeff2 * (1 + (1 - oldeff2) * Math.abs(atom2.getNumberOfNeutrons() - atom2.getNumberOfProtons()) /
                                (double) atom2.getNumberOfProtons()) ||
                        atom2.getNumberOfNeutrons() == atom2.getNumberOfProtons() &&
                                atom2.getEfficiency() == oldeff2 * (1 + (1 - oldeff2) * 0.05));

        assertTrue(
                atom3.getNumberOfNeutrons() != atom3.getNumberOfProtons() &&
                        atom3.getEfficiency() == oldeff3 * (1 + (1 - oldeff3) * Math.abs(atom3.getNumberOfNeutrons() - atom3.getNumberOfProtons()) /
                                (double) atom3.getNumberOfProtons()) ||
                        atom3.getNumberOfNeutrons() == atom3.getNumberOfProtons() &&
                                atom3.getEfficiency() == oldeff3 * (1 + (1 - oldeff3) * 0.05));


        assertTrue(
                atom4.getNumberOfNeutrons() != atom4.getNumberOfProtons() &&
                        atom4.getEfficiency() == oldeff4 * (1 + (1 - oldeff4) * Math.abs(atom4.getNumberOfNeutrons() - atom4.getNumberOfProtons()) /
                                (double) atom4.getNumberOfProtons()) ||
                        atom4.getNumberOfNeutrons() == atom4.getNumberOfProtons() &&
                                atom4.getEfficiency() == oldeff4 * (1 + (1 - oldeff4) * 0.05));


    }

    @Test
    void isCorrectLotaEfficiency() {
        Atom atom1 = new Atom(coords, hitbox, pathPattern, EntityType.ALPHA, efficiency1, numberOfN1, numberOfP1);
        Atom atom2 = new Atom(coords, hitbox, pathPattern, EntityType.BETA, efficiency2, numberOfN2, numberOfP2);
        Atom atom3 = new Atom(coords, hitbox, pathPattern, EntityType.GAMMA, efficiency3, numberOfN3, numberOfP3);
        Atom atom4 = new Atom(coords, hitbox, pathPattern, EntityType.SIGMA, efficiency4, numberOfN4, numberOfP4);
        assertEquals(atom1.getEfficiency(), efficiency1);
        assertEquals(atom2.getEfficiency(), efficiency2);
        assertEquals(atom3.getEfficiency(), efficiency3);
        assertEquals(atom4.getEfficiency(), efficiency4);

        double oldeff1 = atom1.getEfficiency();
        double oldeff2 = atom2.getEfficiency();
        double oldeff3 = atom3.getEfficiency();
        double oldeff4 = atom4.getEfficiency();

        atom1 = new LotaShield(atom1);
        atom2 = new LotaShield(atom2);
        atom3 = new LotaShield(atom3);
        atom4 = new LotaShield(atom4);
        assertEquals(oldeff1 * (1 + (1 - oldeff1) * 0.1), atom1.getEfficiency());
        assertEquals(oldeff2 * (1 + (1 - oldeff2) * 0.1), atom2.getEfficiency());
        assertEquals(oldeff3 * (1 + (1 - oldeff3) * 0.1), atom3.getEfficiency());
        assertEquals(oldeff4 * (1 + (1 - oldeff4) * 0.1), atom4.getEfficiency());
    }

    @Test
    void isCorrectTetaEfficiency() {
        Atom atom1 = new Atom(coords, hitbox, pathPattern, EntityType.ALPHA, efficiency1, numberOfN1, numberOfP1);
        Atom atom2 = new Atom(coords, hitbox, pathPattern, EntityType.BETA, efficiency2, numberOfN2, numberOfP2);
        Atom atom3 = new Atom(coords, hitbox, pathPattern, EntityType.GAMMA, efficiency3, numberOfN3, numberOfP3);
        Atom atom4 = new Atom(coords, hitbox, pathPattern, EntityType.SIGMA, efficiency4, numberOfN4, numberOfP4);
        assertEquals(atom1.getEfficiency(), efficiency1);
        assertEquals(atom2.getEfficiency(), efficiency2);
        assertEquals(atom3.getEfficiency(), efficiency3);
        assertEquals(atom4.getEfficiency(), efficiency4);

        double oldeff1 = atom1.getEfficiency();
        double oldeff2 = atom2.getEfficiency();
        double oldeff3 = atom3.getEfficiency();
        double oldeff4 = atom4.getEfficiency();

        atom1 = new ThetaShield(atom1);
        atom2 = new ThetaShield(atom2);
        atom3 = new ThetaShield(atom3);
        atom4 = new ThetaShield(atom4);

        assertTrue(
                atom1.getEfficiency() == oldeff1 * (1 + (1 - oldeff1) * 0.05) ||
                        atom1.getEfficiency() == oldeff1 * (1 + (1 - oldeff1) * 0.15));
        assertTrue(
                atom2.getEfficiency() == oldeff2 * (1 + (1 - oldeff2) * 0.05) ||
                        atom2.getEfficiency() == oldeff2 * (1 + (1 - oldeff2) * 0.15));
        assertTrue(
                atom3.getEfficiency() == oldeff3 * (1 + (1 - oldeff3) * 0.05) ||
                        atom3.getEfficiency() == oldeff3 * (1 + (1 - oldeff3) * 0.15));
        assertTrue(
                atom4.getEfficiency() == oldeff4 * (1 + (1 - oldeff4) * 0.05) ||
                        atom4.getEfficiency() == oldeff4 * (1 + (1 - oldeff4) * 0.15));
    }

    @Test
    void isCorrectZetaEfficiency() {
        Atom atom1 = new Atom(coords, hitbox, pathPattern, EntityType.ALPHA, efficiency1, numberOfN1, numberOfP1);
        Atom atom2 = new Atom(coords, hitbox, pathPattern, EntityType.BETA, efficiency2, numberOfN2, numberOfP2);
        Atom atom3 = new Atom(coords, hitbox, pathPattern, EntityType.GAMMA, efficiency3, numberOfN3, numberOfP3);
        Atom atom4 = new Atom(coords, hitbox, pathPattern, EntityType.SIGMA, efficiency4, numberOfN4, numberOfP4);
        assertEquals(atom1.getEfficiency(), efficiency1);
        assertEquals(atom2.getEfficiency(), efficiency2);
        assertEquals(atom3.getEfficiency(), efficiency3);
        assertEquals(atom4.getEfficiency(), efficiency4);

        double oldeff1 = atom1.getEfficiency();
        double oldeff2 = atom2.getEfficiency();
        double oldeff3 = atom3.getEfficiency();
        double oldeff4 = atom4.getEfficiency();

        atom1 = new ZetaShield(atom1);
        atom2 = new ZetaShield(atom2);
        atom3 = new ZetaShield(atom3);
        atom4 = new ZetaShield(atom4);
        assertTrue(
                atom1.getNumberOfNeutrons() == atom1.getNumberOfProtons() &&
                            atom1.getEfficiency() == oldeff1 * (1 + (1 - oldeff1) * 0.2) ||
                        atom1.getNumberOfNeutrons() != atom1.getNumberOfProtons() &&
                                atom1.getEfficiency() == oldeff1);

        assertTrue(
                atom2.getNumberOfNeutrons() == atom2.getNumberOfProtons() &&
                        atom2.getEfficiency() == oldeff2 * (1 + (1 - oldeff2) * 0.2) ||
                        atom2.getNumberOfNeutrons() != atom2.getNumberOfProtons() &&
                                atom2.getEfficiency() == oldeff2 );

        assertTrue(
                atom3.getNumberOfNeutrons() == atom3.getNumberOfProtons() &&
                        atom3.getEfficiency() == oldeff3 * (1 + (1 - oldeff3) * 0.2) ||
                        atom3.getNumberOfNeutrons() != atom3.getNumberOfProtons() &&
                                atom3.getEfficiency() == oldeff3);


        assertTrue(
                atom4.getNumberOfNeutrons() == atom4.getNumberOfProtons() &&
                        atom4.getEfficiency() == oldeff4 * (1 + (1 - oldeff4) * 0.2) ||
                        atom4.getNumberOfNeutrons() != atom4.getNumberOfProtons() &&
                                atom4.getEfficiency() == oldeff4);


    }


    private double alphaEfficiency(int numberOfNeutrons) {
        return 1 - (Math.abs(numberOfNeutrons - ALPHA_PROTONS) / (double) ALPHA_PROTONS) * ALPHA_STABILITY_CONSTANT;
    }

    private double betaEfficiency(int numberOfNeutrons) {
        return BETA_STABILITY_CONSTANT - (0.5 * Math.abs(numberOfNeutrons - BETA_PROTONS) / (double) BETA_PROTONS);
    }

    private double gammaEfficiency(int numberOfNeutrons) {
        return GAMMA_STABILITY_CONSTANT + (float) Math.abs(numberOfNeutrons - GAMMA_PROTONS) / (double) 2 * GAMMA_PROTONS;
    }

    private double sigmaEfficiency(int numberOfNeutrons) {
        return (1 + (SIGMA_STABILITY_CONSTANT / 2) + (float) Math.abs(numberOfNeutrons - SIGMA_PROTONS) / (double) SIGMA_PROTONS);
    }
}
