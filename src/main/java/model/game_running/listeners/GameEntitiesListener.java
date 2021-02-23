package model.game_running.listeners;

import model.game_entities.AutonomousEntity;

import java.util.Collection;

public interface GameEntitiesListener {
    void onEntityAdd(AutonomousEntity entity);

    void onEntitiesRemove(Collection<AutonomousEntity> entities);

    /**
     * Reset all game components in the UI
     */
    void onGameReset();
}
