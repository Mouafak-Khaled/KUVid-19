package model.game_running.listeners;

import model.game_building.ConfigBundle;

import java.util.ArrayList;

public interface ParametersValidationListener {
    /**
     * this method is called after game parameters get checked and proved valid.
     */
    void onValidParameters(ConfigBundle bundle);

    /**
     * this method is called after game parameters get checked and proved invalid.
     *
     * @param message takes a message indicating that some parameters are invalid as well as those parameters.
     */
    void onInvalidParameters(ArrayList<String> message);
}
