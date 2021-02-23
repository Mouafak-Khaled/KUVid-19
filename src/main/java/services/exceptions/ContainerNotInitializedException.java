package services.exceptions;

public class ContainerNotInitializedException extends Exception{

    public ContainerNotInitializedException(){
        super("Projectile container is null or not initialized");
    }

    public ContainerNotInitializedException(String message) {
        super(message);
    }
}
