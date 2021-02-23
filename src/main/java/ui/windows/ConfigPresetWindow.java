package ui.windows;

import model.game_building.ConfigBundle;
import model.game_building.ConfigPreset;
import model.game_running.listeners.PresetSelectionListener;
import org.apache.log4j.Logger;
import services.exceptions.UnsupportedNameFormatException;
import services.utils.IOHandler;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;

public class ConfigPresetWindow extends JFrame implements PresetSelectionListener {
    private final ConfigPreset configPreset;
    private final JFrame buildingGameFrame;
    private JList<String> configurationFilesList;

    private static Logger logger;

    public ConfigPresetWindow(JFrame parent) {
        super("Preset Selection");
        logger = Logger.getLogger(getClass().getName());
        this.configPreset = new ConfigPreset(this);
        this.buildingGameFrame = parent;
        this.setContentPane(new JPanel());
        boolean presetsLoaded = this.addComponents(IOHandler.getFilesInDirectory("configurations"));
        this.pack(); // Pack the frame around the components
        this.setLocationRelativeTo(null); // Center the blender frame
        if (presetsLoaded)
            this.setVisible(true);
    }

    /**
     * adds the list of saved presets to the window. displays an error if the list is empty
     *
     * @param fileNames list of preset file names to be added to the window
     * @return true if file names were added to the list, false otherwise (no saved presets).
     */
    private boolean addComponents(String[] fileNames) {
        if (fileNames.length == 0) {//if there are no presets.
            onPresetsFailure("No Presets Found!");
            return false;
        }
        //create list
        configurationFilesList = new JList<>();
        fileNames = Arrays.stream(fileNames)
                .filter(name -> name.endsWith(".yaml"))
                .map(name -> {
                    try {
                        return IOHandler.prettifyYAMLFileName(name);
                    } catch (UnsupportedNameFormatException e) {
                        logger.error(e.getMessage(), e);
                        return "";
                    }
                })
                .filter(n -> !n.isEmpty()) //do not show files that threw unsupported name format exception
                .toArray(String[]::new);

        configurationFilesList.setListData(fileNames);


        JButton confirmPresetButton = new JButton("Confirm Preset");
        confirmPresetButton.addActionListener(e -> {
            String properFileName = IOHandler.prettyToProperFileName(configurationFilesList.getSelectedValue());
            try {
                configPreset.getConfigBundleFromFile(properFileName);
            } catch (IOException exception) {
                exception.printStackTrace();
                onPresetsFailure("Could not find the selected preset file");
            }
        });
        //set a selected index
        configurationFilesList.setSelectedIndex(0);

        //add list to panel
        this.getContentPane().add(configurationFilesList);
        this.getContentPane().add(confirmPresetButton);
        return true;
    }

    @Override
    public void onSelectedPreset(ConfigBundle bundle) {
        // Load the bundle into the buildingWindow
        ((BuildingWindow) buildingGameFrame).loadPresetParameters(bundle);
        // Close the frame
        this.dispose();
    }

    @Override
    public void onPresetsFailure(String message) {
        // Close the current game-building frame.
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        // Close the frame
        this.dispose();
    }
}
