package model.game_running.listeners;

import model.game_building.GameBundle;

import java.util.ArrayList;

public interface SessionLoadListener {
    void onSessionListFetched(ArrayList<String> sessions);

    void onSessionRetrieved(GameBundle bundle);

    void onLoadFailed(String errorMessage);

    void getSavedSessions();
}
