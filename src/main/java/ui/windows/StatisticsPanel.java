package ui.windows;

import model.game_building.Configuration;
import model.game_building.GameConstants;
import model.game_entities.enums.EntityType;
import model.game_entities.enums.ShieldType;
import model.game_entities.enums.SuperType;
import model.game_running.ProjectileContainer;
import model.game_running.RunningMode;
import model.game_running.ShieldHandler;
import model.game_running.listeners.GameStatisticsListener;
import services.utils.MathUtils;
import ui.movable_drawables.ImageResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class draws the Statistics window. through this window, the player
 * can observe the statistics of the game such as atom/power numbers and score...
 */
public class StatisticsPanel extends JPanel implements GameStatisticsListener {

    private final RunningMode runningMode;
    private final Configuration config;

    //icons
    private JLabel healthLabel, timeLabel, SCORE, scoreLabel, blenderButton;
    private ImageIcon healthImg, watchImg, blenderImg, blenderImg_bg;

    //atoms
    private JLabel gammaAtomsNumberLabel, alphaAtomsNumberLabel, betaAtomsNumberLabel, sigmaAtomsNumberLabel;
    private ImageIcon atomAlphaImg, atomBetaImg, atomSigmaImg, atomGammaImg;

    //powerups
    private JLabel gammaPowerupsNumberLabel, alphaPowerupsNumberLabel, betaPowerupsNumberLabel, sigmaPowerupsNumberLabel;
    private JLabel alphaPowerupButton, betaPowerupButton, gammaPowerupButton, sigmaPowerupButton;
    private ImageIcon powerupAlphaImg_bg, powerupBetaImg_bg, powerupSigmaImg_bg, powerupGammaImg_bg;
    private ImageIcon powerupAlphaImg, powerupBetaImg, powerupSigmaImg, powerupGammaImg;

    //shields
    private JLabel etaNumberLabel, lotaNumberLabel, thetaNumberLabel, zetaNumberLabel;
    private JLabel etaButton, lotaButton, thetaButton, zetaButton;
    private ImageIcon etaImg_bg, lotaImg_bg, thetaImg_bg, zetaImg_bg;
    private ImageIcon etaImg, lotaImg, thetaImg, zetaImg;

    //iconSize
    private final int iconSize;

    //mouse adapters
    private MouseAdapter blenderAdapter;
    private MouseAdapter alphaPowerupAdapter, betaPowerupAdapter, gammaPowerupAdapter, sigmaPowerupAdapter;
    private MouseAdapter etaAdapter, lotaAdapter, thetaAdapter, zetaAdapter;
    
    public StatisticsPanel(RunningMode runningMode) {
        this.setPreferredSize(Configuration.getInstance().getStatisticsPanelDimensions());
        this.config = Configuration.getInstance();
        this.iconSize = (int) (config.getUnitL() * GameConstants.ICON_SIZE);
        //Controller
        this.runningMode = runningMode;
        GridBagLayout gridLayout = new GridBagLayout();
        setLayout(gridLayout);
        setOpaque(false);

        retrieveImages();
        initializeTextLabels();
        initializeButtons();
        if (config.isDiscoTheme())
            setDiscoThemeContent();
        else
            setPepegaThemeContent();
        setButtonListeners();

    }

    /**
     * retrieves images from ImageResources with the specified height and width
     */
    private void retrieveImages() {
        atomAlphaImg = ImageResources.getStatIcon(SuperType.ATOM + "", EntityType.ALPHA + "", iconSize, false);
        atomBetaImg = ImageResources.getStatIcon(SuperType.ATOM + "", EntityType.BETA + "", iconSize, false);
        atomSigmaImg = ImageResources.getStatIcon(SuperType.ATOM + "", EntityType.SIGMA + "", iconSize, false);
        atomGammaImg = ImageResources.getStatIcon(SuperType.ATOM + "", EntityType.GAMMA + "", iconSize, false);

        powerupAlphaImg = ImageResources.getStatIcon(SuperType.POWERUP + "", EntityType.ALPHA + "", iconSize, false);
        powerupBetaImg = ImageResources.getStatIcon(SuperType.POWERUP + "", EntityType.BETA + "", iconSize, false);
        powerupSigmaImg = ImageResources.getStatIcon(SuperType.POWERUP + "", EntityType.SIGMA + "", iconSize, false);
        powerupGammaImg = ImageResources.getStatIcon(SuperType.POWERUP + "", EntityType.GAMMA + "", iconSize, false);

        powerupAlphaImg_bg = ImageResources.getStatIcon(SuperType.POWERUP + "", EntityType.ALPHA + "", iconSize, true);
        powerupBetaImg_bg = ImageResources.getStatIcon(SuperType.POWERUP + "", EntityType.BETA + "", iconSize, true);
        powerupSigmaImg_bg = ImageResources.getStatIcon(SuperType.POWERUP + "", EntityType.SIGMA + "", iconSize, true);
        powerupGammaImg_bg = ImageResources.getStatIcon(SuperType.POWERUP + "", EntityType.GAMMA + "", iconSize, true);

        healthImg = ImageResources.getStatIcon("icons", "health", iconSize / 3 * 2, false);
        watchImg = ImageResources.getStatIcon("icons", "timer", iconSize / 3 * 2, false);
        blenderImg = ImageResources.getStatIcon("icons", "blender", iconSize * 2, false);
        blenderImg_bg = ImageResources.getStatIcon("icons", "blender", iconSize * 2, true);

        etaImg = ImageResources.getStatIcon(SuperType.SHIELD + "", ShieldType.ETA + "", iconSize, false);
        lotaImg = ImageResources.getStatIcon(SuperType.SHIELD + "", ShieldType.LOTA + "", iconSize, false);
        thetaImg = ImageResources.getStatIcon(SuperType.SHIELD + "", ShieldType.THETA + "", iconSize, false);
        zetaImg = ImageResources.getStatIcon(SuperType.SHIELD + "", ShieldType.ZETA + "", iconSize, false);

        etaImg_bg = ImageResources.getStatIcon(SuperType.SHIELD + "", ShieldType.ETA + "", iconSize, true);
        lotaImg_bg = ImageResources.getStatIcon(SuperType.SHIELD + "", ShieldType.LOTA + "", iconSize, true);
        thetaImg_bg = ImageResources.getStatIcon(SuperType.SHIELD + "", ShieldType.THETA + "", iconSize, true);
        zetaImg_bg = ImageResources.getStatIcon(SuperType.SHIELD + "", ShieldType.ZETA + "", iconSize, true);
    }

    /**
     * initializes TextField that correspond to number of atoms/powerups, score, health, and time
     */
    private void initializeTextLabels() {
        ProjectileContainer container = this.runningMode.getProjectileContainer();
        int fontSize = (int) (iconSize * 0.6);
        int smallFontSize = (int) (iconSize * 0.35);
        Font normalFont = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
        Font smallFont = new Font(Font.SANS_SERIF, Font.BOLD, smallFontSize);

        alphaAtomsNumberLabel = new JLabel(String.valueOf(container.getAtomCountForType(EntityType.ALPHA)));
        alphaAtomsNumberLabel.setForeground(Color.DARK_GRAY);
        alphaAtomsNumberLabel.setFont(normalFont);
        betaAtomsNumberLabel = new JLabel(String.valueOf(container.getAtomCountForType(EntityType.BETA)));
        betaAtomsNumberLabel.setForeground(Color.DARK_GRAY);
        betaAtomsNumberLabel.setFont(normalFont);
        gammaAtomsNumberLabel = new JLabel(String.valueOf(container.getAtomCountForType(EntityType.GAMMA)));
        gammaAtomsNumberLabel.setForeground(Color.DARK_GRAY);
        gammaAtomsNumberLabel.setFont(normalFont);
        sigmaAtomsNumberLabel = new JLabel(String.valueOf(container.getAtomCountForType(EntityType.SIGMA)));
        sigmaAtomsNumberLabel.setForeground(Color.DARK_GRAY);
        sigmaAtomsNumberLabel.setFont(normalFont);

        gammaPowerupsNumberLabel = new JLabel("0");
        gammaPowerupsNumberLabel.setForeground(Color.white);
        gammaPowerupsNumberLabel.setFont(normalFont);
        alphaPowerupsNumberLabel = new JLabel("0");
        alphaPowerupsNumberLabel.setForeground(Color.white);
        alphaPowerupsNumberLabel.setFont(normalFont);
        betaPowerupsNumberLabel = new JLabel("0");
        betaPowerupsNumberLabel.setForeground(Color.white);
        betaPowerupsNumberLabel.setFont(normalFont);
        sigmaPowerupsNumberLabel = new JLabel("0");
        sigmaPowerupsNumberLabel.setForeground(Color.white);
        sigmaPowerupsNumberLabel.setFont(normalFont);

        etaNumberLabel = new JLabel("" + config.getNumOfEtaShields());
        etaNumberLabel.setForeground(Color.black);
        etaNumberLabel.setOpaque(true);
        etaNumberLabel.setBackground(Color.lightGray);
        etaNumberLabel.setFont(smallFont);
        lotaNumberLabel = new JLabel("" + config.getNumOfLotaShields());
        lotaNumberLabel.setForeground(Color.black);
        lotaNumberLabel.setOpaque(true);
        lotaNumberLabel.setBackground(Color.lightGray);
        lotaNumberLabel.setFont(smallFont);
        thetaNumberLabel = new JLabel("" + config.getNumOfThetaShields());
        thetaNumberLabel.setForeground(Color.black);
        thetaNumberLabel.setOpaque(true);
        thetaNumberLabel.setBackground(Color.lightGray);
        thetaNumberLabel.setFont(smallFont);
        zetaNumberLabel = new JLabel("" + config.getNumOfZetaShields());
        zetaNumberLabel.setForeground(Color.black);
        zetaNumberLabel.setOpaque(true);
        zetaNumberLabel.setBackground(Color.lightGray);
        zetaNumberLabel.setFont(smallFont);

        healthLabel = new JLabel("100%");
        healthLabel.setForeground(Color.red);
        healthLabel.setFont(normalFont);
        timeLabel = new JLabel("10 : 00");
        timeLabel.setForeground(Color.white);
        timeLabel.setFont(normalFont);
        SCORE = new JLabel("Score: ");
        SCORE.setForeground(Color.white);
        SCORE.setFont(normalFont);
        scoreLabel = new JLabel("0.0");
        scoreLabel.setForeground(Color.white);
        scoreLabel.setFont(normalFont);
    }

    private void initializeButtons() {
        blenderButton = new JLabel(blenderImg);
        alphaPowerupButton = new JLabel(powerupAlphaImg);
        betaPowerupButton = new JLabel(powerupBetaImg);
        gammaPowerupButton = new JLabel(powerupGammaImg);
        sigmaPowerupButton = new JLabel(powerupSigmaImg);

        etaButton = new JLabel(etaImg);
        lotaButton = new JLabel(lotaImg);
        thetaButton = new JLabel(thetaImg);
        zetaButton = new JLabel(zetaImg);
    }

    private void setPepegaThemeContent() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipady = 6;
        //x = 0
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 0;

        gridBagConstraints.gridy = 0;
        add(SCORE, gridBagConstraints);
        gridBagConstraints.gridy = 1;
        add(new JLabel(watchImg), gridBagConstraints);
        gridBagConstraints.gridy = 2;
        add(new JLabel(healthImg), gridBagConstraints);
        gridBagConstraints.gridy = 3;
        add(alphaPowerupButton, gridBagConstraints);
        gridBagConstraints.gridy = 4;
        add(betaPowerupButton, gridBagConstraints);
        gridBagConstraints.gridy = 5;
        add(gammaPowerupButton, gridBagConstraints);
        gridBagConstraints.gridy = 6;
        add(sigmaPowerupButton, gridBagConstraints);

        //x = 1
        gridBagConstraints.fill = GridBagConstraints.CENTER;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridy = 0;
        add(scoreLabel, gridBagConstraints);
        gridBagConstraints.gridy = 1;
        add(timeLabel, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        add(healthLabel, gridBagConstraints);
        gridBagConstraints.gridy = 3;
        add(alphaPowerupsNumberLabel, gridBagConstraints);
        gridBagConstraints.gridy = 4;
        add(betaPowerupsNumberLabel, gridBagConstraints);
        gridBagConstraints.gridy = 5;
        add(gammaPowerupsNumberLabel, gridBagConstraints);
        gridBagConstraints.gridy = 6;
        add(sigmaPowerupsNumberLabel, gridBagConstraints);

        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridx = 1;
        add(blenderButton, gridBagConstraints);

        //x = 0
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridy = 8;
        add(alphaAtomsNumberLabel, gridBagConstraints);
        gridBagConstraints.gridy = 9;
        add(betaAtomsNumberLabel, gridBagConstraints);
        gridBagConstraints.gridy = 10;
        add(gammaAtomsNumberLabel, gridBagConstraints);
        gridBagConstraints.gridy = 11;
        add(sigmaAtomsNumberLabel, gridBagConstraints);

        //x = 1
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        add(new JLabel(atomAlphaImg), gridBagConstraints);
        gridBagConstraints.gridy = 9;
        add(new JLabel(atomBetaImg), gridBagConstraints);
        gridBagConstraints.gridy = 10;
        add(new JLabel(atomGammaImg), gridBagConstraints);
        gridBagConstraints.gridy = 11;
        add(new JLabel(atomSigmaImg), gridBagConstraints);

        //x = 2
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        add(etaButton, gridBagConstraints);
        gridBagConstraints.gridy = 9;
        add(lotaButton, gridBagConstraints);
        gridBagConstraints.gridy = 10;
        add(thetaButton, gridBagConstraints);
        gridBagConstraints.gridy = 11;
        add(zetaButton, gridBagConstraints);

        //x = 3
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        add(etaNumberLabel, gridBagConstraints);
        gridBagConstraints.gridy = 9;
        add(lotaNumberLabel, gridBagConstraints);
        gridBagConstraints.gridy = 10;
        add(thetaNumberLabel, gridBagConstraints);
        gridBagConstraints.gridy = 11;
        add(zetaNumberLabel, gridBagConstraints);

        setButtonListeners();

    }

    /**
     * Add images and numbers to the window
     */
    private void setDiscoThemeContent() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        double fiveUnits = (1.0 / 9.0) * iconSize;

        gridBagConstraints.insets = new Insets((int) (7 * fiveUnits), 0, 0, 0);
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.gridwidth = 2;

        //score
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.weighty = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(SCORE, gridBagConstraints);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(scoreLabel, gridBagConstraints);

        //health and time
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new Insets((int) (2 * fiveUnits), 0, 0, 0);
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(new JLabel(healthImg), gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(healthLabel, gridBagConstraints);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(new JLabel(watchImg), gridBagConstraints);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets((int) (2 * fiveUnits), 0, 0, (int) (2 * fiveUnits));
        add(timeLabel, gridBagConstraints);

        //powerups
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.insets = new Insets((int) (14 * fiveUnits), (int) (10 * fiveUnits), 0, 0);
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(alphaPowerupsNumberLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.insets = new Insets((int) (14 * fiveUnits), 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        add(alphaPowerupButton, gridBagConstraints);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        add(sigmaPowerupsNumberLabel, gridBagConstraints);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(sigmaPowerupButton, gridBagConstraints);

        gridBagConstraints.insets = new Insets((int) (4 * fiveUnits), (int) (10 * fiveUnits), 0, 0);
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(betaPowerupsNumberLabel, gridBagConstraints);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.insets = new Insets((int) (4 * fiveUnits), 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        add(betaPowerupButton, gridBagConstraints);
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        add(gammaPowerupsNumberLabel, gridBagConstraints);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(gammaPowerupButton, gridBagConstraints);

        //blender
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.insets = new Insets((int) (16 * fiveUnits), (int) (12 * fiveUnits), (int) (2 * fiveUnits), 0);
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 4;
        add(blenderButton, gridBagConstraints);

        //atom labels and shields buttons
        int leftMargin = (int) (3 * fiveUnits);
        int leftMargin2 = (int) (6 * fiveUnits);
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.insets = new Insets((int) (6 * fiveUnits), leftMargin, 0, 0);
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridx = 1;
        add(alphaAtomsNumberLabel, gridBagConstraints);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.insets = new Insets((int) (8 * fiveUnits), leftMargin2, 0, 0);
        add(etaButton, gridBagConstraints);

        gridBagConstraints.insets = new Insets(0, leftMargin, 0, 0);
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridx = 1;
        add(betaAtomsNumberLabel, gridBagConstraints);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.insets = new Insets(0, leftMargin2, 0, 0);
        add(lotaButton, gridBagConstraints);

        gridBagConstraints.insets = new Insets(0, leftMargin, 0, 0);
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridx = 1;
        add(gammaAtomsNumberLabel, gridBagConstraints);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.insets = new Insets(0, leftMargin2, 0, 0);
        add(thetaButton, gridBagConstraints);

        gridBagConstraints.insets = new Insets(0, leftMargin, 0, 0);
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridx = 1;
        add(sigmaAtomsNumberLabel, gridBagConstraints);
        gridBagConstraints.gridx = 3;
        gridBagConstraints.insets = new Insets(0, leftMargin2, 0, 0);
        add(zetaButton, gridBagConstraints);

        //shields labels
        gridBagConstraints.insets = new Insets((int) (0.8 * fiveUnits), leftMargin2, 0, 0);
        gridBagConstraints.weighty = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridx = 3;
        add(etaNumberLabel, gridBagConstraints);
        gridBagConstraints.insets = new Insets(0, leftMargin2, 0, 0);
        gridBagConstraints.gridy = 8;
        add(lotaNumberLabel, gridBagConstraints);
        gridBagConstraints.gridy = 10;
        add(thetaNumberLabel, gridBagConstraints);
        gridBagConstraints.insets = new Insets(0, leftMargin2, (int) (4 * fiveUnits), 0);
        gridBagConstraints.gridy = 12;
        add(zetaNumberLabel, gridBagConstraints);
    }

    private void setButtonListeners() {
        //blender listener
        blenderAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                runningMode.pause();
                runningMode.getBlender().showBlender();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                blenderButton.setIcon(blenderImg_bg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                blenderButton.setIcon(blenderImg);
            }
        };
        blenderButton.addMouseListener(blenderAdapter);

        //power-ups listeners
        alphaPowerupAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                runningMode.getShooter().setPowerup(EntityType.ALPHA);

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                alphaPowerupButton.setIcon(powerupAlphaImg_bg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                alphaPowerupButton.setIcon(powerupAlphaImg);
            }
        };
        betaPowerupAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                runningMode.getShooter().setPowerup(EntityType.BETA);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                betaPowerupButton.setIcon(powerupBetaImg_bg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                betaPowerupButton.setIcon(powerupBetaImg);
            }
        };
        sigmaPowerupAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                runningMode.getShooter().setPowerup(EntityType.SIGMA);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                sigmaPowerupButton.setIcon(powerupSigmaImg_bg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                sigmaPowerupButton.setIcon(powerupSigmaImg);
            }
        };
        gammaPowerupAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                runningMode.getShooter().setPowerup(EntityType.GAMMA);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                gammaPowerupButton.setIcon(powerupGammaImg_bg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                gammaPowerupButton.setIcon(powerupGammaImg);
            }
        };
        alphaPowerupButton.addMouseListener(alphaPowerupAdapter);
        betaPowerupButton.addMouseListener(betaPowerupAdapter);
        sigmaPowerupButton.addMouseListener(sigmaPowerupAdapter);
        gammaPowerupButton.addMouseListener(gammaPowerupAdapter);

        //shields listeners
        etaAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                runningMode.applyShield(ShieldType.ETA);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                etaButton.setIcon(etaImg_bg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                etaButton.setIcon(etaImg);
            }
        };
        lotaAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                runningMode.applyShield(ShieldType.LOTA);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                lotaButton.setIcon(lotaImg_bg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                lotaButton.setIcon(lotaImg);
            }
        };
        thetaAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                runningMode.applyShield(ShieldType.THETA);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                thetaButton.setIcon(thetaImg_bg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                thetaButton.setIcon(thetaImg);
            }
        };
        zetaAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                runningMode.applyShield(ShieldType.ZETA);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                zetaButton.setIcon(zetaImg_bg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                zetaButton.setIcon(zetaImg);
            }

        };
        etaButton.addMouseListener(etaAdapter);
        lotaButton.addMouseListener(lotaAdapter);
        thetaButton.addMouseListener(thetaAdapter);
        zetaButton.addMouseListener(zetaAdapter);
    }

    public void removeButtonListeners() {
        blenderButton.removeMouseListener(blenderAdapter);

        alphaPowerupButton.removeMouseListener(alphaPowerupAdapter);
        betaPowerupButton.removeMouseListener(betaPowerupAdapter);
        sigmaPowerupButton.removeMouseListener(sigmaPowerupAdapter);
        gammaPowerupButton.removeMouseListener(gammaPowerupAdapter);

        etaButton.removeMouseListener(etaAdapter);
        lotaButton.removeMouseListener(lotaAdapter);
        thetaButton.removeMouseListener(thetaAdapter);
        zetaButton.removeMouseListener(zetaAdapter);
    }

    @Override
    public void onHealthChanged(double health) {
        healthLabel.setText(String.format("%.2f", health));
    }

    @Override
    public void onTimerChanged(String currentTime) {
        timeLabel.setText(currentTime);
    }

    @Override
    public void onScoreChanged(double score) {
        scoreLabel.setText(MathUtils.truncateByTwo(score));
    }

    @Override
    public void onShieldsCountChange() {
        ShieldHandler shieldHandler = this.runningMode.getShieldHandler();

        etaNumberLabel.setText(shieldHandler.getShieldsCount(ShieldType.ETA));
        lotaNumberLabel.setText(shieldHandler.getShieldsCount(ShieldType.LOTA));
        thetaNumberLabel.setText(shieldHandler.getShieldsCount(ShieldType.THETA));
        zetaNumberLabel.setText(shieldHandler.getShieldsCount(ShieldType.ZETA));
    }

    @Override
    public void onProjectileCountChange() {
        ProjectileContainer container = this.runningMode.getProjectileContainer();

        alphaAtomsNumberLabel.setText(String.valueOf(container.getAtomCountForType(EntityType.ALPHA)));
        betaAtomsNumberLabel.setText(String.valueOf(container.getAtomCountForType(EntityType.BETA)));
        gammaAtomsNumberLabel.setText(String.valueOf(container.getAtomCountForType(EntityType.GAMMA)));
        sigmaAtomsNumberLabel.setText(String.valueOf(container.getAtomCountForType(EntityType.SIGMA)));

        alphaPowerupsNumberLabel.setText(String.valueOf(container.getPowerUpCountForType(EntityType.ALPHA)));
        betaPowerupsNumberLabel.setText(String.valueOf(container.getPowerUpCountForType(EntityType.BETA)));
        gammaPowerupsNumberLabel.setText(String.valueOf(container.getPowerUpCountForType(EntityType.GAMMA)));
        sigmaPowerupsNumberLabel.setText(String.valueOf(container.getPowerUpCountForType(EntityType.SIGMA)));
    }
}
