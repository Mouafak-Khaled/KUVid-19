package model.game_building;


import model.game_running.listeners.ParametersValidationListener;
import services.utils.IOHandler;

import java.io.IOException;
import java.util.ArrayList;

public class BuildingMode {

    private final ParametersValidationListener validationListener;
    // This array will carry the invalid fields and we will use it to check if there are any invalid fields.
    private final ArrayList<String> invalidFields;

    public BuildingMode(ParametersValidationListener validationListener) {
        this.validationListener = validationListener;
        invalidFields = new ArrayList<>();
    }

    public static ConfigBundle getDefaultBundle() throws IOException {
        return IOHandler.readFromYaml("Default","configurations" , ConfigBundle.class);
    }

    //TODO: specify the wrong field in the warning popup window

    /**
     * this method is to check if all the parameters of the object ConfigBundle are valid or not at the same time.
     */
    public void validateParameters(ConfigBundle bundle) {
        this.invalidFields.clear();
        if (!isValidNumber(bundle.getNumOfAlphaAtoms()))
            invalidFields.add("Invalid number of Alpha Atoms\n");
        if (!isValidNumber(bundle.getNumOfBetaAtoms()))
            invalidFields.add("Invalid number of Beta Atoms\n");
        if (!isValidNumber(bundle.getNumOfGammaAtoms()))
            invalidFields.add("Invalid number of Gamma Atoms\n");
        if (!isValidNumber(bundle.getNumOfSigmaAtoms()))
            invalidFields.add("Invalid number of Sigma Atoms\n");

        if (!isValidNumber(bundle.getNumOfAlphaPowerups()))
            invalidFields.add("Invalid number of Alpha Powerups\n");
        if (!isValidNumber(bundle.getNumOfBetaPowerups()))
            invalidFields.add("Invalid number of Beta Powerups\n");
        if (!isValidNumber(bundle.getNumOfGammaPowerups()))
            invalidFields.add("Invalid number of Gamma Powerups\n");
        if (!isValidNumber(bundle.getNumOfSigmaPowerups()))
            invalidFields.add("Invalid number of Sigma Powerups\n");

        if (!isValidNumber(bundle.getNumOfAlphaBlockers()))
            invalidFields.add("Invalid number of Alpha Blockers\n");
        if (!isValidNumber(bundle.getNumOfBetaBlockers()))
            invalidFields.add("Invalid number of Beta Blockers\n");
        if (!isValidNumber(bundle.getNumOfGammaBlockers()))
            invalidFields.add("Invalid number of Gamma Blockers\n");
        if (!isValidNumber(bundle.getNumOfSigmaBlockers()))
            invalidFields.add("Invalid number of Sigma Blockers\n");

        if (!isValidNumber(bundle.getNumOfAlphaMolecules()))
            invalidFields.add("Invalid number of Alpha Molecules\n");
        if (!isValidNumber(bundle.getNumOfBetaMolecules()))
            invalidFields.add("Invalid number of Beta Molecules\n");
        if (!isValidNumber(bundle.getNumOfGammaMolecules()))
            invalidFields.add("Invalid number of Gamma Molecules\n");
        if (!isValidNumber(bundle.getNumOfSigmaMolecules()))
            invalidFields.add("Invalid number of Sigma Molecules\n");

        if (!isValidNumber(bundle.getNumOfEtaShields()))
            invalidFields.add("Invalid number of Eta Shields\n");
        if (!isValidNumber(bundle.getNumOfLotaShields()))
            invalidFields.add("Invalid number of Lota Shields\n");
        if (!isValidNumber(bundle.getNumOfThetaShields()))
            invalidFields.add("Invalid number of Theta Shields\n");
        if (!isValidNumber(bundle.getNumOfZetaShields()))
            invalidFields.add("Invalid number of Zeta Shields\n");

        if (!isValidDifficulty(bundle.getDifficulty()))
            invalidFields.add("Invalid Difficulty number\n");
        if (!isValidLength(bundle.getL()))
            invalidFields.add("Invalid Value for L\n");
        if (invalidFields.isEmpty())
            validationListener.onValidParameters(bundle);
        else {
            validationListener.onInvalidParameters(invalidFields);
        }

    }


    /**
     * this method to check if a given integer is greater than 0 to be valid according to the game's rules
     *
     * @param number the number to be validated
     * @return true if number > 0
     */
    private boolean isValidNumber(int number) {
        return number > 0;
    }


    /**
     * this method is to check if the value of the given integer in the range [0,2].
     *
     * @param difficulty a number indicating the difficulty level
     * @return true if difficulty is in range [0,2].
     */
    private boolean isValidDifficulty(int difficulty) {
        return difficulty >= 0 && difficulty <= 2;
    }


    /**
     * this method to check if a given integer is greater than 0 to be valid according to the game's rules
     *
     * @param l the length that will be validated
     * @return true if l > 0.
     */
    private boolean isValidLength(double l) {
        return l > 0;
    }


}
