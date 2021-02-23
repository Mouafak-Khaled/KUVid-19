package ui.windows;

import model.game_building.GameConstants;
import model.game_entities.enums.EntityType;
import model.game_running.RunningMode;
import model.game_running.listeners.BlenderListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BlenderWindow extends JFrame implements BlenderListener {
    private JPanel contentPane; // TODO: delete this and use getContentPane() instead.

    // Labels
    private JLabel sourceLabel, destinationLabel, destinationQuantityLabel, errorLabel;

    // ComboBoxes
    private JComboBox sourceComboBox, destinationComboBox;

    private JTextField destinationQuantityField;
    private JButton blendButton;
    private RunningMode runningMode;

    public BlenderWindow(RunningMode runningMode) {
        super("blender");
        runningMode.getBlender().setBlenderListener(this); // Pass this listener to Blender for the observer pattern
        this.runningMode = runningMode;
        this.addOnBlenderCloseListener(runningMode);

        this.contentPane = new JPanel();
        this.setSize(GameConstants.BLENDER_WINDOW_SIZE);
        getContentPane().add(contentPane);
        addComponents(contentPane); // Add components to the panel
        this.pack(); // Pack the frame around the components
        setLocationRelativeTo(null); // Center the blender frame
        this.setVisible(false); // Keep it invisible by default
    }

    /**
     * Add all the components to the content panel
     *
     * @param contentPane to which the components will be added
     */
    private void addComponents(JPanel contentPane) {
        sourceLabel = new JLabel("Source");
        sourceComboBox = new JComboBox(EntityType.stringValues());
        destinationLabel = new JLabel("Destination");
        destinationComboBox = new JComboBox(EntityType.stringValues());
        destinationQuantityLabel = new JLabel("Quantity");
        destinationQuantityField = new JTextField(3);
        destinationQuantityField.setText("1");
        blendButton = new JButton("Blend");
        addButtonActionListener(blendButton);

        contentPane.add(sourceLabel);
        contentPane.add(sourceComboBox);
        contentPane.add(destinationLabel);
        contentPane.add(destinationComboBox);
        contentPane.add(destinationQuantityLabel);
        contentPane.add(destinationQuantityField);
        contentPane.add(blendButton);
    }

    /**
     * Add a listener to the blender window to resume the game if we close it with the x button
     *
     * @param runningMode
     */
    private void addOnBlenderCloseListener(RunningMode runningMode) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                runningMode.resume();
            }
        });
    }

    /**
     * add action listener to the button to call the blend method on the domain object
     *
     * @param button to which the listener will be added
     */
    private void addButtonActionListener(JButton button) {
        button.addActionListener(e -> {
            int sourceRank = sourceComboBox.getSelectedIndex();
            int destinationRank = destinationComboBox.getSelectedIndex();
            int numOfConversions;
            try {
                numOfConversions = Integer.parseInt(destinationQuantityField.getText());
            } catch (NumberFormatException exception) {
                numOfConversions = 1;
            }

            try {
                runningMode.getBlender().convert(sourceRank, destinationRank, numOfConversions);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public void onBlend() {
        this.setVisible(false);
        runningMode.resume();
        // this.dispose();
    }

    @Override
    public void onFailBlend() {
        if (errorLabel != null)
            this.contentPane.remove(errorLabel); // Remove the error message before displaying a new one
        errorLabel = new JLabel("Not enough Atoms of type " + sourceComboBox.getSelectedItem().toString());
        errorLabel.setForeground(Color.red);
        this.contentPane.add(errorLabel);
        this.contentPane.revalidate();
        this.pack(); // re-pack the frame to the elements after adding the new one
    }

    @Override
    public void onShow() {
        this.setVisible(true);
    }
}
