package model.game_running.runnables;

import model.game_building.Configuration;
import model.game_building.GameConstants;
import model.game_entities.AutonomousEntity;
import model.game_entities.Blocker;
import model.game_entities.Molecule;
import model.game_entities.Powerup;
import model.game_entities.enums.EntityType;
import model.game_entities.factories.BlockerFactory;
import model.game_entities.factories.MoleculeFactory;
import model.game_entities.factories.PowerupFactory;
import model.game_running.RunningMode;
import org.apache.log4j.Logger;
import services.utils.Coordinates;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * responsible for creating blockers, and powerups, molecules in the game space
 */
public class EntityGeneratorRunnable extends GameRunnable {
    private Map<EntityType, Integer> moleculeCountPerType, blockerCountPerType, powerUpCountPerType;
    private final RunningMode runningMode;
    private static final Logger logger = Logger.getLogger(EntityGeneratorRunnable.class.getName());

    public EntityGeneratorRunnable(RunningMode runningMode) {
        super();
        this.runningMode = runningMode;
        initializeMaps();
    }

    /**
     * fills the counting maps for molecules, blockers, power-ups
     */
    public void initializeMaps() {
        //fill molecule count map
        moleculeCountPerType = new HashMap<>();
        moleculeCountPerType.put(EntityType.ALPHA, Configuration.getInstance().getNumAlphaMolecules());
        moleculeCountPerType.put(EntityType.BETA, Configuration.getInstance().getNumBetaMolecules());
        moleculeCountPerType.put(EntityType.GAMMA, Configuration.getInstance().getNumGammaMolecules());
        moleculeCountPerType.put(EntityType.SIGMA, Configuration.getInstance().getNumSigmaMolecules());

        //fill blocker count map
        blockerCountPerType = new HashMap<>();
        blockerCountPerType.put(EntityType.ALPHA, Configuration.getInstance().getNumAlphaBlockers());
        blockerCountPerType.put(EntityType.BETA, Configuration.getInstance().getNumBetaBlockers());
        blockerCountPerType.put(EntityType.GAMMA, Configuration.getInstance().getNumGammaBlockers());
        blockerCountPerType.put(EntityType.SIGMA, Configuration.getInstance().getNumSigmaBlockers());

        //fill power-up count map
        powerUpCountPerType = new HashMap<>();
        powerUpCountPerType.put(EntityType.ALPHA, Configuration.getInstance().getNumAlphaPowerups());
        powerUpCountPerType.put(EntityType.BETA, Configuration.getInstance().getNumBetaPowerups());
        powerUpCountPerType.put(EntityType.GAMMA, Configuration.getInstance().getNumGammaPowerups());
        powerUpCountPerType.put(EntityType.SIGMA, Configuration.getInstance().getNumSigmaPowerups());
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                latch.await(); // if the game is paused, this latch clogs this runnable.
                List<Integer> randomTypes = new ArrayList<>();
                if (moleculeCountPerType.values().stream().reduce(0, Integer::sum) > 0)
                    randomTypes.add(0);
                if (blockerCountPerType.values().stream().reduce(0, Integer::sum) > 0)
                    randomTypes.add(1);
                if (powerUpCountPerType.values().stream().reduce(0, Integer::sum) > 0)
                    randomTypes.add(2);

                Collections.shuffle(randomTypes);
                if (randomTypes.isEmpty()) {
                    logger.info("[EntityGeneratorRunnable] OUT OF ENTITIES TO DROP");
                    runningMode.setOutOfEntities();
                    break;
                } else {
                    AutonomousEntity entity = GetRandomEntity(randomTypes.get(0));
                    this.runningMode.addEntity(entity);
                    //sleep before adding new objects
                    Thread.sleep(Configuration.getInstance().getDropRate());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private AutonomousEntity GetRandomEntity(int choice) {

        switch (choice) {
            case 0:
                return generateMolecule();
            case 1:
                return generateBlocker();
            case 2:
                return generatePowerup();
            default:
                return null;
        }
    }

    /**
     * generates a random Blocker to be thrown in the space
     *
     * @return a Blocker of a random type
     */
    public Blocker generateBlocker() {
        List<Integer> randomTypes = Stream.of(0, 1, 2, 3)
                .filter(c -> blockerCountPerType.get(EntityType.forValue(c)) > 0)
                .collect(Collectors.toList());
        Collections.shuffle(randomTypes);

        if (randomTypes.isEmpty())
            return null; //no more blockers

        double l = GameConstants.BLOCKER_RADIUS * Configuration.getInstance().getUnitL();
        double r = Configuration.getInstance().getGamePanelDimensions().getWidth() - GameConstants.BLOCKER_RADIUS * Configuration.getInstance().getUnitL();
        double x_coord = l + Math.random() * (r - l);
        logger.info("[ObjectGeneratorRunnable] generating a blocker at coordinates " + new Coordinates(x_coord, 0) + " ]");

        Blocker blocker = BlockerFactory.getInstance().getBlocker(EntityType.forValue(randomTypes.get(0)));
        blockerCountPerType.replace(blocker.getEntityType(), blockerCountPerType.get(blocker.getEntityType()) - 1);
        blocker.setCoordinates(new Coordinates(x_coord, 1));
        return blocker;
    }

    /**
     * generates a random powerup to be thrown in the space
     *
     * @return a Powerup of a random type
     */
    public Powerup generatePowerup() {
        List<Integer> randomTypes = Stream.of(0, 1, 2, 3)
                .filter(c -> powerUpCountPerType.get(EntityType.forValue(c)) > 0)
                .collect(Collectors.toList());
        Collections.shuffle(randomTypes);

        if (randomTypes.isEmpty())
            return null; //no more powerups

        double l = GameConstants.POWERUP_RADIUS * Configuration.getInstance().getUnitL();
        double r = Configuration.getInstance().getGamePanelDimensions().getWidth() - GameConstants.POWERUP_RADIUS * Configuration.getInstance().getUnitL();
        double x_coord = l + Math.random() * (r - l);
        logger.info("[ObjectGenerator: generating a powerup at coordinates " + new Coordinates(x_coord, 0) + " ]");

        Powerup powerup = PowerupFactory.getInstance().getPowerup(EntityType.forValue(randomTypes.get(0)));
        powerUpCountPerType.replace(powerup.getEntityType(), powerUpCountPerType.get(powerup.getEntityType()) - 1);
        powerup.setCoordinates(new Coordinates(x_coord, 0));
        return powerup;
    }

    /**
     * generates a random Molecule to be thrown in the space
     *
     * @return a Molecule of a random type
     */
    public Molecule generateMolecule() {
        List<Integer> randomTypes = Stream.of(0, 1, 2, 3)
                .filter(c -> moleculeCountPerType.get(EntityType.forValue(c)) > 0)
                .collect(Collectors.toList());
        Collections.shuffle(randomTypes);

        if (randomTypes.isEmpty())
            return null; //no more power-ups

        double l = GameConstants.MOLECULE_RADIUS * Configuration.getInstance().getUnitL();
        double r = Configuration.getInstance().getGamePanelDimensions().getWidth() - GameConstants.MOLECULE_RADIUS * Configuration.getInstance().getUnitL();
        double x_coord = l + Math.random() * (r - l);
        logger.info("[ObjectGenerator: generating a molecule at coordinates " + new Coordinates(x_coord, 0) + " ]");

        Molecule molecule = MoleculeFactory.getInstance().getMolecule(EntityType.forValue(randomTypes.get(0)));
        moleculeCountPerType.replace(molecule.getEntityType(), moleculeCountPerType.get(molecule.getEntityType()) - 1);
        molecule.setCoordinates(new Coordinates(x_coord, 0));

        return molecule;
    }
}
