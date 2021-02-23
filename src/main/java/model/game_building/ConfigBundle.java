package model.game_building;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

/**
 * a container for game parameters
 */
public class ConfigBundle {

    private double l;
    private boolean linearAlpha, linearBeta, spinningAlpha, spinningBeta; // TODO: convert to enums
    private int difficulty; // 0, 1, 2 for easy, normal, difficult, respectively. TODO: convert to enum
    private ArrayList<Integer> atoms, powerups, molecules, blockers, shields;
    private String theme;

    /**
     * Constructor to initialize game parameter attributes
     *
     * @param atoms         array of atoms number for each type
     * @param powerups      array of powerups number for each type
     * @param blockers      array of blockers number for each type
     * @param molecules     array of molecules number for each type
     * @param l             The L unit of the game
     * @param linearAlpha   a boolean indicates weather Alpha molecules are linear or not
     * @param linearBeta    a boolean indicates weather Beta molecules are linear or not
     * @param spinningAlpha a boolean indicates weather Alpha molecules are spinning or not
     * @param spinningBeta  a boolean indicates weather Beta molecules are spinning or not
     * @param difficulty    integer number indicates the difficulty level
     */
    public ConfigBundle(ArrayList<Integer> atoms,
                        ArrayList<Integer> powerups,
                        ArrayList<Integer> blockers,
                        ArrayList<Integer> molecules,
                        ArrayList<Integer> shields,
                        double l,
                        boolean linearAlpha,
                        boolean linearBeta,
                        boolean spinningAlpha,
                        boolean spinningBeta,
                        int difficulty,
                        String theme) {

        this.atoms = atoms;
        this.powerups = powerups;
        this.blockers = blockers;
        this.molecules = molecules;
        this.shields = shields;
        this.l = l;
        this.linearAlpha = linearAlpha;
        this.linearBeta = linearBeta;
        this.spinningAlpha = spinningAlpha;
        this.spinningBeta = spinningBeta;
        this.difficulty = difficulty;
        this.theme = theme;
    }

    public ConfigBundle() {//this Constructor is required for the yaml file
    }

    public ArrayList<Integer> getAtoms() {
        return atoms;
    }

    public ArrayList<Integer> getPowerups() {
        return powerups;
    }

    public ArrayList<Integer> getMolecules() {
        return molecules;
    }

    public ArrayList<Integer> getBlockers() {
        return blockers;
    }

    public ArrayList<Integer> getShields() {
        return shields;
    }

    /*
    Adding @JsonIgnore annotation to some of the methods below so that numbers of individual elements are ignored
    while writing and reading YAML files, as arrays of these numbers will be read/written under there corresponding
    type.
     */

    @JsonIgnore
    public int getNumOfAlphaAtoms() {
        return atoms.get(0);
    }

    @JsonIgnore
    public int getNumOfBetaAtoms() {
        return atoms.get(1);
    }

    @JsonIgnore
    public int getNumOfGammaAtoms() {
        return atoms.get(2);
    }

    @JsonIgnore
    public int getNumOfSigmaAtoms() {
        return atoms.get(3);
    }

    @JsonIgnore
    public int getNumOfAlphaPowerups() {
        return powerups.get(0);
    }

    @JsonIgnore
    public int getNumOfBetaPowerups() {
        return powerups.get(1);
    }

    @JsonIgnore
    public int getNumOfGammaPowerups() {
        return powerups.get(2);
    }

    @JsonIgnore
    public int getNumOfSigmaPowerups() {
        return powerups.get(3);
    }

    @JsonIgnore
    public int getNumOfAlphaBlockers() {
        return blockers.get(0);
    }

    @JsonIgnore
    public int getNumOfBetaBlockers() {
        return blockers.get(1);
    }

    @JsonIgnore
    public int getNumOfGammaBlockers() {
        return blockers.get(2);
    }

    @JsonIgnore
    public int getNumOfSigmaBlockers() {
        return blockers.get(3);
    }

    @JsonIgnore
    public int getNumOfAlphaMolecules() {
        return molecules.get(0);
    }

    @JsonIgnore
    public int getNumOfBetaMolecules() {
        return molecules.get(1);
    }

    @JsonIgnore
    public int getNumOfGammaMolecules() {
        return molecules.get(2);
    }

    @JsonIgnore
    public int getNumOfSigmaMolecules() {
        return molecules.get(3);
    }

    @JsonIgnore
    public int getNumOfEtaShields() {
        return shields.get(0);
    }

    @JsonIgnore
    public int getNumOfLotaShields() {
        return shields.get(1);
    }

    @JsonIgnore
    public int getNumOfThetaShields() {
        return shields.get(2);
    }

    @JsonIgnore
    public int getNumOfZetaShields() {
        return shields.get(3);
    }

    public double getL() {
        return l;
    }

    public boolean isLinearAlpha() {
        return linearAlpha;
    }

    public boolean isLinearBeta() {
        return linearBeta;
    }

    public boolean isSpinningAlpha() {
        return spinningAlpha;
    }

    public boolean isSpinningBeta() {
        return spinningBeta;
    }

    public int getDifficulty() {
        return difficulty;
    }

    @JsonIgnore
    public String getDifficultyString(){
        switch (getDifficulty()){
            case 0:
                return "Easy";
            case 1:
                return "Normal";
            default:
                return "Hard";
        }
    }

    public String getTheme() {
        return theme;
    }
}
