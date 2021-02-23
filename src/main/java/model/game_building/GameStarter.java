package model.game_building;

import org.apache.log4j.BasicConfigurator;
import ui.windows.BuildingWindow;

public class GameStarter {
    public static void main(String[] args) {
        // Setup all loggers to print to the console.
        BasicConfigurator.configure();
        // note: running mode starts as a result of finishing building mode
        BuildingWindow buildingWindow = new BuildingWindow("Game Build");
        buildingWindow.start();
    }
}
