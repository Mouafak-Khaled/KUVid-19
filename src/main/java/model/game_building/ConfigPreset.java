package model.game_building;

import model.game_running.listeners.PresetSelectionListener;
import services.utils.IOHandler;

import java.io.IOException;

public class ConfigPreset {
    private final PresetSelectionListener listener;

    public ConfigPreset(PresetSelectionListener listener) {
        this.listener = listener;
    }

    /**
     * given a filename retrieve the bundle from the file and pass it to the listener
     * @param filename
     * @throws IOException
     */
    public void getConfigBundleFromFile(String filename) throws IOException {
        ConfigBundle bundle = IOHandler.readFromYaml(filename, "configurations", ConfigBundle.class);
        if (bundle == null)
            listener.onPresetsFailure("could not load preset configurations!");
        else
            listener.onSelectedPreset(bundle);
    }


}
