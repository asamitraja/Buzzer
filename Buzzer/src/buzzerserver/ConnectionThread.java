package buzzerserver;

import buzzer.Buzzer;
import buzzer.Message;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 *
 * @author AmitSingh
 */
public class ConnectionThread extends Thread{
    
    private final Socket socket;
    private DataOutputStream dOut;
    private DataInputStream dIn;
    private String contestantName;
    private final BuzzerServer bServer;
    private boolean running;
    private boolean socketClose;
    
    ConnectionThread(Socket socket) {
        this.socket = socket;
        setupStreams();
        bServer = BuzzerServer.getInstance();
    }
    
    private void setupStreams(){
        try {
            dOut = new DataOutputStream(socket.getOutputStream());
            dOut.flush();
            dIn = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {}
    }
    
    @Override
    public void run(){
        running=true;
        while(running){
            try {
                String str = dIn.readUTF();
                bServer.whenBuzzerIsPressed(str);
            }catch(Exception ex){
                running=false;
                if(!socketClose){
                    removeMe();
                }
            }  
        }
    }
    
    void readContestantName(){
        try {
            contestantName = dIn.readUTF();
        } catch (IOException ex) {}
    }

    String getContestantName() {
        return contestantName;
    }

    void sendMessage(String msg) {
        try {
            dOut.writeUTF(msg);
        } catch (IOException ex) {}
    }
    
    void closeSocket(){
        try {
            socketClose = true;
            socket.close();
        } catch (IOException ex) {}
    }
    
    @Override
    public String toString(){
        return contestantName;
    }

    public void removeMe() {
        bServer.getContestant().remove(this);
        if(!bServer.isConnectingClients()){
            bServer.setNoOfContestants(bServer.getNoOfContestants()-1);
            if(bServer.getNoOfContestants()==0){
                bServer.closeServerSocket();
                Platform.runLater(() -> {
                    try {
                        Buzzer.getStage().setScene(new Scene(FXMLLoader.load(getClass().getResource("/buzzer/frontPage.fxml"))));
                    }catch(IOException e) {}
                });
            }
        }
        (new Message()).showMessage("Team " + contestantName + " Disconnected");
    }
}
