package services.database;

import org.bson.Document;
import org.junit.jupiter.api.*;
import services.utils.IOHandler;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MongoDBAdapterTest {

    private MongoDBAdapter database;
    private String COLLECTION_TITLE = "DBTest";

    @BeforeAll
    void connectToDB(){
        this.database = MongoDBAdapter.getInstance();
        this.database.removeCollection(COLLECTION_TITLE);
    }

    @BeforeEach
    void updateCollection(){
        // remove the collection to start fresh
        this.database.removeCollection(COLLECTION_TITLE);
    }

    @AfterAll
    void disconnect(){
        // delete the temporary collection
        this.database.removeCollection(COLLECTION_TITLE);
    }

    @Test
    void saveLoadDocumentTest() {
        String uID = "1234";
        // create a collection
        this.database.registerCollection(COLLECTION_TITLE);

        // insert a document
        try {
            this.database.save(COLLECTION_TITLE, uID, new Document("Pepega", "Best project of the year"));
        } catch (IOException e) {
            fail();
        }
        //retrieve the document
        try {
            String ID = IOHandler.formatFileNameWithDate(uID, "");
            Document doc = this.database.load(COLLECTION_TITLE, ID, Document.class);
            assertEquals("Best project of the year", doc.get("Pepega"));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void saveLoadDummyTest() {
        String uID = "1234";

        // create a collection
        this.database.registerCollection(COLLECTION_TITLE);

        // insert a document
        try {
            this.database.save(COLLECTION_TITLE, uID, new DummyClass("Pepega", 21));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        //retrieve the document
        DummyClass dummyClass = null;
        try {
            String ID = IOHandler.formatFileNameWithDate(uID, "");
            dummyClass = this.database.load(COLLECTION_TITLE, ID, DummyClass.class);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(21, dummyClass.getAge());
    }
}