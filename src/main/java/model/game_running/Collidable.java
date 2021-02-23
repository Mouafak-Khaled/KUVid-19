package model.game_running;

import model.game_entities.*;

public interface Collidable {
    void collideWith(CollisionVisitor visitor, Atom atom);
    void collideWith(CollisionVisitor visitor, Blocker blocker);
    void collideWith(CollisionVisitor visitor, Molecule molecule);
    void collideWith(CollisionVisitor visitor, Powerup powerup);
    void collideWith(CollisionVisitor visitor, Shooter shooter);
    void acceptCollision(CollisionVisitor visitor, Entity entity);
}
