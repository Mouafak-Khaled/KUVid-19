package services.database;

import services.utils.IOHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LocalDBAdapter implements IDatabase {

    private static LocalDBAdapter adapterInstance;

    private LocalDBAdapter() {
    }

    public static synchronized LocalDBAdapter getInstance() {
        if (adapterInstance == null)
            adapterInstance = new LocalDBAdapter();
        return adapterInstance;
    }

    @Override
    public <T> boolean save(String collectionTitle, String uniqueID, T instance) throws IOException {
        IOHandler.writeToYAML(instance, uniqueID, "sessions");
        return true;
    }

    @Override
    public <T> T load(String collectionTitle, String uniqueID, Class<T> tClass) throws IOException {
        return IOHandler.readFromYaml(uniqueID, "sessions", tClass);
    }

    @Override
    public List<String> getDocumentsIds(String collectionTitle) {
        return Arrays.asList(IOHandler.getFilesInDirectory("sessions"));
    }
}
