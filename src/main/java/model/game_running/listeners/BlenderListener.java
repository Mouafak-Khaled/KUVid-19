package model.game_running.listeners;

public interface BlenderListener {
    /**
     * this method is called after game parameters get checked and proved valid.
     */
    void onBlend();

    void onFailBlend();

    void onShow();
}
