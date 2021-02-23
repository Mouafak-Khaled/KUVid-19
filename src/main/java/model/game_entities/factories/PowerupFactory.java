package model.game_entities.factories;


import model.game_entities.Powerup;
import model.game_entities.enums.EntityType;
import model.game_physics.hitbox.HitboxFactory;
import model.game_physics.path_patterns.PathPatternFactory;
import services.utils.Coordinates;

public class PowerupFactory { // TODO: This class may not be needed. Do it like projectile container.

    private static PowerupFactory powerupFactory = new PowerupFactory();

    private PowerupFactory() {
    }

    public static PowerupFactory getInstance() {
        if (powerupFactory == null) {
            powerupFactory = new PowerupFactory();
        }
        return powerupFactory;
    }

    public Powerup getPowerup(EntityType type) {// TODO : modify implementation.
        Coordinates defaultCoordinates = new Coordinates(0, 0);

        return new Powerup(defaultCoordinates, HitboxFactory.getInstance().getPowerUpHitbox(),
                PathPatternFactory.getInstance().getPowerUpPathPattern(), type, true);
    }
}


