package ui.movable_drawables;

import model.game_entities.Atom;
import services.utils.Coordinates;
import services.utils.MathUtils;

import java.awt.*;

/**
 * This class is responsible for drawing an Atom given the Atom entity in the constructor
 */
public class AtomDrawer implements Drawable {

    private final Atom atom;
    private final Image atomImage;

    public AtomDrawer(Atom atom) {
        this.atom = atom;
        this.atomImage = ImageResources.get(atom);
    }

    /**
     * draw the atom on game view
     * @param g Graphics instance passed to be used in drawing
     */
    @Override
    public void draw(Graphics g) {
        Coordinates drawingCoordinates = MathUtils.drawingCoordinates(atom.getCoordinates(),
                atom.getHitbox().getWidth(),
                atom.getHitbox().getHeight());

        g.drawImage(atomImage, drawingCoordinates.getPoint().x, drawingCoordinates.getPoint().y, null);

    }

    /**
     * draw a hit box around the atom on the game view
     * @param g Graphics instance passed to be used in drawing
     */
    @Override
    public void drawHitbox(Graphics g) {
        Coordinates drawingCoordinates = MathUtils.drawingCoordinates(atom.getCoordinates(),
                atom.getHitbox().getWidth(),
                atom.getHitbox().getHeight());

        g.drawOval(
                drawingCoordinates.getPoint().x,
                drawingCoordinates.getPoint().y,
                (int) atom.getHitbox().getWidth(),
                (int) atom.getHitbox().getHeight());
    }
}
