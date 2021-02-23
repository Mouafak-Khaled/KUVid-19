package model.game_entities.shields;

import model.game_building.GameConstants;
import model.game_entities.Atom;
import services.utils.MathUtils;

public class ThetaShield extends ShieldDecorator {

    private final double THETA_EFFICIENCY_BOOST;

    public ThetaShield(Atom atom) {
        super(atom);
        THETA_EFFICIENCY_BOOST = getThetaEfficiency();
    }

    /**
     * @return returns either 0.05 or 0.15 (Which resemble the values that could be Theta's efficiency) randomly.
     */
    private double getThetaEfficiency() {
        return MathUtils.chooseFrom(GameConstants.thetaEfficiencyValues);
    }

    /**
     * @return the value of efficiency of the atom.
     */
    @Override
    public double getEfficiency() {
        //MODIFIES: the efficiency of the original atom, or possibly the shielded atom
        //EFFECTS: the efficiency of the original atom/shielded atom will be reduced depending on some criteria

        double oldEfficiency = super.getEfficiency();
        double efficiencyFactor = (1 - oldEfficiency) * THETA_EFFICIENCY_BOOST;

        return oldEfficiency * (1 + efficiencyFactor);
    }

    /**
     *
     * @return the percentage value that the speed will be decreased by.
     */
    @Override
    public double getAtomSpeedPercentage() {
        //MODIFIES: the speed of the original atom, or possibly the shielded atom
        //EFFECTS: the speed of the original atom/shielded atom will be reduced by a 9%
        return super.getAtom().getAtomSpeedPercentage() * (1 - GameConstants.THETA_SPEED_REDUCTION_PERCENTAGE);
    }
}
