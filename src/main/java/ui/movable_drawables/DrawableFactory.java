package ui.movable_drawables;

import model.game_entities.*;

public class DrawableFactory {

    /**
     * @param entity an entity instance that will use draw to draw itself
     * @return a drawable for the entity given
     */
    public static Drawable get(Entity entity) {
        switch (entity.getSuperType()) {
            case MOLECULE:
                return new MoleculeDrawer((Molecule) entity);
            case ATOM:
                return new AtomDrawer((Atom) entity);
            case BLOCKER:
                return new BlockerDrawer((Blocker) entity);
            case SHOOTER:
                return new ShooterDrawer((Shooter) entity);
            case POWERUP:
                return new PowerupDrawer((Powerup) entity);
            default:
                throw new IllegalArgumentException("Entity type is not correct");
        }
    }


}
