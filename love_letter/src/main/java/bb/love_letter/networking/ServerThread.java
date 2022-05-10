package bb.love_letter.networking;

import bb.love_letter.game.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * This is the Thread started for each user, it waits for messages from its corresponding user and then forwards
 * the received messages to all the other logged-in Users.
 *
 * @author Bence Ament
 */
public class ServerThread extends Thread{
    public ClientList clientList = new ClientList();
    public DataInputStream dataInputStream = null;
    public DataOutputStream dataOutputStream = null;
    public Server parent;

    public ServerThread(Server parent) {
        this.parent = parent;
    }

    /**
     * Waits for and forwards messages of the User corresponding to the ServerThread instance.
     */
    public void run(){
        String json = "";
        System.out.println("Chat Server Running .....");

        while(true){
            try{
                if(clientList != null){
                    for(User user: clientList.getUsers())
                    {
                        dataInputStream = new DataInputStream(clientList.getClientSocket(user).getInputStream());
                        if(dataInputStream.available()>0)
                        {
                            // RECEIVE MESSAGE FROM USERS
                            json = dataInputStream.readUTF();
                            System.out.println(json);
                            Envelope envelope = Envelope.deserializeEnvelopeFromJson(json);
                            if (envelope.getType() == Envelope.TypeEnum.CHATMESSAGE) {
                                ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
                                String message = chatMessage.getMessage();
                                if (message.equals("bye")) {
                                    UserEvent userEvent = new UserEvent(chatMessage.getSender(), UserEvent.UserEventType.LOGOUT_CONFIRMATION);
                                    Envelope logoutNotification = new Envelope(userEvent, Envelope.TypeEnum.USEREVENT);
                                    Gson gson = new GsonBuilder().registerTypeAdapter(Envelope.class, new EnvelopeSerializer()).create();
                                    json = gson.toJson(logoutNotification);
                                    this.clientList.removeClient(user);
                                    this.parent.clientList.removeClient(user);
                                    Thread.currentThread().interrupt();
                                }
                            }
                            for (User recipient: clientList.getUsers()) {
                                dataOutputStream = new DataOutputStream(clientList.getClientSocket(recipient).getOutputStream());
                                dataOutputStream.writeUTF(json);
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