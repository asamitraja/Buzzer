package buzzerclient;

import buzzer.Message;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author AmitSingh
 */
public class ConnectionThread extends Thread{
    
    private final Socket socket;
    private DataOutputStream dOut;
    private DataInputStream dIn;
    private boolean running=true;
    private final BuzzerClient bClient;
    private boolean socketClose;
    
    ConnectionThread(Socket socket) {
        this.socket = socket;
        setupStreams();
        bClient = BuzzerClient.getInstance();
    }
    
    void sendTeamName(String tName){
        try {
            dOut.writeUTF(tName);
        } catch (IOException ex) {}
    }
    
    public final void setupStreams(){
        try {
            dOut = new DataOutputStream(socket.getOutputStream());
            dOut.flush();
            dIn = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {}
    }
    
    @Override
    public void run(){
        while(running){
            try {
                String str;
                str = dIn.readUTF();
                switch(str){
                    case "1":
                        bClient.setCircleDisable(false);
                        break;
                    case "0":
                        bClient.setCircleDisable(true);
                        break;
                    default:
                        bClient.displayBuzzerPressedBy(str);
                        break; 
                }
            }catch (IOException ex) {
                running=false;
                if(!socketClose){
                    bClient.resetApplication();
                    (new Message()).showMessage("Connection Closed");
                }
            }
        }
        
    }
    
    void sendMessage(String msg){
        try {
            dOut.writeUTF(msg);
        } catch (IOException ex) {}
    }

    void closeSocket() {
        try {
            socketClose = true;
            socket.close();
        } catch (IOException ex) {}
    }
    
}
