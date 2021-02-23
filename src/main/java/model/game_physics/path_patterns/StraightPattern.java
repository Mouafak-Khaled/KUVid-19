package model.game_physics.path_patterns;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonTypeName;
import services.utils.Coordinates;
import services.utils.Vector;
import services.utils.Velocity;

/**
 * Straight path pattern with a constant speed.
 */
@JsonTypeName("straight-pattern")
@JsonIdentityReference(alwaysAsId = true)
public class StraightPattern extends PathPattern {
    private Velocity initialVelocity;
    private int sinceReflected = 100;

    @SuppressWarnings("unused")
    public StraightPattern() {//this is needed for the save/load functionality
    }

    public StraightPattern(Coordinates initialCoords, Velocity initialVelocity) {
        super(initialCoords);
        this.initialVelocity = initialVelocity;
    }

    public StraightPattern(Velocity initialVelocity) {
        this.initialVelocity = initialVelocity;
    }

    public Velocity getInitialVelocity() {
        return initialVelocity;
    }


    @Override
    public Coordinates nextPosition() {
        setCurrentCoords(new Coordinates(getInitialVelocity().getXv() + getCurrentCoords().getX(),
                getInitialVelocity().getYv() + getCurrentCoords().getY()));
        this.sinceReflected++;
        return getCurrentCoords();
    }

    @Override
    public void reflect(Vector n) {
        if(sinceReflected > 3) {
            this.initialVelocity = initialVelocity.reflect(n);
            this.sinceReflected = 0;
            PathPattern.logger.debug("[StraightPattern] pattern reflected");
        }
        else {
            PathPattern.logger.debug("[StraightPattern] pattern was already reflected");
        }
    }

    @Override
    public void setVelocity(double velocity) {
        initialVelocity = new Velocity(initialVelocity.getXv() * velocity, initialVelocity.getYv() * velocity);
    }
}
