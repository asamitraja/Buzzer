package buzzerserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
public class BuzzerServer{
    private int noOfContestants; 
    private ServerSocket serverSocket;
    private static BuzzerServer instance;
    private Socket socket;
    private ArrayList<ConnectionThread> contestant;
    private String buzzerPressedBy;
    private boolean connectingClients;
    private boolean buzzerPressed;
    private final Object lock = new Object();

    public void setBuzzerPressed(boolean buzzerPressed) {
        this.buzzerPressed = buzzerPressed;
    }

    public ArrayList<ConnectionThread> getContestant() {
        return contestant;
    }
    private ServerUIController suiController;

    public String getBuzzerPressedBy() {
        return buzzerPressedBy;
    }

    public void setBuzzerPressedBy(String buzzerPressedBy) {
        this.buzzerPressedBy = buzzerPressedBy;
    }

    void setNoOfContestants(int noOfContestants) {
        this.noOfContestants = noOfContestants;
    }

    int getNoOfContestants() {
        return noOfContestants;
    }
    
    private BuzzerServer(){}
    
    void openServerSocket(){
        try {
            serverSocket = new ServerSocket(1500);
        }catch (IOException ex) {}
    }
    
    void closeServerSocket(){
        try {
            serverSocket.close();
        } catch (IOException ex) {}
    }
    
    static BuzzerServer getInstance(){
        if(instance==null){
            instance = new BuzzerServer();
        }
        return instance;
    }
    
    String waitForConnection() { 
        try {
            socket = serverSocket.accept();
            ConnectionThread cth = new ConnectionThread(socket);
            contestant.add(cth);
            cth.readContestantName();
            cth.start();
            return cth.getContestantName();
        }catch (IOException ex) {}
        return null;
    }

    void disableAllBuzzers(boolean tof) {
        String str;
        if(tof)
            str="0";
        else
            str="1";
        for(int i=0;i<contestant.size();i++){
            contestant.get(i).sendMessage(str);
        }
    }
    
    void closeAllSockets(){
        for(int i=0;i<contestant.size();i++){
            contestant.get(i).closeSocket();
        }
    }

    void sendTeamName(){
        Platform.runLater(() ->{
            try{
                Stage stage = new Stage();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/buzzerserver/message.fxml"))));
                stage.setTitle("Message");
                stage.setResizable(false);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            }catch (IOException ex) {}
        });
        for(int i=0;i<noOfContestants;i++){
            contestant.get(i).sendMessage(buzzerPressedBy);
        }
        suiController.setStartDisable(false);
    }

    void setControllerObject(ServerUIController serverUIController){
        this.suiController = serverUIController;
    }

    void setContestants(){
        contestant = new ArrayList<ConnectionThread>();
    }

    void setConnectingClients(boolean b) {
        this.connectingClients = b;
    }
    
    boolean isConnectingClients(){
        return connectingClients;
    }
    
    void whenBuzzerIsPressed(String str){
        synchronized(lock){
            if(str!=null && !buzzerPressed){
                disableAllBuzzers(true);
                setBuzzerPressedBy(str);
                sendTeamName();
                buzzerPressed = true;
            }
        }
    }
    
}