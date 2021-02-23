package model.game_physics.path_patterns;
import model.game_building.Configuration;
import model.game_building.GameConstants;
import model.game_entities.enums.EntityType;
import services.utils.Vector;
import services.utils.Velocity;

import java.security.InvalidParameterException;
import java.util.Arrays;

// TODO: get the entity speed from the configuration
public class PathPatternFactory {
    private static PathPatternFactory instance = null;
    private final Configuration config;

    private PathPatternFactory(){
        config = Configuration.getInstance();
    }

    public static PathPatternFactory getInstance(){
        if(instance == null){
            instance = new PathPatternFactory();
        }
        return instance;
    }

    public PathPattern getBlockerPathPattern(EntityType type){
        return getEntityTypePathPattern(type);
    }

    public PathPattern getMoleculePathPattern(EntityType type){
        return getEntityTypePathPattern(type);
    }

    public PathPattern getPowerUpPathPattern(){
        // powerUp always fall in a straight pattern downward
        return new StraightPattern(new Velocity(0, config.getStraightPatternSpeed()));
    }

    public PathPattern getAtomPathPattern(double angle){
        // rotate the upward vector speed
        Vector rotatedVector = (new Vector(0, -config.getAtomSpeed())).rotateVector(angle);
        return new StraightPattern(new Velocity(rotatedVector));
    }
    public PathPattern getAtomPathPattern(){
        return getAtomPathPattern(0);
    }

    private PathPattern getEntityTypePathPattern(EntityType type){
        // typical Straight pattern
        Velocity straightVelocity = new Velocity(0, config.getStraightPatternSpeed());

        // typical Zigzag pattern
        Velocity zigZagVelocity = new Velocity(new Vector(0, config.getZigZagPatternSpeed())
                .rotateVector(GameConstants.ZIGZAG_SPEED_ANGLE));

        switch (type){
            case ALPHA:
                // follow a zigzag pattern
                return new ZigzagPatten(zigZagVelocity, GameConstants.FPS);
            case BETA:
                // 25% straight, then 75% zigzag
                PathPattern pattern1 = new StraightPattern(straightVelocity);
                PathPattern pattern2 = new ZigzagPatten(zigZagVelocity, GameConstants.FPS);
                return new RatioPattern(Arrays.asList(pattern1, pattern2), Arrays.asList(0.25, 0.75));
            case GAMMA:
                // 50% straight, then 50% zigzag
                pattern1 = new StraightPattern(straightVelocity);
                pattern2 = new ZigzagPatten(zigZagVelocity, GameConstants.FPS);
                return new RatioPattern(Arrays.asList(pattern1, pattern2), Arrays.asList(0.5, 0.5));

            case SIGMA:
                // follow a straight pattern
                return new StraightPattern(straightVelocity);
            default:
                throw new InvalidParameterException("[PathPatternFactory] Entity type is not supported");
        }
    }
}
