package buzzer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author user
 */
public class FrontPageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private Button coordinator;
    
    @FXML
    private Button contestant;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        coordinator.setOnAction(e -> {
            try {
                Buzzer.getStage().setScene(new Scene(FXMLLoader.load(getClass().getResource("/buzzerserver/frontPage.fxml"))));
            } catch (IOException ex) {
                //Logger.getLogger(FrontPageController.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        
        });
        
        contestant.setOnAction(e -> {
            try {
                Buzzer.getStage().setScene(new Scene(FXMLLoader.load(getClass().getResource("/buzzerclient/frontPage.fxml"))));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        
        });
        
    }  
    
    
    
}
