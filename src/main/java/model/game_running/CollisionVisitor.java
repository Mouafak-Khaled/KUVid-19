package model.game_running;

import model.game_entities.*;

public interface CollisionVisitor {

    /**
     * this methods is supposed to handle the collision of atom entity with powerup entity.
     * since no action is done in this type of collision, the method is default.
     *
     * @param atom to be checked if collision happened
     * @param powerup to be checked if collision happened
     */
    default void handleCollision(Atom atom, Powerup powerup) {
    }

    /**
     * this method is to handle atom collision with a molecule entity.
     * it will be overridden in collisionHandler class from the side of atom entity.
     *
     * @param atom to be checked if collision happened
     * @param molecule to be checked if collision happened
     */
    void handleCollision(Atom atom, Molecule molecule);

    /**
     * this method is to handle atom collision with a blocker entity.
     * it will be overridden in collisionHandler class from the side of atom entity.
     *
     * @param atom to be checked if collision happened
     * @param blocker to be checked if collision happened
     */
    void handleCollision(Atom atom, Blocker blocker);

    /**
     * this methods is supposed to handle the collision of atom entity with a shooter entity.
     * since no action is done in this type of collision, the method is default.
     *
     * @param atom to be checked if collision happened
     * @param shooter to be checked if collision happened
     */
    default void handleCollision(Atom atom, Shooter shooter) {
    }

    /**
     * this methods is supposed to handle the collision of atom entity with another atom entity.
     * since no action is done in this type of collision, the method is default.
     *
     * @param atom1 to be checked if collision happened
     * @param atom2 to be checked if collision happened
     */
    default void handleCollision(Atom atom1, Atom atom2) {
    }

    // Powerup collision

    /**
     * this methods is supposed to handle the collision of powerup entity with another a powerup entity.
     * since no action is done in this type of collision, the method is default.
     *
     * @param powerup1 to be checked if collision happened
     * @param powerup2 to be checked if collision happened
     */
    default void handleCollision(Powerup powerup1, Powerup powerup2) {
    }

    /**
     * this methods is supposed to handle the collision of powerup entity with a molecule entity.
     * since no action is done in this type of collision, the method is default.
     *
     * @param powerup to be checked if collision happened
     * @param molecule to be checked if collision happened
     */
    default void handleCollision(Powerup powerup, Molecule molecule) {
    }

    /**
     * this methods is to handle the collision of powerup entity with a blocker entity.
     * it will be overridden in collisionHandler class from the side of powerup entity.
     *
     * @param powerup to be checked if collision happened
     * @param blocker to be checked if collision happened
     */
    void handleCollision(Powerup powerup, Blocker blocker);

    /**
     * this methods is supposed to handle the collision of powerup entity with an atom entity.
     * since no action is done in this type of collision, the method is default.
     *
     * @param powerup to be checked if collision happened
     * @param atom to be checked if collision happened
     */
    default void handleCollision(Powerup powerup, Atom atom) {
    }


    // Molecule collision

    /**
     * this methods is supposed to handle the collision of molecule entity with a powerup entity.
     * since no action is done in this type of collision, the method is default.
     *
     * @param molecule to be checked if collision happened
     * @param powerup to be checked if collision happened
     */
    default void handleCollision(Molecule molecule, Powerup powerup) {
    }

    /**
     * this methods is supposed to handle the collision of molecule entity with a molecule entity.
     * since no action is done in this type of collision, the method is default.
     *
     * @param molecule1 to be checked if collision happened
     * @param molecule2 to be checked if collision happened
     */
    default void handleCollision(Molecule molecule1, Molecule molecule2) {
    }


    /**
     * this methods is to handle the collision of molecule entity with a blocker entity.
     * it will be overridden in collisionHandler class from the side of molecule entity.
     *
     * @param molecule to be checked if collision happened
     * @param blocker to be checked if collision happened
     */
    void handleCollision(Molecule molecule, Blocker blocker);

    /**
     * this methods is supposed to handle the collision of molecule entity with a shooter entity.
     * since no action is done in this type of collision, the method is default.
     *
     * @param molecule to be checked if collision happened
     * @param shooter to be checked if collision happened
     */
    default void handleCollision(Molecule molecule, Shooter shooter) {
    }

    /**
     * this methods is supposed to handle the collision of molecule entity with an atom entity.
     * since no action is done in this type of collision, the method is default.
     *
     * @param molecule to be checked if collision happened
     * @param atom to be checked if collision happened
     */
    default void handleCollision(Molecule molecule, Atom atom) {
    }


    // Blocker collision

    /**
     * this methods is supposed to handle the collision of blocker entity with a powerup entity.
     * since the collision action of blocker with powerup is already implemented from the powerup side,
     * the method is default.
     *
     * @param blocker to be checked if collision happened
     * @param powerup to be checked if collision happened
     */
    default void handleCollision(Blocker blocker, Powerup powerup) {
    }

    /**
     * this methods is supposed to handle the collision of blocker entity with a powerup entity.
     * since the collision action of blocker with molecule is already implemented from the molecule side,
     * the method is default.
     *
     * @param blocker to be checked if collision happened
     * @param molecule to be checked if collision happened
     */
    default void handleCollision(Blocker blocker, Molecule molecule) {
    }

    /**
     * this methods is supposed to handle the collision of blocker entity with a blocker entity.
     * since no action is done in this type of collision, the method is default.
     *
     * @param blocker1 to be checked if collision happened
     * @param blocker2 to be checked if collision happened
     */
    default void handleCollision(Blocker blocker1, Blocker blocker2) {
    }

    /**
     * this methods is to handle the collision of blocker entity with a shooter entity.
     * it will be overridden in collisionHandler class from the side of blocker entity.
     *
     * @param blocker to be checked if collision happened
     * @param shooter to be checked if collision happened
     */
    void handleCollision(Shooter shooter, Blocker blocker);

    /**
     * this methods is supposed to handle the collision of blocker entity with an atom entity.
     * since the collision action of blocker with an atom is already implemented from the atom side,
     * the method is default.
     *
     * @param blocker to be checked if collision happened
     * @param atom2 to be checked if collision happened
     */
    default void handleCollision(Blocker blocker, Atom atom2) {
        // todo: handle this after meeting and effect hitbox feature.
    }

    /**
     * this methods is to handle the collision of powerup entity with a shooter entity.
     * it will be overridden in collisionHandler class from the side of powerup entity.
     *
     * @param powerup to be checked if collision happened
     * @param shooter to be checked if collision happened
     */
    void handleCollision(Shooter shooter, Powerup powerup);

    // Shooter collision
    /* the methods of shooter collision with any entity has no action.
     * there is either no action done when a shooter collide with an entity, or the collision action
     * is already implemented from the side of the other entity
     *
     */

    default void handleCollision(Blocker blocker, Shooter shooter) {
    }

    default void handleCollision(Powerup powerup, Shooter shooter) {
    }

    default void handleCollision(Shooter shooter, Molecule molecule) {
    }

    default void handleCollision(Shooter shooter1, Shooter shooter2) {
    }


    default void handleCollision(Shooter shooter, Atom atom2) {
    }


}
