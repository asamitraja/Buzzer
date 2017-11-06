package buzzerserver;

import buzzer.Buzzer;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;

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
    private TextField textField;
    
    @FXML
    private Button okButton;
    
    @FXML
    private Label ipLabel;
    
    @FXML
    private HBox hBox;
    
    @FXML
    private MenuItem reset;
    
    @FXML
    private MenuItem about;
    
    @FXML
    private MenuItem home;
    
    @FXML
    private TextArea tArea;

    BuzzerServer bServer;
    Thread th;
    boolean running;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){

        bServer = BuzzerServer.getInstance();
        
        okButton.setOnAction(e -> {
            try{
                int k = Integer.parseInt(textField.getText());
                if(k>0){
                    bServer.setNoOfContestants(k);
                    Platform.runLater(() -> {
                        try {
                            ipLabel.setText(InetAddress.getLocalHost().getHostAddress().toString());
                            okButton.setDisable(true);
                            textField.setEditable(false);
                            hBox.setVisible(true);
                            tArea.setVisible(true);
                            waitForConnection();
                        }catch(UnknownHostException ex){}
                    });
                }
            }catch(NumberFormatException ex){}
        });
        
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (textField.getText().length() > 2 || !textField.getText().matches("[0-9]*") ) {
                    String s = textField.getText().substring(0, textField.getText().length()-1);
                    textField.setText(s);
                }
            }
        });
        
        reset.setOnAction(e ->{
            try {
                if(th!=null ){
                    bServer.closeAllSockets();
                    bServer.closeServerSocket();
                    th.interrupt();
                }
                Buzzer.getStage().setScene(new Scene(FXMLLoader.load(getClass().getResource("/buzzerserver/frontPage.fxml"))));
            }catch (IOException ex) {}
        });
        
        home.setOnAction(e ->{
            try {
                if(th!=null ){
                    bServer.closeAllSockets();
                    bServer.closeServerSocket();
                    th.interrupt();
                }
                Buzzer.getStage().setScene(new Scene(FXMLLoader.load(getClass().getResource("/buzzer/frontPage.fxml"))));
            }catch(IOException ex){}
        });
        
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()==KeyCode.ENTER)
                    okButton.fire();
            }
        }); 
        Buzzer.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(th!=null ){
                    bServer.closeServerSocket();
                    th.interrupt();
                }
            }
        });
    }

    private void waitForConnection(){
        bServer.openServerSocket();
        bServer.setContestants();
        bServer.setConnectingClients(true);
        th = new Thread(){
            @Override
            public void run(){
                while(bServer.getContestant().size()<bServer.getNoOfContestants() && !th.isInterrupted()){
                    final String str;
                    str = bServer.waitForConnection();
                    if(str!=null){
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run(){
                                tArea.appendText("\nTeam " + str + " Connected");
                            }
                        });
                    }else{
                        tArea.clear();
                    }
                }
                if(!th.isInterrupted()){
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run(){
                            try{
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/buzzerserver/serverUI.fxml"));
                                Parent root = loader.load();
                                bServer.setControllerObject((ServerUIController)loader.getController());
                                Buzzer.getStage().setScene(new Scene(root));
                            }catch (IOException ex) {}
                        }
                    });
                }
                th=null;
                bServer.setConnectingClients(false);
            }
        };
        th.start();
    }
}