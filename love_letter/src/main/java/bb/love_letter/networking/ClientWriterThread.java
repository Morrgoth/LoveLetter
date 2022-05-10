package bb.love_letter.networking;

import bb.love_letter.user_interface.ChatMessage;
import bb.love_letter.game.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

/**
 * This is the Thread that sends all the messages from the Client to the Server. It waits for lines on the command line
 * and if a line is fed into it, it forwards it the Server.
 *
 * It is used by the CLI version of Love Letter.
 *
 * @author Bence Ament
 */
public class ClientWriterThread extends Thread{
    private DataOutputStream dataOutputStream;
    public BufferedReader bufferedReader;
    public User user;
    public ClientWriterThread(DataOutputStream dataOutputStream, User user){
        this.dataOutputStream = dataOutputStream;
        this.user = user;
        try{
            InputStreamReader isr = new InputStreamReader(System.in);
            bufferedReader = new BufferedReader(isr);
        }
        catch(Exception e)
        {

        }
    }

    /**
     * Upon inputting a line of text it is forwarded to the Server.
     */
    public void run()
    {
        try{
            // Notify others that the User logged in
            UserEvent loginEvent = new UserEvent(user, UserEvent.UserEventType.LOGIN_CONFIRMATION);
            Envelope loginNotification = new Envelope(loginEvent, Envelope.TypeEnum.USEREVENT);
            Gson gson = new GsonBuilder().registerTypeAdapter(Envelope.class, new EnvelopeSerializer()).create();
            dataOutputStream.writeUTF(gson.toJson(loginNotification));
            while(true){
                // SEND MESSAGE TO SERVER
                String msg = bufferedReader.readLine();
                ChatMessage chatMessage = new ChatMessage(user, msg);
                Envelope envelope = new Envelope(chatMessage, Envelope.TypeEnum.CHATMESSAGE);
                String json = gson.toJson(envelope);
                dataOutputStream.writeUTF(json);
            }
        }
        catch(Exception e){

        }
    }
}