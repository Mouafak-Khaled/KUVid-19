package model.game_entities.shields;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.game_entities.Atom;

/**
 * Decorator pattern to add new attributes to atom class following open-closed principle.
 */
public abstract class ShieldDecorator extends Atom {
    protected Atom atom;


    public ShieldDecorator(Atom atom) {
        super(atom.getCoordinates(),
                atom.getHitbox(),
                atom.getPathPattern(),
                atom.getEntityType(),
                atom.getEfficiency(),
                atom.getNumberOfProtons(),
                atom.getNumberOfNeutrons());

        this.atom = atom;
    }

    Atom getAtom() {
        return this.atom;
    }

    @Override
    public double getEfficiency() {
        return atom.getEfficiency();
    }

    @JsonIgnore
    @Override
    public double getSpeedPercentage() {
        //MODIFIES: the speed of the original atom, or possibly the shielded atom
        //EFFECTS: the speed of the original atom/shielded atom will be reduced by a percentage depending on the
        //type of the shield

        return super.getSpeedPercentage() * getAtomSpeedPercentage();
    }
}
