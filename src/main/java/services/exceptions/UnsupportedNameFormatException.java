package services.exceptions;

public class UnsupportedNameFormatException extends Exception{

    public UnsupportedNameFormatException(){
        super("Unsupported file name format. Expected: <name>-dd-mm-yy--hh-mm");
    }

    public UnsupportedNameFormatException(String givenName){
        super("Unsupported file name format. Expected: <name>-dd-mm-yy--hh-mm, Given: " + givenName);
    }

}
