package ui.movable_drawables;

import java.awt.*;

public interface Drawable {

    /**
     * Draws objects in the game space
     *
     * @param g Graphics instance passed to be used in drawing
     */
    void draw(Graphics g);

    /**
     * Draws the hitbox of the entity in the game space
     *
     * @param g Graphics instance passed to be used in drawing
     */
    default void drawHitbox(Graphics g) {
    }
}
