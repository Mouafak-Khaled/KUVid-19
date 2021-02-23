package model.game_entities.shields;

import model.game_building.GameConstants;
import model.game_entities.Atom;

public class EtaShield extends ShieldDecorator {

    public EtaShield(Atom atom) {
        super(atom);
    }


    /**
     * @return the value of efficiency of the atom.
     */
    @Override
    public double getEfficiency() {
        //MODIFIES: the efficiency of the original atom, or possibly the shielded atom
        //EFFECTS: the efficiency of the original atom/shielded atom will be reduced depending on some criteria

        double oldEfficiency = super.getEfficiency();
        int numberOfNeutrons = super.getNumberOfNeutrons();
        int numberOfProtons = super.getNumberOfProtons();

        double efficiencyFactor;

        if (numberOfProtons != numberOfNeutrons) {
            efficiencyFactor = (1 - oldEfficiency) * Math.abs(numberOfNeutrons - numberOfProtons) /
                    (double) numberOfProtons;
            return oldEfficiency * (1 + efficiencyFactor);
        }
        efficiencyFactor = (1 - oldEfficiency) * GameConstants.ETA_EFFICIENCY_BOOST;
        return oldEfficiency * (1 + efficiencyFactor);
    }

    /**
     *
     * @return the percentage value that the speed will be decreased by.
     */
    @Override
    public double getAtomSpeedPercentage() {
        //MODIFIES: the speed of the original atom, or possibly the shielded atom
        //EFFECTS: the speed of the original atom/shielded atom will be reduced by a 5%
        return this.getAtom().getAtomSpeedPercentage() * (1 - GameConstants.ETA_SPEED_REDUCTION_PERCENTAGE);
    }
}
