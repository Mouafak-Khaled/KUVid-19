package model.game_running;

import model.game_building.GameConstants;
import model.game_entities.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class CollisionHandler implements CollisionVisitor {

    RunningMode controller;
    private final OnCollisionListener onCollisionListener;

    CollisionHandler(RunningMode controller, OnCollisionListener onCollisionListener) {
        this.controller = controller;
        this.onCollisionListener = onCollisionListener;

    }

    /**
     * this method calls the removeEntity method of the runningMode object to remove the entities from the game view
     *
     * @param entity1 first autonomousEntity
     * @param entity2 second autonomousEntity
     */
    private void defaultCollision(AutonomousEntity entity1, AutonomousEntity entity2) {
        controller.removeEntity(entity1);
        controller.removeEntity(entity2);
    }


    @Override
    public void handleCollision(Atom atom, Molecule molecule) {
        if (atom.getEntityType().getValue() == molecule.getEntityType().getValue()) {
            controller.increaseScore(atom.getEfficiency());
            defaultCollision(atom, molecule);

            if (onCollisionListener != null) {
                try {
                    onCollisionListener.onAtomMoleculeCollision();
                } catch (Exception e) {
                    e.printStackTrace();

            }
        }
    }
    }

    @Override
    public void handleCollision(Atom atom, Blocker blocker) {
        // this only breaks the atom if enters the AOE of a corresponding type blocker.

        if (blocker.isExploded()) {
            controller.removeEntity(atom);
        } else {
            if (blocker.isCollidedWithBlockingHitbox(atom))
                if (atom.getEntityType().getValue() == blocker.getEntityType().getValue()) {
                    controller.removeEntity(atom);
                    try {
                        onCollisionListener.onAtomBlockerCollision();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    @Override
    public void handleCollision(Powerup powerup, Blocker blocker) {
        if (blocker.isCollidedWithBlockingHitbox(powerup))
            if ((blocker.getEntityType().getValue() == powerup.getEntityType().getValue()) && !powerup.isFalling()) {
                defaultCollision(powerup, blocker);
                try {
                    onCollisionListener.onPowerupBlockerCollision();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }

    @Override
    public void handleCollision(Shooter shooter, Powerup powerup) {
        if (powerup.isFalling()) {
            controller.collectPowerUp(powerup);
            controller.removeEntity(powerup);
            try {
                onCollisionListener.onShooterPowerupCollision();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleCollision(Molecule molecule, Blocker blocker) {
//        defaultCollision(molecule, blocker);
        //nothing for now. this collision will be conditional: only when the blocker is exploding.
        if (blocker.isExploded()) {
            controller.removeEntity(molecule);
        }
    }

    @Override
    public void handleCollision(Shooter shooter, Blocker blocker) {
        // decrease the health of the player.
        // check for close atom and molecules and destroy them.
        double damageDone;

        try {
        if (blocker.isExploded()) {
            damageDone = blocker.getExplosionDamage(shooter);
            controller.updateHealth(damageDone);
            controller.removeEntity(blocker);
            this.onCollisionListener.onShooterBlockerCollision();
        } else if (blocker.isCollidedWithOriginalHitbox(shooter)) {
            blocker.setExploded(true);
            controller.updateHealth(GameConstants.TERMINATING_DAMAGE);
            controller.removeEntity(blocker);
            this.onCollisionListener.onPowerupBlockerCollision();
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnCollisionListener {

        void onShooterBlockerCollision() throws UnsupportedAudioFileException, IOException, LineUnavailableException;

        void onAtomMoleculeCollision() throws UnsupportedAudioFileException, IOException, LineUnavailableException;

        void onShooterPowerupCollision() throws UnsupportedAudioFileException, IOException, LineUnavailableException;

        void onPowerupBlockerCollision() throws UnsupportedAudioFileException, IOException, LineUnavailableException;

        void onAtomBlockerCollision() throws UnsupportedAudioFileException, IOException, LineUnavailableException;

    }
}
