package ui.windows;

import model.game_building.Configuration;
import model.game_building.GameBundle;
import model.game_building.GameConstants;
import model.game_entities.AutonomousEntity;
import model.game_entities.Entity;
import model.game_entities.Shooter;
import model.game_physics.path_patterns.PathPattern;
import model.game_running.ProjectileContainer;
import model.game_running.RunningMode;
import model.game_running.SessionLoader;
import model.game_running.listeners.GameEntitiesListener;
import model.game_running.listeners.RunningStateListener;
import model.game_running.runnables.EntityGeneratorRunnable;
import model.game_running.runnables.GameRunnable;
import model.game_space.Player;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import services.database.IDatabase;
import services.database.MongoDBAdapter;
import services.utils.SoundHandler;
import ui.movable_drawables.Drawable;
import ui.movable_drawables.DrawableFactory;
import ui.movable_drawables.ImageResources;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * This class draws the game running window.
 * all game animations and actions will happen in this window
 */
public class RunningWindow extends JFrame implements RunningStateListener, GameEntitiesListener {
    private final RunningMode runningMode;
    private final GamePanel gameContentPanel;
    private final StatisticsPanel statisticsPanel;
    private boolean running;
    private Configuration config;
    private Image background;
    private final Image background_gameOver;
    private final Map<AutonomousEntity, Drawable> drawableMap;
    private final SessionLoadWindow sessionLoadWindow;
    private final SessionSaveWindow saveSessionWindow;
    private CountDownLatch pauseLatch;
    private static Logger logger = Logger.getLogger(RunningWindow.class.getName());;

    public RunningWindow(String title) { // TODO: CLEAN: maybe move panel to a separate class.
        super(title);
        drawableMap = new ConcurrentHashMap<>(); // concurrent so that it supports concurrent addition and deletion.
        this.config = Configuration.getInstance();
        this.setSize(config.getRunningWindowDimension());
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.sessionLoadWindow = new SessionLoadWindow(this);
        this.saveSessionWindow = new SessionSaveWindow(this);
        this.runningMode = new RunningMode(this, this, sessionLoadWindow,
                saveSessionWindow, new SoundHandler());
        logger.info("in running window" + runningMode.getBlender());
        BlenderWindow blenderWindow = new BlenderWindow(runningMode); // Window that implements the blending listener for the observer pattern
        gameContentPanel = new GamePanel(this.runningMode, drawableMap);
        statisticsPanel = new StatisticsPanel(this.runningMode);
        Player player = new Player("player", statisticsPanel); //todo: change temp username
        this.runningMode.setPlayer(player);
        background = ImageResources.backGround(getWidth(), getHeight(), false);
        background_gameOver = ImageResources.backGround(getWidth(), getHeight(), true);
        JPanel backgroundPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };

        setContentPane(backgroundPanel);
        getContentPane().add(gameContentPanel, BorderLayout.LINE_START);
        getContentPane().add(statisticsPanel, BorderLayout.LINE_END);

        //add separator
        if (!config.isDiscoTheme()) {
            JPanel separator = new JPanel();
            separator.setPreferredSize(new Dimension(GameConstants.PANEL_SEPARATOR_WIDTH, getHeight()));
            getContentPane().add(separator, BorderLayout.CENTER);
            separator.setBackground(Color.BLACK);
            getContentPane().add(statisticsPanel, BorderLayout.CENTER);
        }
        setLocationRelativeTo(null); //centers the window in the middle of the screen

        setResizable(false);
        setVisible(true);
        pack();
        start();


        // configure loggers
        Logger.getLogger(RunningWindow.class.getName()).setLevel(Level.DEBUG);
        Logger.getLogger(GameRunnable.class.getName()).setLevel(Level.DEBUG);
        Logger.getLogger(Entity.class.getName()).setLevel(Level.DEBUG);
        Logger.getLogger(Shooter.class.getName()).setLevel(Level.DEBUG);
        Logger.getLogger(PathPattern.class.getName()).setLevel(Level.DEBUG);
        Logger.getLogger(ProjectileContainer.class.getName()).setLevel(Level.DEBUG);
        Logger.getLogger(EntityGeneratorRunnable.class.getName()).setLevel(Level.DEBUG);
        Logger.getLogger(RunningMode.class.getName()).setLevel(Level.DEBUG);
        Logger.getLogger(SessionLoader.class.getName()).setLevel(Level.DEBUG);
        Logger.getLogger(Player.class.getName()).setLevel(Level.DEBUG);
        Logger.getLogger(MongoDBAdapter.class.getName()).setLevel(Level.DEBUG);
    }

    /**
     * starts the the game loop (drawing, movement, and collision checks)
     */
    private void start() {
        running = true; // this will be made false somewhere else (when health or time are over)
        pauseLatch = new CountDownLatch(0);
        runningMode.startThreads();
        startDrawingThread();
    }

    private void startDrawingThread() {
        Thread drawingThread = new Thread(() -> {
            while (running) {
                try {
                    pauseLatch.await();
                    runningMode.updateTimer(GameConstants.GAME_THREAD_DELAY);
                    repaint();
                    if (runningMode.isGameFinished()) runningMode.endGame();
                    Thread.sleep(GameConstants.GAME_THREAD_DELAY);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

        drawingThread.start();
    }

    private void unregisterInputListeners() {
        gameContentPanel.unregisterInputListeners();
        statisticsPanel.removeButtonListeners();
    }

    public void loadGameSession(GameBundle bundle) {
        runningMode.loadGameSession(bundle);
    }

    public void saveAdapterSelected(IDatabase database) {
        runningMode.saveWithAdapter(database);
    }

    /**
     * starts the loop that draws game elements.
     */
    @Override
    public void onRunningStateChanged(int state) {
        if (state == GameConstants.GAME_STATE_PAUSED) {
            pauseLatch = new CountDownLatch(1);
            gameContentPanel.showPauseIndicator(true);
        } else {
            pauseLatch.countDown();
            gameContentPanel.showPauseIndicator(false);
        }
    }

    @Override
    public void onGameOver() {
        unregisterInputListeners();
        drawableMap.clear();
        background = background_gameOver;
        this.running = false;
    }

    @Override
    public void onEntityAdd(AutonomousEntity entity) {
        drawableMap.put(entity, DrawableFactory.get(entity));
    }

    @Override
    public void onEntitiesRemove(Collection<AutonomousEntity> entities) {
        for (AutonomousEntity entity : entities)
            drawableMap.remove(entity);
    }

    @Override
    public void onGameReset() {
        drawableMap.clear();
        this.config = Configuration.getInstance();
        this.gameContentPanel.reset(drawableMap);
        this.statisticsPanel.onProjectileCountChange();
        this.statisticsPanel.onShieldsCountChange();
        invalidate();
        repaint();
    }

    public void setEnabled(boolean enabled) {
        setFocusableWindowState(enabled);
        gameContentPanel.setFocusable(enabled);
        statisticsPanel.setFocusable(enabled);
    }
}
