package model.game_running.states;


import model.game_entities.enums.ShieldType;

public interface GameState {
    void saveGameSession();

    void showSavedSessions();

    void applyShield(ShieldType shieldType);

    void rotateShooter(int direction);

    void moveShooter(int direction);

    void resume();

    void pause();
}
