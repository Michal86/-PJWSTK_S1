import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.ModelManager;
import view.ViewManager;
import controller.Controller;
/**
 * User: Michał Radzewicz
 * Project: (N Slider) "Puzzle Game" - it's my coursework using JavaFX for GUI.
 * User is allowed to:
 *  -> Add his Profile (Nick) to keep a track of his games.
 *  -> Pick a map to solve
 *  -> Choose difficulty from 3 to 5 (Meaning i.e.: Picking 3 program creates 3 by 3 square map)
 *  -> Check saved records [I used JSON file to save and load previous rankings]
 *
 *  Additionally:
 *      1. I implemented helper button [A* path finding algorithm] -> It still needs updates/rework
 *         as it's working slow for me (even with the Manhattan distance heuristic), and only found solutions for 3x3 puzzle game.
 *      2. TO_DO: add save/load Players profiles to FilesManager
 */

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            //--- ADD MODEL ---
            ModelManager modelManager = new ModelManager();
            //--- CREATE VIEW ---
            ViewManager viewManager = new ViewManager();
            primaryStage = viewManager.getMainStage();
            primaryStage.setTitle("Puzzle Game");
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setResizable(false);

            //--- ADD CONTROLLER ---
            Controller myController = new Controller(viewManager, modelManager);
            viewManager.setController(myController);
            //--- SHOW ---
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
