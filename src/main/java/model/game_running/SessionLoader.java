package model.game_running;

import model.game_building.GameBundle;
import model.game_building.GameConstants;
import model.game_running.listeners.SessionLoadListener;
import org.apache.log4j.Logger;
import services.database.IDatabase;
import services.database.LocalDBAdapter;
import services.database.MongoDBAdapter;
import services.utils.IOHandler;

import java.io.IOException;
import java.util.ArrayList;

public class SessionLoader {

    private final SessionLoadListener loadListener;

    private IDatabase dbAdapter;
    private static Logger logger;

    ArrayList<String> sessions;

    public SessionLoader(SessionLoadListener loadListener) {
        this.loadListener = loadListener;
        logger = Logger.getLogger(this.getClass().getName());
    }

    /**
     * Retrieve a list of saved sessions and pass them to the load listener
     */
    public synchronized void fetchSavedSessions(String database) {
        dbAdapter = getDatabaseAdapter(database);
        ArrayList<String> sessions = new ArrayList<>(dbAdapter.getDocumentsIds(GameConstants.SESSION_COLLECTION_TITLE));
        loadListener.onSessionListFetched(sessions);
    }

    /**
     * Given an ID retrieve a save that corresponds to that ID and pass it to the load listener
     * @param sessionID
     */
    public void retrieveSession(String sessionID) {
        GameBundle loadedBundle = null;
        try {
            loadedBundle = dbAdapter.load(GameConstants.SESSION_COLLECTION_TITLE, IOHandler.prettyToProperFileName(sessionID), GameBundle.class);
        } catch (IOException e) {
            e.printStackTrace();
            loadListener.onLoadFailed("Could not load the selected game session.");
        }
        loadListener.onSessionRetrieved(loadedBundle);
    }

    /**
     * Given a database type as a String return the database adapter object.
     * @param databaseType
     * @return a database adapter
     */
    private IDatabase getDatabaseAdapter(String databaseType){
        if(databaseType.equalsIgnoreCase("local"))
            return LocalDBAdapter.getInstance();
        else
            return MongoDBAdapter.getInstance();
    }

    /**
     * resets the lists of saved sessions to null, so that they are updated on the next fetch call
     */
    public void resetList() {
        sessions = null;
    }

}
