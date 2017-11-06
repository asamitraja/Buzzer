package buzzer;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author AmitSingh
 */
public class DisplayMessageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button okButton;
    
    @FXML
    private Label label;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        label.setText(Message.getMsg());
        
        okButton.setOnAction((ActionEvent event) -> {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
        
        okButton.setOnKeyPressed((KeyEvent event) -> {
            if(event.getCode()==KeyCode.ENTER){
                Platform.runLater(()->{
                    okButton.fire();
                });
            }
        });
        
    }    
    
}
