package bb.love_letter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread{
    public HashMap<User, Socket> clientList = new HashMap<User,Socket>();
    public DataInputStream dataInputStream = null;
    public DataOutputStream dataOutputStream = null;
    public NewServer parent;

    public ServerThread(NewServer parent) {
        this.parent = parent;
    }
    public void run(){
        String msg = "";
        System.out.println("Chat Server Running .....");

        while(true){
            try{
                if(clientList != null){
                    for(User user: clientList.keySet())
                    {
                        dataInputStream = new DataInputStream(clientList.get(user).getInputStream());
                        if(dataInputStream.available()>0)
                        {
                            // RECEIVE MESSAGE FROM USERS
                            msg = dataInputStream.readUTF();
                            System.out.println(msg);
                            Envelope envelope = Util.deserializeJsontoEnvelope(msg);
                            if (envelope.getType() == Envelope.TypeEnum.CHATMESSAGE) {
                                ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
                                String message = chatMessage.getMessage();
                                if (message.equals("bye")) {
                                    UserEvent userEvent = new UserEvent(chatMessage.getSender(), UserEvent.UserEventType.LOGOUT_CONFIRMATION);
                                    Envelope logoutNotification = new Envelope(userEvent, Envelope.TypeEnum.USEREVENT);
                                    Gson gson = new GsonBuilder().registerTypeAdapter(Envelope.class, new EnvelopeSerializer()).create();
                                    msg = gson.toJson(logoutNotification);
                                    this.clientList.remove(user);
                                    this.parent.logoutUser(user);
                                    Thread.currentThread().interrupt();
                                }
                            }
                            for (User recepient: clientList.keySet()) {
                                dataOutputStream = new DataOutputStream(clientList.get(recepient).getOutputStream());
                                dataOutputStream.writeUTF(msg);
                            }
                        }
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println("Server Recovering Initialization Failure ...");
                System.out.println("Server Up And Running ...");
            }
        }
    }
}