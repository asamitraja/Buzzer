package buzzerserver;

import buzzer.Buzzer;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author AmitSingh
 */
public class ServerUIController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private Button start;
    
    @FXML
    private Button resetBuzzers;
    
    @FXML
    private Button resetApp;
    
    @FXML
    private Button removeContestant;
    
    private BuzzerServer bServer; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        bServer = BuzzerServer.getInstance();
        
        bServer = BuzzerServer.getInstance();

        start.setOnAction((ActionEvent event) -> {
            bServer.setBuzzerPressed(false);
            bServer.disableAllBuzzers(false);
            setStartDisable(true);
        });
        
        Buzzer.getStage().setOnCloseRequest((WindowEvent event) -> {
            bServer.closeAllSockets();
            bServer.closeServerSocket();
        });
        
        resetBuzzers.setOnAction((ActionEvent event) ->{
            if(start.isDisable()){
                bServer.disableAllBuzzers(true);
                setStartDisable(false);
            }
        });
        
        
        resetApp.setOnAction((ActionEvent)->{
            bServer.closeAllSockets();
            bServer.closeServerSocket();
            Platform.runLater(() -> {
                try {
                    Buzzer.getStage().setScene(new Scene(FXMLLoader.load(getClass().getResource("/buzzer/frontPage.fxml"))));
                } catch (IOException ex) {}
            });
        });
        
        removeContestant.setOnAction((ActionEvent)->{
            Platform.runLater(() ->{
                try{
                    Stage stage = new Stage();
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/buzzerserver/removeContestants.fxml"))));
                    stage.setTitle("Remove Contestant");
                    stage.setResizable(false);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.show();
                }catch (IOException ex) {}
            });
        });  
    }
    
    void setStartDisable(boolean value){
        Platform.runLater(() -> {
            start.setDisable(value);
        });
    }

}
