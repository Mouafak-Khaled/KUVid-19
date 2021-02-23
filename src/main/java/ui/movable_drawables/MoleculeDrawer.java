package ui.movable_drawables;

import model.game_entities.Molecule;
import model.game_entities.enums.MoleculeStructure;
import services.utils.Coordinates;
import services.utils.MathUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * This class is responsible for drawing a Molecule given a Molecule entity in the constructor
 */
public class MoleculeDrawer implements Drawable {

    private final Molecule molecule;
    private final Image moleculeImage;

    public MoleculeDrawer(Molecule molecule) {
        this.molecule = molecule;
        this.moleculeImage = ImageResources.get(molecule);
    }

    /**
     * draw molecule on game view
     * @param g Graphics instance passed to be used in drawing
     */
    @Override
    public void draw(Graphics g) {
        // rotate the molecule
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        g2d.rotate(Math.toRadians(molecule.getRotationDegree()), molecule.getCoordinates().getPoint().x, molecule.getCoordinates().getPoint().y);
        Coordinates drawingCoordinates = MathUtils.drawingCoordinates(molecule.getCoordinates(),
                molecule.getHitbox().getWidth(), molecule.getHitbox().getHeight());
        g2d.drawImage(moleculeImage, drawingCoordinates.getPoint().x, drawingCoordinates.getPoint().y, null);
        g2d.setTransform(old);

    }

    /**
     * draw a hit box around the molecule on the game view
     * @param g Graphics instance passed to be used in drawing
     */
    @Override
    public void drawHitbox(Graphics g) {
        Coordinates drawingCoordinates = MathUtils.drawingCoordinates(molecule.getCoordinates(),
                molecule.getHitbox().getWidth(),
                molecule.getHitbox().getHeight());

        if (molecule.getStructure() == MoleculeStructure.CIRCULAR)
            g.drawOval(
                    drawingCoordinates.getPoint().x,
                    drawingCoordinates.getPoint().y,
                    (int) molecule.getHitbox().getWidth(),
                    (int) molecule.getHitbox().getHeight());
        else
            g.drawRect(
                    drawingCoordinates.getPoint().x,
                    drawingCoordinates.getPoint().y,
                    (int) molecule.getHitbox().getWidth(),
                    (int) molecule.getHitbox().getHeight());
    }
}
