package bb.love_letter.networking;

import bb.love_letter.networking.data.Envelope;
import bb.love_letter.user_interface.ChatController;
import javafx.application.Platform;

/**
 * This is the Thread that receives all messages from the Server and handles them as needed. It is used by
 * the GUI version of Love Letter.
 *
 * @author Bence Ament
 * @author Tolga Engin
 */
public class ClientReaderThreadUI extends Thread{
    ChatController chatController;
    public ClientReaderThreadUI(ChatController chatController){
        this.chatController = chatController;
    }
    /**
     * Handling of messages received from the Server
     */
    public void run()
    {
        System.out.println("ClientReaderThreadUI started running");
        try{
            String json=null;
            while(true){
                // RECEIVE MESSAGE FROM SERVER
                json = NetworkConnection.getInstance().getInputStream().readUTF();
                if(json != null) {
                    Envelope envelope = Envelope.fromJson(json);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            chatController.addChatMessage(envelope);
                        }
                    });
                }
                json = null;
            }
        }
        catch(Exception e){
            // Error Message should be added here
        }
    }
}