package buzzer;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author AmitSingh
 */
public class Buzzer extends Application {
    
    private static Stage stage;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("frontPage.fxml"));
            Parent root = loader.load();

            stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            
            stage.setTitle("BUZZER");
            stage.setResizable(false);
            stage.show();
        }catch (IOException ex) {}
    }
    
    public static Stage getStage(){
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
