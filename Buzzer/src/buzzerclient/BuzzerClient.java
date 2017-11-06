package buzzerclient;

import buzzer.Buzzer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author AmitSingh
 */
public class BuzzerClient {
    
    private String teamName;
    private Socket socket;
    private static BuzzerClient instance;
    private ConnectionThread connectionThread;
    private ClientUIController cuiController;
    private String buzzerPressedBy;
    
    public String getTeamName() {
        return teamName;
    }

    public String getBuzzerPressedBy() {
        return buzzerPressedBy;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    
    private BuzzerClient(){}
    
    public static BuzzerClient getInstance(){
        if(instance==null){
            instance = new BuzzerClient();
        }
        return instance;
    }
    
    public boolean Connect(String ipAddress,String tName) {     
        try {
            socket = new Socket();
            InetSocketAddress socketAddress = new InetSocketAddress(ipAddress, 1500);
            socket.connect(socketAddress, 1500);
            connectionThread = new ConnectionThread(socket);
            connectionThread.sendTeamName(tName);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    void startConnectionThread() {
        connectionThread.start();
    }

    void setControllerObject(ClientUIController controller) {
        this.cuiController = controller;
    }

    void setCircleDisable(boolean b) {
        cuiController.setButtonDisable(b);
    }

    void displayBuzzerPressedBy(String str) {
        this.buzzerPressedBy = str;
        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/buzzerclient/message.fxml"))));
                stage.setTitle("Message");
                stage.setResizable(false);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            }catch (IOException ex) {}
        }); 
    }

    void sendTeamName() {
        connectionThread.sendMessage(teamName);
    }

    void resetApplication() {
        Platform.runLater(() -> {
            try {
                Buzzer.getStage().setScene(new Scene(FXMLLoader.load(getClass().getResource("/buzzer/frontPage.fxml"))));
            } catch (IOException ex) {} 
        });
    }

    void closeSocket() {
        connectionThread.closeSocket();
    }
}
