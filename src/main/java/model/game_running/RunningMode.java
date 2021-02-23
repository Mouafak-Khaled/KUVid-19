package model.game_running;

import model.game_building.Configuration;
import model.game_building.GameBundle;
import model.game_building.GameConstants;
import model.game_entities.*;
import model.game_entities.enums.ShieldType;
import model.game_entities.enums.SuperType;
import model.game_running.listeners.*;
import model.game_running.runnables.CollisionRunnable;
import model.game_running.runnables.EntityGeneratorRunnable;
import model.game_running.runnables.GameRunnable;
import model.game_running.runnables.MovementRunnable;
import model.game_running.states.GameState;
import model.game_running.states.PausedState;
import model.game_running.states.ResumedState;
import model.game_space.Blender;
import model.game_space.Player;
import org.apache.log4j.Logger;
import services.database.IDatabase;
import services.utils.IOHandler;
import services.utils.SoundHandler;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The main control unit behind the game UI
 * Facade Controller
 */
public class RunningMode {
    public Logger logger = Logger.getLogger(this.getClass().getName());
    private Player player;

    // game state
    GameState currentState;
    GameState pausedState;
    GameState resumedState;

    //space objects
    private final CopyOnWriteArrayList<AutonomousEntity> autonomousEntities;
    private ProjectileContainer projectileContainer;
    private Shooter shooter;

    //Listeners
    private final RunningStateListener runningStateListener;
    private final GameEntitiesListener gameEntitiesListener;
    private final SessionLoadListener sessionLoadListener;
    private final SessionSaveListener sessionSaveListener;
    private final OnGameStateListener onGameStateListener;
    // Runnables
    private ArrayList<GameRunnable> gameRunnables;
    private EntityGeneratorRunnable entityGeneratorRunnable;
    private boolean outOfEntities; // flags that the entity generator runnable has run out of entities to drop

    // Threads
    private Thread movementThread;
    private Thread collisionThread;
    private Thread entityGeneratorThread;

    // Blender
    private final Blender blender;

    public RunningMode(RunningStateListener runningStateListener,
                       GameEntitiesListener gameEntitiesListener,
                       SessionLoadListener sessionLoadListener,
                       SessionSaveListener sessionSaveListener,
                       OnGameStateListener onGameStateListener) {

        autonomousEntities = new CopyOnWriteArrayList<>();
        // Config
        Configuration config = Configuration.getInstance();
        // States
        resumedState = new ResumedState(this);
        pausedState = new PausedState(this);
        currentState = resumedState;

        // Listeners
        this.runningStateListener = runningStateListener;
        this.gameEntitiesListener = gameEntitiesListener;
        this.sessionLoadListener = sessionLoadListener;
        this.sessionSaveListener = sessionSaveListener;

        // Shooter and Projectile Container
        this.projectileContainer = new ProjectileContainer(
                this,
                config.getNumAlphaAtoms(),
                config.getNumBetaAtoms(),
                config.getNumSigmaAtoms(),
                config.getNumGammaAtoms());
        this.blender = new Blender(this.projectileContainer);
        this.shooter = new Shooter(this);
        this.onGameStateListener = onGameStateListener;
        initialize();
    }

    /**
     * instantiates the threads and runnables. fills the list. sets "initialized" to true
     */
    private void initialize() {
        gameRunnables = new ArrayList<>();

        GameRunnable movementRunnable = new MovementRunnable(this);
        gameRunnables.add(movementRunnable);

        CollisionHandler collisionHandler = new CollisionHandler(this, new SoundHandler());
        GameRunnable collisionRunnable = new CollisionRunnable(this, collisionHandler);
        gameRunnables.add(collisionRunnable);

        entityGeneratorRunnable = new EntityGeneratorRunnable(this);
        gameRunnables.add(entityGeneratorRunnable);

        movementThread = new Thread(movementRunnable);
        collisionThread = new Thread(collisionRunnable);
        entityGeneratorThread = new Thread(this.entityGeneratorRunnable);

        try {
            onGameStateListener.onGameStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * starts the Movement, collision, shooter, and EntityGenerator threads
     */
    public void startThreads() {
        movementThread.start();
        collisionThread.start();
        entityGeneratorThread.start();
    }

    // Shooter ////

    /**
     * move shooter in a given direction.
     * @param direction
     */
    public void moveShooter(int direction) {
        currentState.moveShooter(direction);
    }

    /**
     * rotate shooter in a given direction
     * @param direction
     */
    public void rotateShooter(int direction) {
        currentState.rotateShooter(direction);
    }

    /**
     * Shoot entity at the tip of the Shooter
     */
    public void shootProjectile() {
        Projectile shotEntity = this.shooter.shoot();
        try {
            onGameStateListener.onShoot();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (shotEntity == null) {
            if (noAtomsOnScreen())
                endGame();
            return;
        }
        // projectileContainer.decreaseAtoms(shotEntity.getEntityType().getValue(), 1);
        addEntity(shotEntity);
    }

    /**
     * switch atom at the tip of the gun
     */
    public void switchAtom() {
        getShooter().switchAtom();
    }

    /**
     * add a given powerup to projectileContainer
     * @param powerup
     */
    public void collectPowerUp(Powerup powerup) {
        projectileContainer.addPowerUp(powerup);
    }

    /**
     * apply a given shield on the atom at the tip of the gun
     * @param shieldType
     */
    public void applyShield(ShieldType shieldType) {
        currentState.applyShield(shieldType);
    }

    // Space Entities ////

    /**
     * static so that all classes
     *
     * @return returns the list of autonomous entities
     */
    public CopyOnWriteArrayList<AutonomousEntity> getAutonomousEntities() {
        return autonomousEntities;
    }

    /**
     * @param entity the entity to be added to the list of entities
     */
    public void addEntity(AutonomousEntity entity) {
        gameEntitiesListener.onEntityAdd(entity);
        autonomousEntities.add(entity);
    }

    /**
     *remove entity from the arraylist to be removed from the game view.
     * @param entity to be removed
     */
    public void removeEntity(AutonomousEntity entity) {
        // TODO: change the gamerListener to removeEntity. Handle multiple entities by calling removeEntity on them one by one.
        ArrayList<AutonomousEntity> tmp = new ArrayList<>();
        tmp.add(entity);
        autonomousEntities.remove(entity);
        gameEntitiesListener.onEntitiesRemove(tmp);
    }

    public boolean noAtomsOnScreen() {
        for (Entity entity : autonomousEntities)
            if (entity.getSuperType() == SuperType.ATOM)
                return false;
        return true;
    }

    /**
     *
     * @return true if there is no entities other than atoms and shooter on the screen.
     */
    public boolean noEntitiesOnScreen() {
        for (Entity entity : autonomousEntities)
            if (entity.getSuperType() != SuperType.ATOM && entity.getSuperType() != SuperType.SHOOTER)
                return false;
        return true;
    }

    public void setOutOfEntities() {
        outOfEntities = true;
    }

    // Player ////
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * decrease the health of the player by a given damage amount.
     * @param damageAmount
     */
    public void updateHealth(double damageAmount) {
        if (player != null)
            if (player.loseHealth(damageAmount))
                endGame();
    }

    /**
     * update the timer of the game
     * @param amountInMillis
     */
    public void updateTimer(int amountInMillis) {
        if (player != null) {
            boolean timerOver = !player.updateTime(amountInMillis);
            if (timerOver)
                endGame();
        }
    }

    /**
     * update the number of projectiles on the statistics window
     */
    public void updateStatisticsProjectileCount() {
        if (player != null)
            player.updateOwnedProjectiles();
    }

    /**
     * update the number of shields on the statistics window
     */
    public void updateStatisticsShieldCount() {
        if (player != null)
            player.changeShieldCount();
    }

    /**
     * increase the score of the player by a given score value.
     * @param score
     */
    public void increaseScore(double score) {
        if (player != null) player.incrementScore(score);
    }

    // Save-Load ////

    /**
     * display saved sessions of the game
     */
    public void showSavedSessions() {
        currentState.showSavedSessions();
    }

    /**
     * load a saved session of the game to play
     * @param session
     */
    public void loadGameSession(GameBundle session) {

        // update the game configuration
        Configuration.getInstance().reset(session.getConfigBundle());
        // update the entities in the game view
        this.autonomousEntities.clear();
        this.autonomousEntities.addAll(session.getAtoms());
        this.autonomousEntities.addAll(session.getBlockers());
        this.autonomousEntities.addAll(session.getMolecules());
        this.autonomousEntities.addAll(session.getPowerUps());

        // update the projectile containers
        this.projectileContainer = session.getProjectileContainer();
        this.projectileContainer.setRunningMode(this);

        // update the shooter state
        shooter = session.getShooter();
        shooter.setRunningMode(this);
        shooter.initializeShieldHandler();

        // update the player state and statistics listener
        GameStatisticsListener listener = this.player.getStatisticsListener();
        this.player = session.getPlayer();
        this.player.setStatisticsListener(listener);

        // update runnables
        this.blender.setProjectileContainer(projectileContainer);
        this.entityGeneratorRunnable.initializeMaps();

        // reflect the changes in the UI
        gameEntitiesListener.onGameReset();
        for (AutonomousEntity entity : this.autonomousEntities) {
            gameEntitiesListener.onEntityAdd(entity);
        }

    }

    /**
     * request for saving a game session
     */
    public void saveGameRequest() {
        this.currentState.saveGameSession();
    }

    public void saveWithAdapter(IDatabase database) {
        GameBundle.Builder builder = new GameBundle.Builder();
        builder.setPlayer(player).
                setShooter(getShooter()).
                setProjectileContainer(getProjectileContainer()).
                setConfigBundle(Configuration.getInstance().getConfigBundle());

        getAutonomousEntities().forEach(entity -> entity.saveState(builder));

        GameBundle bundle = builder.build();
        try {
            database.save(GameConstants.SESSION_COLLECTION_TITLE, "Session", bundle); //
        } catch (IOException e) {
            logger.error("Could not save the game session", e);
        }
    }

    public SessionLoadListener getSessionLoadListener() {
        return sessionLoadListener;
    }

    public SessionSaveListener getSessionSaveListener() {
        return sessionSaveListener;
    }

    // Game State ////

    /**
     * resume game
     */
    public void resume() {
        try {
            onGameStateListener.onGameResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentState.resume();
    }

    /**
     * pause game
     */
    public void pause() {
        try {
            onGameStateListener.onGamePaused();
        } catch (Exception e) {
            e.printStackTrace();
        }

        currentState.pause();
    }

    /**
     * change the state of the game to a given state.
     * @param currentState
     */
    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    /**
     * Pauses/Resumes/Stops all runnables.
     */
    public void applyRunningState(int state) {
        // set the state of game model threads
        gameRunnables.forEach(runnable -> runnable.setRunnableState(state));
        // set the state of the UI
        runningStateListener.onRunningStateChanged(state);
    }

    /**
     *
     * @return true if the game is finished.
     */
    public boolean isGameFinished() {
        if (shooter.getCurrentProjectile() == null)
            return noAtomsOnScreen();
        if (outOfEntities)
            return noEntitiesOnScreen();
        return false;
    }

    /**
     * call for the end of the game
     */
    public void endGame() {
        try {
            onGameStateListener.onGameOver();
        } catch (Exception e) {
            e.printStackTrace();
        }

        applyRunningState(GameConstants.GAME_STATE_STOP);
        runningStateListener.onGameOver();
    }

    // Getters ////
    public ProjectileContainer getProjectileContainer() {
        return this.projectileContainer;
    }

    public void setProjectileContainer(ProjectileContainer projectileContainer) {
        this.projectileContainer = projectileContainer;
    }


    public Blender getBlender() {
        return this.blender;
    }

    public Shooter getShooter() {
        return shooter;
    }

    public GameState getPausedState() {
        return pausedState;
    }

    public GameState getResumedState() {
        return resumedState;
    }

    public ShieldHandler getShieldHandler() {
        return shooter.getShieldHandler();
    }

    public interface OnGameStateListener {

        void onGameStart() throws UnsupportedAudioFileException, IOException, LineUnavailableException;

        void onGameOver() throws UnsupportedAudioFileException, IOException, LineUnavailableException;

        void onGamePaused() throws UnsupportedAudioFileException, IOException, LineUnavailableException;

        void onGameResume() throws UnsupportedAudioFileException, IOException, LineUnavailableException;

        void onShoot() throws UnsupportedAudioFileException, IOException, LineUnavailableException;
    }
}
