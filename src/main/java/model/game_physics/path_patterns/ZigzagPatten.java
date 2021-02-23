package model.game_physics.path_patterns;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import services.utils.Coordinates;
import services.utils.Vector;
import services.utils.Velocity;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A zigzag composite pattern that follows two interchanging straight pattern
 */
@JsonTypeName("zigzag-pattern")
@JsonIdentityReference(alwaysAsId = true)
public class ZigzagPatten extends PathPattern {
    private CompositePattern zigzagPattern;

    @SuppressWarnings("unused")
    public ZigzagPatten() {//this is needed for the save/load functionality
    }

    public ZigzagPatten(Velocity rightDiagonalVelocity, Velocity leftDiagonalVelocity, int rightDescendSteps, int leftDescentSteps, boolean rightFirst) {
        // prepare the composite pattern
        ArrayList<PathPattern> patterns = new ArrayList<>();
        ArrayList<Integer> iters = new ArrayList<>();

        // add the right diagonal pattern
        patterns.add(new StraightPattern(rightDiagonalVelocity));
        iters.add(rightDescendSteps);

        // add the left diagonal pattern
        patterns.add(new StraightPattern(leftDiagonalVelocity));
        iters.add(leftDescentSteps);

        if (!rightFirst) {
            // swap the patterns, and corresponding iterations
            Collections.swap(patterns, 0, 1);
            Collections.swap(iters, 0, 1);
        }

        // set the initial coordinates of the first pattern to the given initial coordinates
        this.zigzagPattern = new CompositePattern(patterns, iters);
    }

    public ZigzagPatten(Velocity rightDiagonalVelocity, Velocity leftDiagonalVelocity, int rightDescendSteps, int leftDescentSteps) {
        this(rightDiagonalVelocity, leftDiagonalVelocity, rightDescendSteps, leftDescentSteps, true);
    }

    public ZigzagPatten(Velocity diagonalVelocity, int iterationStep, boolean rightFirst) {
        // prepare the composite pattern
        ArrayList<PathPattern> patterns = new ArrayList<>();

        // add the right diagonal pattern
        patterns.add(new StraightPattern(diagonalVelocity));

        // add the left diagonal pattern
        patterns.add(new StraightPattern(new Velocity(-1 * diagonalVelocity.getXv(), diagonalVelocity.getYv())));

        if (!rightFirst) {
            // swap the patterns, and corresponding iterations
            Collections.swap(patterns, 0, 1);
        }

        // set the initial coordinates of the first pattern to the given initial coordinates
        this.zigzagPattern = new CompositePattern(patterns, iterationStep);
    }

    public CompositePattern getZigzagPattern() {
        return zigzagPattern;
    }

    public ZigzagPatten(Velocity diagonalVelocity, int iterationStep) {
        this(diagonalVelocity, iterationStep, true);
    }

    @Override
    @JsonIgnore
    public void setCurrentCoords(Coordinates currentCoords) {
        this.zigzagPattern.getCurrentPattern().setCurrentCoords(currentCoords);
    }

    @Override
    public Coordinates nextPosition() {
        return this.zigzagPattern.nextPosition();
    }

    @Override
    public void reflect(Vector n) {
        zigzagPattern.reflect(n);
    }
}