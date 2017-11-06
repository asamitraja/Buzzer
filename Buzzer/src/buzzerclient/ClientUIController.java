package buzzerclient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import buzzer.Buzzer;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author AmitSingh
 */
public class ClientUIController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button button;
    
    @FXML
    private Label label;
    
    @FXML
    private Label teamName;
    
    BuzzerClient bClient;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        bClient = BuzzerClient.getInstance();
        setButtonDisable(true);
        
        teamName.setText(bClient.getTeamName());
        
        button.setOnAction((ActionEvent event) -> {
            setButtonDisable(true);
            bClient.sendTeamName();
        });
        
        Buzzer.getStage().setOnCloseRequest((WindowEvent event) -> {
            bClient.closeSocket();
        });  
    }
    public void setButtonDisable(boolean value){
        Platform.runLater(() -> {
            button.setDisable(value);
            if(value){
                label.setText("Waiting...");
            }else{
                label.setText("Click the CIRCLE or Press SPACE BAR");
            }
        });
    }  
}