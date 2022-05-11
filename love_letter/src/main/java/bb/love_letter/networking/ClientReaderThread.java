package bb.love_letter.networking;

import bb.love_letter.game.User;
import javafx.event.Event;

import java.io.DataInputStream;

/**
 * This is the Thread that receives all messages from the Server and handles them as needed. It is used by
 * the CLI version of Love Letter.
 *
 * @author Bence Ament
 */
public class ClientReaderThread extends Thread{
    DataInputStream dataInputStream;
    User user;
    public ClientReaderThread(DataInputStream dataInputStream, User user){
        this.dataInputStream = dataInputStream;
        this.user = user;
    }

    /**
     * Handling of messages received from the Server
     */
    public void run()
    {
        try{
            String json=null;
            while(true){
                // RECEIVE MESSAGE FROM SERVER
                json = dataInputStream.readUTF();
                if(json != null) {
                    Envelope envelope = Envelope.deserializeEnvelopeFromJson(json);
                    if (envelope.getType() == Envelope.EnvelopeType.SERVER_EVENT) {
                        ServerEvent serverEvent = (ServerEvent) envelope.getPayload();
                        System.out.println(serverEvent.getMessage());

                    } else if (envelope.getType() == Envelope.EnvelopeType.CHAT_MESSAGE) {
                        ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
                        System.out.println(chatMessage.getSender().getName() + ": " + chatMessage.getMessage());
                    }
                }
                json = null;
            }
        }
        catch(Exception e){

        }
    }
}