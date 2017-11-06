package buzzerserver;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author AmitSingh
 */
public class RemoveContestantsController implements Initializable {

    @FXML
    private ComboBox cBox;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button removeButton;
    
    BuzzerServer bServer;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        bServer = BuzzerServer.getInstance();
        cBox.getItems().addAll(bServer.getContestant());
        
        cancelButton.setOnAction((ActionEvent event) -> {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
        
        cBox.setOnAction((Event ActionEvent) -> {
            if(removeButton.isDisable()){
                Platform.runLater(() -> {
                    removeButton.setDisable(false);
                });
            }
        });
        
        removeButton.setOnAction((ActionEvent event)->{
            ConnectionThread cTh = (ConnectionThread) cBox.getValue();
            cTh.closeSocket();
            cTh.removeMe();
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
    }
}