package services.utils;

import org.junit.jupiter.api.Test;
import services.exceptions.UnsupportedNameFormatException;

import static org.junit.jupiter.api.Assertions.*;

class IOHandlerTest {

    ///// TEST STRINGS
    String gbTest1String = "preset-26-02-99--10-54";
    //preset   26-02-99--10-54
    String gbResult1String = String.format("%-10s\t%s/%s/%s %s:%s", "preset", "26", "02", "99", "10", "54");

    String gbTest2String = "preset-26-feb-99--15-70";
    //preset   26-feb-99--15-70
    String gbResult2String = String.format("%-10s\t%s/%s/%s %s:%s", "preset", "26", "feb", "99", "15", "70");

    String gbTest3String = "preset-26-feb-99--15-244";
    //preset   26-feb-99--15-244
    String gbResult3String = String.format("%-10s\t%s/%s/%s %s:%s", "preset", "26", "feb", "99", "15", "244");

    @Test
    void prettifyFileNameBlackBox() {
        // expecting exceptions
        assertThrows(NullPointerException.class, () -> IOHandler.prettifyYAMLFileName(null));
        assertThrows(UnsupportedNameFormatException.class, () -> IOHandler.prettifyYAMLFileName("somefile.file"));
        // prettifying a Default yaml file
        try {
            assertEquals("Default", IOHandler.prettifyYAMLFileName("Default.yaml"));
        } catch (Exception ignored) {
        }
    }

    @Test
    void prettifyFileNameGlassBox() {
        try {
            // test with correct format
            assertEquals(gbResult1String, IOHandler.prettifyYAMLFileName(gbTest1String));
            // test with characters instead of numeric values
            assertEquals(gbResult2String, IOHandler.prettifyYAMLFileName(gbTest2String));
            // test with inappropriate "seconds" format
            assertNotEquals(gbResult3String, IOHandler.prettifyYAMLFileName(gbTest3String));
        } catch (Exception ignored) {
        }
    }
}