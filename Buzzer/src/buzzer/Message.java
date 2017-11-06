/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buzzer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class Message {
    
    private static String msg;

    static String getMsg() {
        return msg;
    }

    private void setMsg(String msg) {
        Message.msg = msg;
    }
    
    public void showMessage(String message){
        setMsg(message);
        Platform.runLater(() ->{
            try {
                Stage stg = new Stage();
                stg.setScene(new Scene(FXMLLoader.load(getClass().getResource("/buzzer/displayMessage.fxml"))));
                stg.setTitle("Message");
                stg.setResizable(false);
                stg.initModality(Modality.APPLICATION_MODAL);
                stg.show();
            } catch (IOException ex) {
                Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
}
