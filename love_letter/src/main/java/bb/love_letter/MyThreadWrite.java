package bb.love_letter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

class MyThreadWrite extends Thread{
    private DataOutputStream os;
    public BufferedReader br;
    public User user;
    public MyThreadWrite(DataOutputStream o,User user){
        os=o;
        this.user = user;
        try{
            InputStreamReader isr = new InputStreamReader(System.in);
            br = new BufferedReader(isr);
        }
        catch(Exception e)
        {

        }
    }
    public void run()
    {
        try{
            UserEvent loginEvent = new UserEvent(user, UserEvent.UserEventType.LOGIN_CONFIRMATION);
            Envelope loginNotification = new Envelope(loginEvent, Envelope.TypeEnum.USEREVENT);
            Gson gson = new GsonBuilder().registerTypeAdapter(Envelope.class, new EnvelopeSerializer()).create();
            os.writeUTF(gson.toJson(loginNotification));
            while(true){
                // SEND MESSAGE TO SERVER
                String msg = br.readLine();
                ChatMessage chatMessage = new ChatMessage(user, msg);
                Envelope envelope = new Envelope(chatMessage, Envelope.TypeEnum.CHATMESSAGE);
                String json = gson.toJson(envelope);
                os.writeUTF(json);
            }
        }
        catch(Exception e){

        }
    }
}