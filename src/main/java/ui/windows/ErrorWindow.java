package ui.windows;


import javax.swing.*;
import java.util.ArrayList;

public class ErrorWindow {

    public ErrorWindow(JFrame frame, ArrayList<String> invalidFields) {
        String errorMessage = parseErrors(invalidFields);
        displayError(frame, errorMessage);
    }

    private void displayError(JFrame frame, String errorMessage) {
        JOptionPane.showMessageDialog(frame, errorMessage, "Enter Valid Fields:", JOptionPane.ERROR_MESSAGE);
    }

    private String parseErrors(ArrayList<String> invalidFields) {
        StringBuilder error = new StringBuilder();
        for (String err : invalidFields) {
            error.append(err).append("\n");
        }
        return error.toString();
    }
}
