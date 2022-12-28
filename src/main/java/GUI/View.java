package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class View extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Initialize stage
        stage.setTitle("MIPS Emulator");
        stage.setMaximized(true);

        // Initialize layout
        BorderPane layout = new BorderPane();
        ViewMenu viewMenu = new ViewMenu();
        layout.setTop(viewMenu.render());
        Accelerometer accelerometer = new Accelerometer();
        layout.setBottom(accelerometer.render());

        // Initialize scene
        Scene scene = new Scene(layout);
        stage.setScene(scene);

        // Show stage
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}