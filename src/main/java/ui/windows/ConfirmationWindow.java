package ui.windows;

import model.game_building.ConfigBundle;
import model.game_building.ConfigConfirmation;
import model.game_building.GameConstants;
import model.game_running.listeners.ParametersConfirmationListener;

import javax.swing.*;
import java.awt.*;

public class ConfirmationWindow extends JFrame implements ParametersConfirmationListener {
    ConfigConfirmation configConfirmation;
    JFrame buildingGameFrame;
    JPanel panel;

    public ConfirmationWindow(JFrame frame, ConfigBundle bundle, boolean isSaving) {
        super("Confirm Config");
        this.buildingGameFrame = frame;
        this.configConfirmation = new ConfigConfirmation(this);

        panel = new JPanel();
        this.setContentPane(panel);
        placeComponents(panel, bundle, isSaving);

        // Fits the borders to the content
        // Setting the frame visibility to true.
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null); // centers the window in the middle of the screen
        this.setVisible(true);
    }

    private void placeComponents(JPanel panel, ConfigBundle bundle, boolean isSaving){
        // Setting the layout of the panel
        panel.setLayout(new GridLayout(14, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Summary"));

        // Atoms labels
        JLabel alphaAtomLabel = new JLabel("Alpha Atoms: " + bundle.getNumOfAlphaAtoms());
        panel.add(alphaAtomLabel);

        JLabel betaAtomLabel = new JLabel("Beta Atoms: " + bundle.getNumOfBetaAtoms());
        panel.add(betaAtomLabel);


        JLabel gammaAtomLabel = new JLabel("Gamma Atoms: " + bundle.getNumOfGammaAtoms());
        panel.add(gammaAtomLabel);


        JLabel sigmaAtomLabel = new JLabel("Sigma Atoms: " + bundle.getNumOfSigmaAtoms());
        panel.add(sigmaAtomLabel);


        // Powerup labels
        JLabel alphaPowerupLabel = new JLabel("Alpha Powerups:" + bundle.getNumOfAlphaAtoms());
        panel.add(alphaPowerupLabel);


        JLabel betaPowerupLabel = new JLabel("Beta Powerups: " + bundle.getNumOfBetaAtoms());
        panel.add(betaPowerupLabel);


        JLabel gammaPowerupLabel = new JLabel("Gamma Powerups: " + bundle.getNumOfGammaAtoms());
        panel.add(gammaPowerupLabel);


        JLabel sigmaPowerupLabel = new JLabel("Sigma Powerups: " + bundle.getNumOfSigmaAtoms());
        panel.add(sigmaPowerupLabel);



        // Blocker labels
        JLabel alphaBlockerLabel = new JLabel("Alpha Blockers: " + bundle.getNumOfAlphaBlockers());
        panel.add(alphaBlockerLabel);


        JLabel betaBlockerLabel = new JLabel("Beta Blockers: " + bundle.getNumOfBetaBlockers());
        panel.add(betaBlockerLabel);


        JLabel gammaBlockerLabel = new JLabel("Gamma Blockers: " + bundle.getNumOfGammaAtoms());
        panel.add(gammaBlockerLabel);


        JLabel sigmaBlockerLabel = new JLabel("Sigma Blockers: " + bundle.getNumOfSigmaBlockers());
        panel.add(sigmaBlockerLabel);


        // Molecule labels
        JLabel alphaMoleculeLabel = new JLabel("Alpha Molecules: " + bundle.getNumOfAlphaMolecules());
        panel.add(alphaMoleculeLabel);


        JLabel betaMoleculeLabel = new JLabel("Beta Molecules: " + bundle.getNumOfBetaMolecules());
        panel.add(betaMoleculeLabel);


        JLabel gammaMoleculeLabel = new JLabel("Gamma Molecules: " + bundle.getNumOfGammaMolecules());
        panel.add(gammaMoleculeLabel);


        JLabel sigmaMoleculeLabel = new JLabel("Sigma Molecules: " + bundle.getNumOfSigmaMolecules());
        panel.add(sigmaMoleculeLabel);


        // Shield Labels
        JLabel etaShieldLabel = new JLabel("Eta Shields: " + bundle.getNumOfEtaShields());
        panel.add(etaShieldLabel);


        JLabel lotaShieldLabel = new JLabel("Lota Shields: " + bundle.getNumOfLotaShields());
        panel.add(lotaShieldLabel);


        JLabel thetaShieldLabel = new JLabel("Theta Shields: " + bundle.getNumOfThetaShields());
        panel.add(thetaShieldLabel);


        JLabel zetaShieldLabel = new JLabel("Zeta Shields: " + bundle.getNumOfZetaShields());
        panel.add(zetaShieldLabel);

        JLabel linearAlphaMoleculues = new JLabel("Linear Alpha Molecules: " + (bundle.isLinearAlpha() ? "Yes" : "No"));
        panel.add(linearAlphaMoleculues);

        JLabel spinningAlphaMoleculues = new JLabel("Spinning Alpha Molecules: " + (bundle.isSpinningAlpha() ? "Yes" : "No"));
        panel.add(spinningAlphaMoleculues);

        JLabel betaAlphaMoleculues = new JLabel("Linear Beta Molecules: " + (bundle.isLinearBeta() ? "Yes" : "No"));
        panel.add(betaAlphaMoleculues);

        JLabel spinningBetaMoleculues = new JLabel("Spinning Beta Molecules: " + (bundle.isSpinningBeta() ? "Yes" : "No"));
        panel.add(spinningBetaMoleculues);

        // Length label
        JLabel lengthLabel = new JLabel("L unit " + bundle.getL());
        panel.add(lengthLabel);

        // Difficulty label
        JLabel difficultyLabel = new JLabel("Difficulty: " + bundle.getDifficultyString());
        panel.add(difficultyLabel);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            configConfirmation.confirm(bundle, isSaving);
        });

        JButton rejectButton = new JButton("Cancel");
        rejectButton.addActionListener(e -> {
            this.dispose();
        });

        panel.add(confirmButton);
        panel.add(rejectButton);
    }

    @Override
    public void onConfirmedParameters() {
        // Close the current game-building frame.
        buildingGameFrame.dispose();
        this.dispose();

        new RunningWindow(GameConstants.GAME_TITLE); //todo: maybe start somewhere else?
    }
}
