package model.game_space;


import model.game_building.GameConstants;
import model.game_running.ProjectileContainer;
import model.game_running.listeners.BlenderListener;
import services.exceptions.ContainerNotInitializedException;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Blender {

    private static final Logger logger = Logger.getLogger(Blender.class.getName());

    private ProjectileContainer projectileContainer;
    private BlenderListener blenderListener;

    public Blender(ProjectileContainer projectileContainer) {
        this.projectileContainer = projectileContainer;
    }

    /**
     * Blends/Breaks a number of source atoms into a number of target atoms
     *
     * @param sourceAtom       The atom to be blended
     * @param destinationAtom  The result atom
     * @param numOfConversions The number of the desired atom.
     */
    public void convert(int sourceAtom, int destinationAtom, int numOfConversions) throws Exception {
        if (sourceAtom > destinationAtom) {
            breakAtoms(sourceAtom, destinationAtom, numOfConversions);
        } else if (sourceAtom < destinationAtom) {
            blendAtoms(sourceAtom, destinationAtom, numOfConversions);
        }
        blenderListener.onBlend();
    }

    /**
     * Blends a number of source atoms into a number of target atoms.
     *
     * @param sourceAtom       The atom to be blended.
     * @param destinationAtom  The result atom.
     * @param numOfConversions The number of the desired atom.
     */
    public void blendAtoms(int sourceAtom, int destinationAtom, int numOfConversions) throws ContainerNotInitializedException {
        //MODIFIES: projectileContainer
        //EFFECTS:if the projectileContainer has enough atoms to blend it decreases the number of sourceAtom by a
        // certain number, and increases the number of destinationAtoms in projectile container, and does nothing when
        // there is not enough sourceAtoms to blend into destinationAtoms.
        if (this.projectileContainer == null)
            throw new ContainerNotInitializedException();
        boolean canBlend;
        for (int i = 0; i < numOfConversions; i++) {
            canBlend = projectileContainer.decreaseAtoms(sourceAtom, (int) GameConstants.BLENDING_MATRIX[sourceAtom][destinationAtom]);
            if (!canBlend)
                break;
            projectileContainer.increaseAtoms(destinationAtom, 1, null);
        }
    }

    /**
     * Breaks a number of source atoms into a number of target atoms.
     *
     * @param sourceAtom       The atom to be blended.
     * @param destinationAtom  The result atom.
     * @param numOfConversions The number of the desired atom.
     */
    private void breakAtoms(int sourceAtom, int destinationAtom, int numOfConversions) throws ContainerNotInitializedException {
        boolean canBlend;
        if (this.projectileContainer == null)
            throw new ContainerNotInitializedException();
        for (int i = 0; i < numOfConversions; i++) {
            canBlend = projectileContainer.decreaseAtoms(sourceAtom, 1);
            if (!canBlend)
                break;
            projectileContainer.increaseAtoms(destinationAtom, (int) GameConstants.BLENDING_MATRIX[sourceAtom][destinationAtom], null);
        }
    }

    public void showBlender() {
        blenderListener.onShow();
    }

    public void setBlenderListener(BlenderListener blenderListener) {
        this.blenderListener = blenderListener;
    }

    public void setProjectileContainer(ProjectileContainer projectileContainer) {
        this.projectileContainer = projectileContainer;
    }

}
