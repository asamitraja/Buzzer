package buzzerclient;

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
public class MessageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label label;
    
    @FXML
    private Button okButton;
    
    private BuzzerClient bClient;
    
    private Stage stage;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        bClient = BuzzerClient.getInstance();
        
        String str = bClient.getBuzzerPressedBy();
        
        label.setText(str);
        
        Thread th = new Thread(){
            @Override
            public void run(){
                long start = System.currentTimeMillis();
                long end = start + 10 * 1000;
                while(start < end && !this.isInterrupted()){
                    start = System.currentTimeMillis();
                }
                Platform.runLater(()->{
                    okButton.fire();
                });
            }
        };
        th.start();
        
        okButton.setOnAction((ActionEvent event) -> {
            th.interrupt();
            Node source = (Node) event.getSource();
            stage =  (Stage) source.getScene().getWindow();
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
