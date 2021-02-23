package model.game_space;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GameTimer {
    private int remainingTimeMillis;
    private final int MIN_TO_MILLS_CONVERSION_CONSTANT = 60000;

    /**
     * given a number of minutes, it converts it to milliseconds and initializes the game timer.
     *
     * @param timeInMinutes the number of minutes with which the the timer will be initialized.
     */
    public GameTimer(double timeInMinutes) {
        this.remainingTimeMillis = (int) (timeInMinutes * MIN_TO_MILLS_CONVERSION_CONSTANT);
    }

    @SuppressWarnings("unused")
    public GameTimer() {//this is needed for the save/load functionality
    }

    /**
     * decreases the game timer.
     * usually decreased with the same delay given to the thready this is called from.
     *
     * @param timeInMillis the amount of time decreased in millisecond.
     */
    public void decrease(int timeInMillis) {
        remainingTimeMillis -= timeInMillis;
    }

    @JsonIgnore
    public String getCurrentTimer() {
        return getMinutesCounter() + " : " + getSecondsCounter();
    }

    public int getRemainingTimeMillis() {
        return this.remainingTimeMillis;
    }

    /**
     * converts the remaining time from millisecond to minutes
     *
     * @return returns the remaining minutes (floored)
     */
    @JsonIgnore
    private String getMinutesCounter() {
        int remainingMinutes = remainingTimeMillis / MIN_TO_MILLS_CONVERSION_CONSTANT;
        return remainingMinutes >= 10 ? String.valueOf(remainingMinutes) : ("0" + remainingMinutes);
    }

    /**
     * gets the number of seconds remaining in the current minute
     *
     * @return the number of seconds remaining in a minute
     */
    @JsonIgnore
    private String getSecondsCounter() {
        int secondsInMillis = remainingTimeMillis % MIN_TO_MILLS_CONVERSION_CONSTANT;
        int remainingSeconds = secondsInMillis / 1000;
        return remainingSeconds >= 10 ? String.valueOf(remainingSeconds) : ("0" + remainingSeconds);
    }

    @Override
    public String toString() {
        return "GameTimer{" +
                "remainingTimeMillis=" + remainingTimeMillis +
                '}';
    }
}
