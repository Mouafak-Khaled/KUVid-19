package services.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.log4j.Logger;
import services.exceptions.UnsupportedNameFormatException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IOHandler {

    private static final ObjectMapper YAMLMapper = new ObjectMapper(new YAMLFactory());
    private static final ObjectMapper JSONMapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(IOHandler.class.getName());

    /**
     * writes an object to a YAML file.
     *
     * @param obj      the object to be saved.
     * @param fileName the base name of the file of the saved object.
     */
    public static void writeToYAML(Object obj, String fileName, String directoryName) throws IOException {
        String name = formatFileNameWithDate(fileName, ".yaml");
        writeFileWithMapper(obj, name, directoryName, YAMLMapper);
    }

    public static void writeToJSON(Object obj, String fileName, String directoryName) throws IOException {
        String name = formatFileNameWithDate(fileName, ".json");
        writeFileWithMapper(obj, name, directoryName, JSONMapper);
    }

    public static String getYAMLRepresentation(Object obj) throws IOException {
        return YAMLMapper.writeValueAsString(obj);
    }

    private static void writeFileWithMapper(Object obj, String fileName, String directoryName, ObjectMapper mapper) throws IOException {
        mapper.writeValue(new File(System.getProperty("user.dir") + "/" + directoryName + "/" + fileName), obj);
    }

    /**
     * writes the selected object into a temporary file.
     *
     * @param obj the temporary object to be written to the file.
     */
    public static void writeToYAML(Object obj) throws IOException {
        writeFileWithMapper(obj, "temp.yaml", "configurations", YAMLMapper);
    }

    /**
     * Returns an Object that is loaded from the YAML file with the given name
     *
     * @param fileName name of the YAML file to be read.
     * @param directory
     * @return a new object read from the YAML file
     */
    public static <T> T readFromYaml(String fileName, String directory, Class<T> tClass) throws IOException {
        return YAMLMapper.readValue(new File(System.getProperty("user.dir") + "/" + directory + "/" + fileName + ".yaml"), tClass);
    }

    /**
     * Returns an Object that is loaded from the YAML file with the given name
     *
     * @param content the value of the object in yaml
     * @return a new object read from the YAML file
     * TODO: change the signature of readFromYaml to readFromYamlFile
     */
    public static <T> T readFromYamlString(String content, Class<T> tClass) throws IOException {
        return YAMLMapper.readValue(content, tClass);
    }

    /**
     * Return the names of the files inside the configuration directory
     *
     * @param directoryName name of the directory to fetch file names from
     * @return a list of strings containing the names, potentially null
     */
    public static String[] getFilesInDirectory(String directoryName) {
        File directory = new File(System.getProperty("user.dir") + "/" + directoryName + "/");
        return directory.list();
    }

    public static String formatFileNameWithDate(String rawName, String extension) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy--HH-mm");
        Date currentData = new Date();
        return rawName + "-" + formatter.format(currentData) + extension;
    }

    /**
     * makes preset file names more readable. needed when displaying preset options for the player.
     *
     * @param name preset file raw name
     * @return prettified name
     */
    public static String prettifyYAMLFileName(String name) throws NullPointerException, UnsupportedNameFormatException {
        // @effects: given a raw file name formatted with data, returns the name in a more user-friendly format.
        // if it given file is a default YAML file, returns "Default"
        // if the name is not in the right format, throws an UnsupportedNameFormatException
        // throws a null pointer exception if the given string is null
        if (name.startsWith("Default") && name.endsWith(".yaml"))
            return "Default";
        String[] nameComponents = name.split("-");
        try {
            return String.format("%-10s\t%s/%s/%s %s:%s",
                    nameComponents[0],
                    nameComponents[1],
                    nameComponents[2],
                    nameComponents[3], //skipping 4th index because it's empty
                    nameComponents[5],
                    nameComponents[6].substring(0, 2));
        } catch (IndexOutOfBoundsException e) {
            throw new UnsupportedNameFormatException(name);
        }
    }

    /**
     * converts a prettified file name to its raw format so that it can be recognized by the read method.
     *
     * @param prettyName prettified name
     * @return raw format file name
     */
    public static String prettyToProperFileName(String prettyName) {
        String[] nameComponents = prettyName.split("\\s+");
        try {
            String[] dateComponents = nameComponents[1].split("/");
            String[] timeComponents = nameComponents[2].split(":");
            return String.format("%s-%s-%s-%s--%s-%s", nameComponents[0],
                    dateComponents[0],
                    dateComponents[1],
                    dateComponents[2],
                    timeComponents[0],
                    timeComponents[1]);
        } catch (IndexOutOfBoundsException e) {
            logger.warn("[IOHandler] prettyToProperFileName: Unrecognized name format. returning without modification");
            return prettyName;
        }
    }
}
