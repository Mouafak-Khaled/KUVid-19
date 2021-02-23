package model.game_running.listeners;

import model.game_building.ConfigBundle;

public interface PresetSelectionListener {
    /**
     * this method is called after game parameters get checked and proved valid.
     */
    void onSelectedPreset(ConfigBundle bundle);

    /**
     * if the user has no presets saved.
     */
    void onPresetsFailure(String message);
}
