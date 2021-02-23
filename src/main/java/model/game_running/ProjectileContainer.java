package model.game_running;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.game_entities.Atom;
import model.game_entities.Powerup;
import model.game_entities.enums.EntityType;
import model.game_entities.enums.SuperType;
import model.game_entities.factories.AtomFactory;
import model.game_entities.shields.ShieldTuple;
import model.game_entities.shields.ShieldedAtomFactory;
import model.game_physics.hitbox.HitboxFactory;
import model.game_physics.path_patterns.PathPatternFactory;
import org.apache.log4j.Logger;
import services.utils.Coordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * this class will keep information about the current amount of projectiles remaining as well as provide a projectile
 * on demand.
 */
public class ProjectileContainer {
    public static Logger logger = Logger.getLogger(ProjectileContainer.class.getName());

    private int[] atomMap;
    private int[] powerUpMap; // keeps the number of power-ups per type.
    public HashMap<Integer, ArrayList<ShieldTuple>> shieldsMap;
    int totalAtomCount;

    private RunningMode runningMode;
    private Random random = new Random();

    public ProjectileContainer(RunningMode runningMode, int numOfAlphaAtoms, int numOfBetaAtoms, int numOfSigmaAtoms, int numOfGammaAtoms) {
        this.runningMode = runningMode;

        atomMap = new int[4];
        atomMap[0] = numOfAlphaAtoms;
        atomMap[1] = numOfBetaAtoms;
        atomMap[2] = numOfGammaAtoms;
        atomMap[3] = numOfSigmaAtoms;

        totalAtomCount = numOfAlphaAtoms + numOfBetaAtoms + numOfGammaAtoms + numOfSigmaAtoms;

        powerUpMap = new int[]{0, 0, 0, 0}; //the player starts with 0 power-ups

        shieldsMap = new HashMap<>();
        shieldsMap.put(0, new ArrayList<>());
        shieldsMap.put(1, new ArrayList<>());
        shieldsMap.put(2, new ArrayList<>());
        shieldsMap.put(3, new ArrayList<>());
        random = new Random();
    }

    @SuppressWarnings("unused")
    public ProjectileContainer() {//this is needed for the save/load functionality
    }

    //  this setter is used in case the projectile container was created by loading a game session.
    @JsonIgnore
    public void setRunningMode(RunningMode runningMode) {
        this.runningMode = runningMode;
    }

    /**
     * if there are remaining atoms of the desired type, creates the desired atom object and decrements the number
     * of remaining atoms of the created type. returns null if there are no remaining atoms of the desired type
     *
     * @param coordinates: the coordinates of the desired atom
     * @param type:        the type of desired atom
     * @return the desired atom if there are remaining atoms of that type. null otherwise.
     */
    private Atom getAtom(Coordinates coordinates, int type) {
        if (updateProjectileMap(atomMap, SuperType.ATOM, type, -1)) {
            Atom atom = AtomFactory.getInstance().getAtom((EntityType.forValue(type)));
            if (atom != null)
                atom.setCoordinates(coordinates);
            return atom;
        }
        return null;
    }

    public Atom getRandomAtom(Coordinates coordinates) { //needs some refactoring
        if (totalAtomCount == 0)
            return null; //out of atoms
        int atomType;
        Atom atom = null;
        while (atom == null) {
            atomType = random.nextInt(4);
            atom = getAtom(coordinates, atomType);
        }
        return atom;
    }

    /**
     * return either a shielded atom from the shields available, or the atom itself depending on
     * the number of shields of a type and a random boolean
     *
     * @param atom the atom to be shielded
     * @return either a shielded atom or the atom itself
     */
    public Atom shieldAtom(Atom atom, ShieldTuple shields) {
        return ShieldedAtomFactory.applyShields(shields, atom);
    }

    /**
     * if there are remaining power-ups of the desired type, creates the desired power-up object and decrements the number
     * of remaining power-ups of the created type. returns null if there are no remaining power-ups of the desired type
     *
     * @param coordinates: the coordinates of the desired power-up
     * @param type:        the type of desired power-up
     * @return the desired power-up if there are remaining power-ups of that type. null otherwise.
     */
    public Powerup getPowerUp(Coordinates coordinates, EntityType type) {
        if (updateProjectileMap(powerUpMap, SuperType.POWERUP, type.getValue(), -1)) //
            return new Powerup(coordinates, HitboxFactory.getInstance().getPowerUpHitbox(),
                    PathPatternFactory.getInstance().getPowerUpPathPattern(),
                    EntityType.forValue(type.getValue()), false);
        return null;
    }

    public void addPowerUp(Powerup powerup) {
        updateProjectileMap(powerUpMap, SuperType.POWERUP, powerup.getEntityType().getValue(), 1);
    }

    /**
     * used in blending/breaking. it reduces the amount of the given type by the given amount.
     *
     * @param type  type to be reduced
     * @param count amount of reduction
     * @return returns whether the decrease was successful (player is not out of atoms)
     */
    public boolean decreaseAtoms(int type, int count) { //todo: make this method take an enum type instead of an int
        return updateProjectileMap(atomMap, SuperType.ATOM, type, -count);
    }


    /**
     * used in blending/breaking. it increases the amount of the given type by the given amount.
     *
     * @param type  type to be increased
     * @param count the amount of increase
     */
    public boolean increaseAtoms(int type, int count, ShieldTuple shields) { //todo: make this method take an enum type instead of an int
        addShields(type, shields);
        return updateProjectileMap(atomMap, SuperType.ATOM, type, count);
    }

    public void addShields(int type, ShieldTuple shields) {
        if (shields != null && shields.isNotEmpty())
            shieldsMap.get(type).add(shields);
    }

    /**
     * checks the number of remaining items of a given type in a given map.
     * if the number is greater than zero, it decrements it and returns true (indicating successful decrement).
     * returns false otherwise (indicating there are no remaining items of the given type).
     *
     * @param map   the map in which the check will happen
     * @param type  the type for which the check will happen
     * @param count the amount of change. a negative number decreases the count of the given the type, a positive
     *              number increases it
     * @return the result of the check and decrement.
     */
    private boolean updateProjectileMap(int[] map, SuperType superType, int type, int count) {
        int remaining = map[type];
        if (remaining < -count)
            return false;
        map[type] = remaining + count;
        if (superType.equals(SuperType.ATOM))
            totalAtomCount += count;
        if (runningMode != null) {
            runningMode.updateStatisticsProjectileCount();
        } else {
            logger.warn("RunningMode instance is null");
        }
        return true;
    }

    public int[] getAtomMap() {
        return this.atomMap;
    }

    @SuppressWarnings("unused")
    public int getTotalAtomCount() {
        //needed for serialization
        return totalAtomCount;
    }

    @SuppressWarnings("unused")
    public int[] getPowerUpMap() {
        //needed for serialization
        return powerUpMap;
    }

    @JsonIgnore
    public int getAtomCountForType(EntityType type) {
        return atomMap[type.getValue()];
    }

    @JsonIgnore
    public int getPowerUpCountForType(EntityType type) {
        return powerUpMap[type.getValue()];
    }

    @JsonIgnore
    public ShieldTuple getShields(EntityType entityType) {
        ArrayList<ShieldTuple> shieldLst = shieldsMap.get(entityType.getValue());
        if (random.nextBoolean() || shieldedAtoms(entityType.getValue()) >= getAtomCountForType(entityType))
            return shieldLst.size() > 0 ? shieldsMap.get(entityType.getValue()).remove(random.nextInt(shieldLst.size())) : new ShieldTuple();
        return new ShieldTuple();
    }

    private int shieldedAtoms(int type) {
        return shieldsMap.get(type).size();
    }

    @Override
    public String toString() {
        return "ProjectileContainer{" +
                "atomMap=" + Arrays.toString(atomMap) +
                '}';
    }

}
