package bb.love_letter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

class ClientWriterThread extends Thread{
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