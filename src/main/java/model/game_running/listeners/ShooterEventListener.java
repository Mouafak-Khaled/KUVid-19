package model.game_running.listeners;

public interface ShooterEventListener {
    /**
     * called when shooting event happens in the shooter
     */
    void onShot();

    /**
     * this is triggered when the shooter moves
     */
    void onMoved();

    /**
     * called when the shooter stops moving
     */
    void onStopped();
}
