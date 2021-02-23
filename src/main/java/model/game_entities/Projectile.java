package model.game_entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import model.game_entities.enums.EntityType;
import model.game_physics.hitbox.Hitbox;
import model.game_physics.path_patterns.PathPattern;
import services.utils.Coordinates;
/**
 * Projectile: a superclass for all the game objects that can be shot by the Shooter.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "type") @JsonSubTypes({
        @JsonSubTypes.Type(value = Atom.class, name = "atom"),
        @JsonSubTypes.Type(value = Powerup.class, name = "powerup")
})
public abstract class Projectile extends AutonomousEntity{
    public Projectile(Coordinates coordinates, Hitbox hitbox, PathPattern pathPattern, EntityType type) {
        super(coordinates, hitbox, pathPattern, type);
    }

    public void setVelocity(double velocity){
        getPathPattern().setVelocity(velocity);
    }

    @JsonIgnore
    public abstract double getSpeedPercentage();
}
