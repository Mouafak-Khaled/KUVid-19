package model.game_physics.hitbox;

import model.game_building.Configuration;
import model.game_building.GameConstants;

/**
 * a factory for hitbox that contains the logic of creating hitbox for each type of entity
 */
public class HitboxFactory {

    private static HitboxFactory factory = new HitboxFactory();
    private final Configuration config;

    private HitboxFactory(){
        this.config = Configuration.getInstance();
    }
    public static HitboxFactory getInstance(){
        if(factory == null){
            factory = new HitboxFactory();
        }
            return factory;
    }

    public Hitbox getBlockerHitbox(){
        return  new CircularHitbox(config.getUnitL() * GameConstants.BLOCKER_RADIUS);
    }
    public Hitbox getBlockingHitbox(){
        return  new CircularHitbox(Configuration.getInstance().getUnitL() * GameConstants.BLOCKER_BLOCKING_RADIUS);
    }

    public Hitbox getExplosionHitbox(){
        return  new CircularHitbox(Configuration.getInstance().getUnitL() * GameConstants.BLOCKER_EXPLOSION_RADIUS);
    }

    public Hitbox getAtomHitbox(){
        return new CircularHitbox(config.getUnitL() * GameConstants.ATOM_RADIUS);
    }
    public Hitbox getMoleculeHitbox(){
        return new CircularHitbox(config.getUnitL() * GameConstants.MOLECULE_RADIUS);
    }
    public Hitbox getPowerUpHitbox(){
        return new RectangularHitbox(config.getUnitL() * GameConstants.POWERUP_RADIUS * 2,
                config.getUnitL() * GameConstants.POWERUP_RADIUS * 2);
    }
    public Hitbox getShooterHitbox(){
        return new RectangularHitbox(
                config.getUnitL() * GameConstants.SHOOTER_WIDTH,
                config.getUnitL() * GameConstants.SHOOTER_HEIGHT);
    }

    public Hitbox getLinearMoleculeHitbox() {
        return new RectangularHitbox(
                config.getUnitL() * GameConstants.MOLECULE_RADIUS * 2,
                config.getUnitL() * GameConstants.LINEAR_MOLECULE_HEIGHT);
    }
}
