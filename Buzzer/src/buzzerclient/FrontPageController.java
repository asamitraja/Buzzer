package buzzerclient;

import buzzer.Buzzer;
import buzzer.Message;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author AmitSingh
 */
public class FrontPageController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private TextField teamNamefield;
    
    @FXML
    private TextField ipAdrField1;
    @FXML
    private TextField ipAdrField2;
    @FXML
    private TextField ipAdrField3;
    @FXML
    private TextField ipAdrField4;
    
    @FXML
    private Button okButton;
    
    @FXML
    private MenuItem home;
    
    @FXML
    private MenuItem reset;
    
    BuzzerClient bClient;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bClient = bClient.getInstance();
        okButton.setOnAction(e -> {
            
            String tName = teamNamefield.getText();
            try{
                int ipAdr1 = Integer.parseInt(ipAdrField1.getText());
                int ipAdr2 = Integer.parseInt(ipAdrField2.getText());
                int ipAdr3 = Integer.parseInt(ipAdrField3.getText());
                int ipAdr4 = Integer.parseInt(ipAdrField4.getText());


                if(!(tName.equals("") || ipAdr1>255 || ipAdr2>255 || ipAdr3>255 || ipAdr4>255  )){
                    setOkButtonDisable(true);
                    bClient.setTeamName(tName);
                    if(bClient.Connect(ipAdr1 + "." + ipAdr2 + "." + ipAdr3 + "." + ipAdr4,tName)){
                        try {
                            bClient.startConnectionThread();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/buzzerclient/clientUI.fxml"));
                            Parent root = loader.load();
                            bClient.setControllerObject((ClientUIController)loader.getController());
                            Buzzer.getStage().setScene(new Scene(root));
                        } catch (IOException ex) {}
                    }else{
                        (new Message()).showMessage("Wrong IP or Network Connection Problem");
                        setOkButtonDisable(false);
                    }
                }else{
                    (new Message()).showMessage("Wrong IP or Invalid Team Name");
                }
            }catch(NumberFormatException ex){}     
        });
        
        home.setOnAction((ActionEvent event) -> {
            Platform.runLater(() -> {
                try {
                    Buzzer.getStage().setScene(new Scene(FXMLLoader.load(getClass().getResource("/buzzer/frontPage.fxml"))));
                } catch (IOException ex) {}
            });
        });
        
        reset.setOnAction((ActionEvent event) -> {
            Platform.runLater(() -> {
                try {
                    Buzzer.getStage().setScene(new Scene(FXMLLoader.load(getClass().getResource("/buzzerclient/frontPage.fxml"))));
                } catch (IOException ex) {}
            });
        });
        
        teamNamefield.setOnKeyPressed((KeyEvent event) -> {
            if(event.getCode()==KeyCode.TAB){
                Platform.runLater(()->{
                    ipAdrField3.requestFocus();
                });
            }
        });

        ipAdrField1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if(!ipAdrField1.getText().matches("[0-9]*")){
                    String s = ipAdrField1.getText().substring(0, ipAdrField1.getText().length()-1);
                    ipAdrField1.setText(s);
                }else if (ipAdrField1.getText().length() > 2){
                    ipAdrField2.requestFocus();
                }
            }
        });
        ipAdrField2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if(!ipAdrField2.getText().matches("[0-9]*")){
                    String s = ipAdrField2.getText().substring(0, ipAdrField2.getText().length()-1);
                    ipAdrField2.setText(s);
                }else if (ipAdrField2.getText().length() > 2){
                    ipAdrField3.requestFocus();
                }
            }
        });
        ipAdrField3.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if(!ipAdrField3.getText().matches("[0-9]*")){
                    String s = ipAdrField3.getText().substring(0, ipAdrField3.getText().length()-1);
                    ipAdrField3.setText(s);
                }else if (ipAdrField3.getText().length() > 2){
                    ipAdrField4.requestFocus();
                }
            }
        });
        ipAdrField4.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if(!ipAdrField4.getText().matches("[0-9]*") || ipAdrField4.getText().length() > 3){
                    String s = ipAdrField4.getText().substring(0, ipAdrField4.getText().length()-1);
                    ipAdrField4.setText(s);
                }else if(ipAdrField4.getText().length() > 2){
                    Platform.runLater(()->{
                        okButton.fire();
                    });
                }
            }
        });
        
        ipAdrField4.setOnKeyPressed((KeyEvent event) -> {
            if(event.getCode()==KeyCode.ENTER){
                Platform.runLater(()->{
                    okButton.fire();
                });
            }
        });
    }   
    
    private void setOkButtonDisable(boolean tof){
        Platform.runLater(()->{
            okButton.setDisable(tof);
        });
    }
}
