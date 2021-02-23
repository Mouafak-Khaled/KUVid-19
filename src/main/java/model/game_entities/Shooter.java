package model.game_entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.game_building.Configuration;
import model.game_building.GameConstants;
import model.game_entities.enums.EntityType;
import model.game_entities.enums.SuperType;
import model.game_entities.shields.ShieldTuple;
import model.game_physics.hitbox.HitboxFactory;
import model.game_physics.path_patterns.PathPatternFactory;
import model.game_running.CollisionVisitor;
import model.game_running.RunningMode;
import model.game_running.ShieldHandler;
import model.game_running.listeners.ShooterEventListener;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import services.utils.Coordinates;
import services.utils.MathUtils;
import services.utils.Vector;

import static model.game_building.GameConstants.DEFAULT_ROTATION_STEP;

public class Shooter extends Entity {
    private Projectile currentProjectile;
    private RunningMode runningMode;
    private ShieldHandler shieldHandler; //TODO: CHECK THIS
    private int movementState;

    Configuration config = Configuration.getInstance();

    // TODO move to game configuration
    public static Logger logger = Logger.getLogger(Shooter.class.getName());

    private ShooterEventListener shooterListener;

    public Shooter(RunningMode runningMode) {
        // Turn off logger
        // TODO 1: get initial coords from the game configuration
        // TODO 2: set this in super instead
        // sets the initial coordinates
        double baseHeight = config.getBaseHeight();
        setCoordinates(new Coordinates(
                config.getGameWidth() / 2.0,
                config.getGameHeight() - 0.5 * config.getUnitL() *
                        GameConstants.SHOOTER_HEIGHT - baseHeight));

        movementState = GameConstants.SHOOTER_MOVEMENT_STILL;

        shieldHandler = new ShieldHandler(this);

        // sets the Hitbox
        setHitbox(HitboxFactory.getInstance().getShooterHitbox()); //TODO: set this in super instead
        this.superType = SuperType.SHOOTER;
        this.runningMode = runningMode;
        this.setCurrentProjectile(this.nextAtom());
    }

    public Shooter() {
        movementState = GameConstants.SHOOTER_MOVEMENT_STILL;
    }

    public void setShooterListener(ShooterEventListener shooterListener) {
        this.shooterListener = shooterListener;
    }

    /**
     * Shoot the projectile on the tip of the shooter to the game space
     *
     * @return the atom on the tip of the shooter
     */
    public Projectile shoot() {
        if (getCurrentProjectile() == null)//get atom from the container returned null. (no more of the selected type)
            return null;
        shooterListener.onShot();
        shieldHandler.emptyTempShields();
        this.adjustProjectilePosition();
        return this.reload();
    }

    public void setRunningMode(RunningMode runningMode) {
        this.runningMode = runningMode;
    }

    /**
     * Adjust the projectile coordinates and speed vector orientation according to the coordinates and orientation
     * of the shooter
     */
    private void adjustProjectilePosition() {
        getCurrentProjectile().getHitbox().rotate(getAngle());
        getCurrentProjectile().setPathPattern(PathPatternFactory.getInstance().getAtomPathPattern(getHitbox().getRotationDegree()));
        getCurrentProjectile().setCoordinates(getShootingCoords());
    }


    /**
     * @return the coordinate of the projectile where it will start moving
     */
    @JsonIgnore
    public Coordinates getShootingCoords() {
        int height = (int) (getHitbox().getHeight() * 0.75);
        int projectileRadius = (int) getCurrentProjectile().getHitbox().getHeight() / 2;
        double theta = MathUtils.angleComplement(this.getHitbox().getRotationDegree());

        int newHeight = MathUtils.getCompositeYComponent(projectileRadius, height, theta);
        int newWidth = MathUtils.getCompositeXComponent(projectileRadius, height, theta);

        Coordinates newCenter = new Coordinates(this.getCoordinates().getX(), this.getCoordinates().getY() + 0.25 * getHitbox().getHeight());
        return MathUtils.translate(newCenter, new Coordinates(newWidth, -newHeight));
    }


    /**
     * reload the atom shooter by placing a random atom on the tip of the shooter
     *
     * @return the current projectile at the atom
     */
    public Projectile reload() {
        Projectile projectile = getCurrentProjectile();
        this.setCurrentProjectile(this.nextAtom());
        projectile.setVelocity(projectile.getSpeedPercentage());
        return projectile;
    }

    /**
     * get the next random atom
     *
     * @return a random atom
     */
    public Atom nextAtom() {
        Atom atom = runningMode.getProjectileContainer().getRandomAtom(getCoordinates());
        if (shieldHandler != null && atom != null) {
            shieldHandler.setTempShields(runningMode.getProjectileContainer().getShields(atom.getEntityType()));
            atom = runningMode.getProjectileContainer().shieldAtom(atom, shieldHandler.getTempShields());
        }
        return atom;
    }

    public Projectile getCurrentProjectile() {
        return currentProjectile;
    }

    public void setCurrentProjectile(Projectile currentProjectile) {
        this.currentProjectile = currentProjectile;
    }

    /**
     * this method is used to set a powerup of the given type on the tip of the shooter
     * @param type
     */
    public void setPowerup(EntityType type) {
        Projectile previousProjectile = getCurrentProjectile();
        Powerup currentPowerup = runningMode.getProjectileContainer().getPowerUp(this.getCoordinates(), type);
        if (currentPowerup != null) {
            if (previousProjectile.superType == SuperType.ATOM) {
                runningMode.getProjectileContainer().increaseAtoms(previousProjectile.getEntityType().getValue(), 1, shieldHandler.getTempShields());
                shieldHandler.emptyTempShields();
            } else
                runningMode.getProjectileContainer().addPowerUp((Powerup) previousProjectile);
            setCurrentProjectile(currentPowerup);
        }
    }

    /**
     * this methods is used for changing the atom on tip of the shooter to an atom of different type.
     */
    public void switchAtom() {
        // @REQUIRES: the the projectileContainer not to be empty nor the currentProjectile on the tip of the shooter to be of type powerup.
        // @MODIFIES: the currentProjectile object
        // @EFFECTS: changes the projectile on the tip of the shooter to an atom if the current projectile is powerup, changes the
        //           the projectile if the current projectile is atom to an atom of different type.
        Projectile previousProjectile = getCurrentProjectile();
        ShieldTuple tmpShields = shieldHandler.getTempShields();
        Projectile nextAtom = nextAtom();

        if (nextAtom != null) {
            if (previousProjectile.getSuperType() == SuperType.ATOM) {
                runningMode.getProjectileContainer().increaseAtoms(previousProjectile.getEntityType().getValue(), 1, tmpShields);

                while (previousProjectile.getEntityType() == nextAtom.getEntityType() && !uniqueTypeAvailable()) {
                    runningMode.getProjectileContainer().increaseAtoms(nextAtom.getEntityType().getValue(), 1, shieldHandler.getTempShields());
                    nextAtom = nextAtom();
                }
                setCurrentProjectile(nextAtom);
            } else {
                runningMode.getProjectileContainer().addPowerUp((Powerup) previousProjectile);
            }
            setCurrentProjectile(nextAtom);
        }
    }

    private boolean uniqueTypeAvailable() {

        int[] types = runningMode.getProjectileContainer().getAtomMap();
        int counter = 0;
        for (int type : types)
            if (type == 0)
                counter++;
        return counter == (types.length - 1);
    }

    @JsonIgnore
    public double getAngle() {
        return getHitbox().getRotationDegree();
    }

    public boolean rotate(int direction) {
        int rotationDirection = direction == GameConstants.SHOOTER_ROTATION_LEFT ? -1 : 1;
        if (!checkLegalMovement(this.getCoordinates(), this.getAngle() + DEFAULT_ROTATION_STEP * rotationDirection))
            return false;
        getHitbox().rotate(DEFAULT_ROTATION_STEP * rotationDirection);
        return true;
    }

    public void setMovementState(int movementState) {
        this.movementState = movementState;
    }

    @Override
    public void move() {
        if (movementState == GameConstants.SHOOTER_MOVEMENT_STILL) {
            shooterListener.onStopped(); //stop belt animation (for disco theme)
            return;
        }
        int direction = movementState == GameConstants.SHOOTER_MOVEMENT_RIGHT ? 1 : -1;
        Coordinates newCoords = new Coordinates(getCoordinates().getX() + direction * config.getShooterSpeed(), getCoordinates().getY());
        if (!checkLegalMovement(newCoords, this.getAngle())) {
            logger.info("[Shooter] shooter cannot move to the new coordinates" + this.getCoordinates());
            return;
        }
        shooterListener.onMoved();
        this.setCoordinates(newCoords);
        logger.debug("[Shooter] shooter moved to a new coordinates" + this.getCoordinates());
    }

    /**
     * Check if the shooter config.getShooterSpeed() is within the game view
     *
     * @param coordinates to be checked if inside the game view
     * @param angle       of the shooter
     * @return true if the movement is legal
     */
    private boolean checkLegalMovement(Coordinates coordinates, double angle) {
        double gunWidth = config.getUnitL() * GameConstants.SHOOTER_WIDTH;
        if (coordinates.getX() + gunWidth / 2 > config.getGamePanelDimensions().getWidth())
            return false;
        else if (coordinates.getX() - gunWidth / 2 < 0)
            return false;
        return checkLegalAngle(coordinates, angle);
    }

    /**
     * Check if the shooter rotation is within the game view
     *
     * @param coordinates to be checked if inside the game view
     * @param angle       of the shooter
     * @return true of rotating the shooter is legal
     */
    private boolean checkLegalAngle(Coordinates coordinates, double angle) {
        double gunWidth = config.getUnitL() * GameConstants.SHOOTER_WIDTH;
        double gunHeight = config.getUnitL() * GameConstants.SHOOTER_HEIGHT;

        // assume the left side if the shooter is in the left half of the screen, and right otherwise
        Vector rotatedShooter;
        if (coordinates.getX() < Configuration.getInstance().getGameWidth() / 2.0) {
            rotatedShooter = new Vector(
                    coordinates.getX() - gunWidth / 2,
                    coordinates.getY(),
                    coordinates.getX() - gunWidth / 2,
                    coordinates.getY() - gunHeight / 2.0);
        } else {
            rotatedShooter = new Vector(
                    coordinates.getX() + gunWidth / 2,
                    coordinates.getY(),
                    coordinates.getX() + gunWidth / 2,
                    coordinates.getY() - gunHeight / 2.0);
        }
        rotatedShooter = rotatedShooter.rotateVector(angle);
        return !(angle > 80) &&
                !(angle < -80) &&
                rotatedShooter.getPositionCoordinate().getX() >= 0 &&
                rotatedShooter.getPositionCoordinate().getX() <= config.getGamePanelDimensions().width;
    }

    /**
     * @return true if the current projectile is an atom
     */
    public boolean projectileIsAtom() {
        return getCurrentProjectile().getSuperType() == SuperType.ATOM;
    }

    /**
     * @return the projectile as an atom
     */
    @JsonIgnore
    public Atom getAtomProjectile() {
        return (Atom) getCurrentProjectile();
    }

    /**
     * update number of shields in statistics window
     */
    public void updateStatisticsShieldCount() {
        runningMode.updateStatisticsShieldCount();
    }

    public ShieldHandler getShieldHandler() {
        return shieldHandler;
    }

    public void initializeShieldHandler() {
        shieldHandler.setShooter(this);
    }

    @JsonIgnore
    public boolean isAtomShielded() {
        if (projectileIsAtom())
            return shieldHandler.getTempShields().isNotEmpty();
        return false;
    }

    @Override
    public String toString() {
        return "Shooter{" +
                "coordinate=" + getCoordinates() +
                ", hitbox=" + getHitbox() +
                ", currentProjectile=" + currentProjectile +
                '}';
    }
    // visitor pattern. Double delegation

    /**
     * handle collision with atom
     * @param visitor
     * @param atom
     */
    @Override
    public void collideWith(CollisionVisitor visitor, Atom atom) {
        visitor.handleCollision(this, atom);
    }

    /**
     * handle collision with blocker
     * @param visitor
     * @param blocker
     */
    @Override
    public void collideWith(CollisionVisitor visitor, Blocker blocker) {
        visitor.handleCollision(this, blocker);
    }

    /**
     * handle collision with molecule
     * @param visitor
     * @param molecule
     */
    @Override
    public void collideWith(CollisionVisitor visitor, Molecule molecule) {
        visitor.handleCollision(this, molecule);
    }

    /**
     * handle collision with powerup
     * @param visitor
     * @param powerup
     */
    @Override
    public void collideWith(CollisionVisitor visitor, Powerup powerup) {
        visitor.handleCollision(this, powerup);
    }

    /**
     * handle collision with shooter
     * @param visitor
     * @param shooter
     */
    @Override
    public void collideWith(CollisionVisitor visitor, Shooter shooter) {
        visitor.handleCollision(this, shooter);
    }

    /**
     * apply the visitor pattern to handle collisions between entities and the molecule object
     * @param visitor
     * @param entity
     */
    @Override
    public void acceptCollision(CollisionVisitor visitor, Entity entity) {
        entity.collideWith(visitor, this);
    }
}
