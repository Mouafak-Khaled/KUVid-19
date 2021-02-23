package model.game_entities.factories;

import model.game_entities.Blocker;
import model.game_entities.enums.EntityType;
import model.game_physics.hitbox.HitboxFactory;
import model.game_physics.path_patterns.PathPatternFactory;
import services.utils.Coordinates;

public class BlockerFactory {

    private static BlockerFactory blockerFactory = new BlockerFactory();

    private BlockerFactory() {
    }

    public static BlockerFactory getInstance() {
        if (blockerFactory == null) {
            blockerFactory = new BlockerFactory();
        }
        return blockerFactory;
    }

    public Blocker getBlocker(EntityType type) {// TODO : modify implementation.
        Coordinates defaultCoordinates = new Coordinates(0, 0);
        return new Blocker(defaultCoordinates, HitboxFactory.getInstance().getBlockerHitbox(), HitboxFactory.getInstance().getBlockingHitbox(),
                HitboxFactory.getInstance().getExplosionHitbox(),
                PathPatternFactory.getInstance().getBlockerPathPattern(type), type);
    }
}
