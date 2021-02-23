package ui.movable_drawables;

import model.game_building.Configuration;
import model.game_entities.Blocker;
import services.utils.Coordinates;
import services.utils.MathUtils;

import java.awt.*;

/**
 * This class is responsible for drawing an Blocker given the Blocker entity in the constructor
 */
public class BlockerDrawer implements Drawable {

    private final Blocker blocker;
    private final Image blockerImage;

    public BlockerDrawer(Blocker blocker) {
        this.blocker = blocker;
        this.blockerImage = ImageResources.get(blocker);
    }

    /**
     * draw the blocker on game view
     * @param g Graphics instance passed to be used in drawing
     */
    @Override
    public void draw(Graphics g) {
        Coordinates drawingCoordinates;
        if (Configuration.getInstance().isDiscoTheme())

            drawingCoordinates = MathUtils.drawingCoordinates(blocker.getCoordinates(),
                    2 * blocker.getHitbox().getWidth(),
                    2 * blocker.getHitbox().getHeight());
        else
            drawingCoordinates = MathUtils.drawingCoordinates(blocker.getCoordinates(),
                    blocker.getHitbox().getWidth(),
                    blocker.getHitbox().getHeight());

        g.drawImage(blockerImage, drawingCoordinates.getPoint().x, drawingCoordinates.getPoint().y, null);
    }

    /**
     * draw a hit box around the atom on the game view
     * @param g Graphics instance passed to be used in drawing
     */
    @Override
    public void drawHitbox(Graphics g) {
        Coordinates drawingCoordinates = MathUtils.drawingCoordinates(blocker.getCoordinates(),
                blocker.getHitbox().getWidth(),
                blocker.getHitbox().getHeight());

        Coordinates drawingCoordinates1 = MathUtils.drawingCoordinates(blocker.getCoordinates(),
                blocker.getBlockingHitbox().getWidth(),
                blocker.getBlockingHitbox().getHeight());

        Coordinates drawingCoordinates2 = MathUtils.drawingCoordinates(blocker.getCoordinates(),
                blocker.getExplodingHitbox().getWidth(),
                blocker.getExplodingHitbox().getHeight());

        g.drawOval(
                drawingCoordinates.getPoint().x,
                drawingCoordinates.getPoint().y,
                (int) blocker.getHitbox().getWidth(),
                (int) blocker.getHitbox().getHeight());

        g.drawOval(
                drawingCoordinates1.getPoint().x,
                drawingCoordinates1.getPoint().y,
                (int) blocker.getBlockingHitbox().getWidth(),
                (int) blocker.getBlockingHitbox().getHeight());

        g.drawOval(
                drawingCoordinates2.getPoint().x,
                drawingCoordinates2.getPoint().y,
                (int) blocker.getExplodingHitbox().getWidth(),
                (int) blocker.getExplodingHitbox().getHeight());
    }

}
