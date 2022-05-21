package bb.love_letter.networking.client;

import bb.love_letter.networking.data.Envelope;
import bb.love_letter.user_interface.controller.ChatController;
import bb.love_letter.user_interface.model.ChatModel;
import javafx.application.Platform;

/**
 * This is the Thread that receives all messages from the Server and handles them as needed. It is used by
 * the GUI version of Love Letter.
 *
 * @author Bence Ament
 * @author Tolga Engin
 */
public class ClientReaderThread extends Thread{
    ChatModel chatModel;
    public ClientReaderThread(ChatModel chatModel){
        this.chatModel = chatModel;
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
                            chatModel.processMessage(envelope);
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