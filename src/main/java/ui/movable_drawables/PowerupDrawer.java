package ui.movable_drawables;

import model.game_entities.Powerup;
import services.utils.Coordinates;
import services.utils.MathUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;


/**
 * This class is responsible for drawing a Powerup given a Powerup entity in the constructor
 */
public class PowerupDrawer implements Drawable {

    private final Powerup powerup;
    private final Image powerupImage;

    public PowerupDrawer(Powerup powerup) {
        this.powerup = powerup;
        this.powerupImage = ImageResources.get(powerup);
    }

    /**
     * draw the powerup on the gave view
     * @param g Graphics instance passed to be used in drawing
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();

        g2d.rotate(Math.toRadians(powerup.getHitbox().getRotationDegree()),
                (int) powerup.getCoordinates().getX(),
                (int) powerup.getCoordinates().getY());

        Coordinates drawingCoordinates = MathUtils.drawingCoordinates(powerup.getCoordinates(),
                powerup.getHitbox().getWidth(),
                powerup.getHitbox().getHeight());
        g2d.drawImage(powerupImage, drawingCoordinates.getPoint().x, drawingCoordinates.getPoint().y, null);

        g2d.setTransform(old);
    }

    /**
     * draw a hit box around the powerup on the game view
     * @param g Graphics instance passed to be used in drawing
     */
    @Override
    public void drawHitbox(Graphics g) {
        Coordinates drawingCoordinates = MathUtils.drawingCoordinates(powerup.getCoordinates(),
                powerup.getHitbox().getWidth(),
                powerup.getHitbox().getHeight());

        g.drawOval(
                drawingCoordinates.getPoint().x,
                drawingCoordinates.getPoint().y,
                (int) powerup.getHitbox().getWidth(),
                (int) powerup.getHitbox().getHeight());
    }

}
