package services.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.bson.Document;
import services.utils.IOHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import static com.mongodb.client.model.Filters.eq;

/**
 * Handle the read, write and update operation on a local MongoDB.
 * This assumes that a cluster with the given address and port was already instantiated locally.
 */

public class MongoDBAdapter implements IDatabase {

    private static MongoDBAdapter instance;

    private final String CLUSTER_TITLE = "pepega-kuvid";
    private final String DB_TITLE = "pepega-kuvid";
    private final int HOST_PORT = 27017;
    private final String DOC_ID_KEY = "_id";
    private final String DOC_FILE_KEY = "_file";
    private final String USER_NAME = "pepega_shared";
    private final String PASSWORD = "69420";

    public static Logger logger = Logger.getLogger(MongoDBAdapter.class.getName());
    private MongoDatabase database;


    private MongoDBAdapter() {
        // prepare the connection
        String URL = "mongodb+srv://" + USER_NAME + ":" + PASSWORD + "@" + CLUSTER_TITLE + ".opsl3.mongodb.net/" + DB_TITLE + "?retryWrites=true&w=majority";
        MongoClientURI uri = new MongoClientURI(URL);
        MongoClient mongoClient = new MongoClient(uri);

//        if Local
//        MongoClient mongoClient = new MongoClient(CLUSTER_TITLE, HOST_PORT);
        this.database = mongoClient.getDatabase(DB_TITLE);
    }

    public static synchronized MongoDBAdapter getInstance() {
        if (instance == null)
            instance = new MongoDBAdapter();
        return instance;
    }

    @Override
    public boolean removeCollection(String collectionTitle) {
        logger.debug("[MongoDBAtlasAdapter] deleting a collection with title " + collectionTitle);
        MongoCollection<Document> collection = this.database.getCollection(collectionTitle);
        collection.drop();
        return true;
    }

    @Override
    public boolean registerCollection(String collectionTitle) {
        logger.debug("[MongoDBAtlasAdapter] creating a new collection with title " + collectionTitle);
        // create collection if not available
        boolean exists = false;
        for (String title : this.database.listCollectionNames()) {
            if (collectionTitle.equals(title))
                exists = true;
        }

        if (!exists) {
            this.database.createCollection(collectionTitle);
            return true;
        } else {
            logger.warn("[MongoDBAtlasAdapter] a collection with title " + collectionTitle + " already exist");
            return false;
        }
    }

    @Override
    public <T> boolean save(String collectionTitle, String uID, T instance) throws IOException {
        String ID = IOHandler.formatFileNameWithDate(uID, "");
        logger.debug("[MongoDBAtlasAdapter] saving instance with unique ID " + ID + " to the mongoDB");
        MongoCollection<Document> collection = this.database.getCollection(collectionTitle);
        // check if the uniqueID already exists
        if (collection.find(eq(DOC_ID_KEY, ID)).first() != null) {
            logger.warn("[MongoDBAtlasAdapter] trying to overwrite an entry with ID " + ID);
            return false;
        }

        String FileRepresentation = IOHandler.getYAMLRepresentation(instance);
        collection.insertOne(new Document().append(DOC_ID_KEY, ID).append(DOC_FILE_KEY, FileRepresentation));
        logger.info("[MongoDBAtlasAdapter] instance with unique ID " + ID + " was saved");
        return true;
    }

    @Override
    public <T> boolean update(String collectionTitle, String uniqueID, T instance) throws IOException {
        logger.debug("[MongoDBAtlasAdapter] updating instance with unique ID " + uniqueID + " to the mongoDB");
        MongoCollection<Document> collection = this.database.getCollection(collectionTitle);

        // check if the uniqueID does not exists
        if (collection.find(eq(DOC_ID_KEY, uniqueID)).first() == null) {
            logger.warn("[MongoDBAtlasAdapter] trying to update a non-existing entry with ID " + uniqueID);
        }

        String FileRepresentation = IOHandler.getYAMLRepresentation(instance);
        collection.updateOne(eq(DOC_ID_KEY, uniqueID), new Document().append(DOC_ID_KEY, uniqueID).append(DOC_FILE_KEY, FileRepresentation));
        logger.info("[MongoDBAtlasAdapter] instance with unique ID " + uniqueID + " was updating");
        return true;
    }

    @Override
    public boolean delete(String collectionTitle, String uniqueID) {
        logger.debug("[MongoDBAtlasAdapter] deleting instance with unique ID " + uniqueID + " in the mongoDB");
        MongoCollection<Document> collection = this.database.getCollection(collectionTitle);
        collection.deleteOne(eq(DOC_ID_KEY, uniqueID));
        logger.info("[MongoDBAtlasAdapter] instance with unique ID " + uniqueID + " was deleted");
        return true;
    }

    @Override
    public <T> T load(String collectionTitle, String uniqueID, Class<T> tClass) throws IOException {
        logger.info("[MongoDBAtlasAdapter] retrieving instance with unique ID " + uniqueID + " and of type " + tClass.getName() + " to the mongoDB");
        MongoCollection<Document> collection = this.database.getCollection(collectionTitle);

        try {
            String FileRepresentation = (String) collection.find(eq(DOC_ID_KEY, uniqueID)).first().get(DOC_FILE_KEY);
            return IOHandler.readFromYamlString(FileRepresentation, tClass);
        } catch (NullPointerException exception) {
            logger.error("[MongoDBAtlasAdapter] no instance exists with unique ID " + uniqueID);
            return null;
        }
    }

    @Override
    public ArrayList<String> getDocumentsIds(String collectionTitle) {
        MongoCollection<Document> collection = this.database.getCollection(collectionTitle);
        Iterator<String> iter = collection.distinct(DOC_ID_KEY, String.class).iterator();
        ArrayList<String> ids = new ArrayList<>();
        iter.forEachRemaining(ids::add);
        return ids;
    }
}
