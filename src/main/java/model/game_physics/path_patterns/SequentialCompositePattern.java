package model.game_physics.path_patterns;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonTypeName;
import services.utils.Coordinates;
import services.utils.Vector;

import java.util.ArrayList;

/**
 * Path patterns that follows N different path patterns. It follows a pattern until it reaches
 * the initial coordinates of the next pattern, then it follows the next path pattern until it reaches the
 * initialCoordinates of the next path pattern, and so on and so forth.
 */
@JsonTypeName("sequential-pattern")
@JsonIdentityReference(alwaysAsId = true)
public class SequentialCompositePattern extends PathPattern{
    private final ArrayList<PathPattern> patterns;
    private PathPattern currentPattern;
    private int currentPatternIdx;
    private Coordinates currentCoords;

    public SequentialCompositePattern(ArrayList<PathPattern> patterns) {
        super(patterns.get(0).getCurrentCoords());
        this.patterns = patterns;
        this.currentPatternIdx = 0;
        setCurrentPattern(patterns.get(this.currentPatternIdx));
    }

    public ArrayList<PathPattern> getPatterns() {
        return patterns;
    }

    public PathPattern getCurrentPattern() {
        return currentPattern;
    }

    public void setCurrentPattern(PathPattern currentPattern) {
        this.currentPattern = currentPattern;
    }

    @Override
    public Coordinates nextPosition() {
        // check if we reached the next path pattern.
        if (currentCoords.equals(getPatterns().get(currentPatternIdx + 1).getCurrentCoords())){
            this.currentPatternIdx += 1;
            setCurrentPattern(getPatterns().get(this.currentPatternIdx));
        }
        this.currentCoords = getCurrentPattern().nextPosition();
        return this.currentCoords;
    }

    @Override
    public void reflect(Vector n) {
        // TODO: implement reflect or delete the whole pattern (We have not used this pattern yet)
    }
}
