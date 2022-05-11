package bb.love_letter.networking;

import bb.love_letter.game.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This is the Thread started for each user, it waits for messages from its corresponding user and then forwards
 * the received messages to all the other logged-in Users.
 *
 * @author Bence Ament
 * @author Zeynab Baiani
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
                            if (envelope.getType() == Envelope.EnvelopeType.CHAT_MESSAGE) {
                                ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
                                Command command = new Command(chatMessage);
                                execute(command);
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
    private void broadcast(Envelope envelope, ArrayList<User> whitelist, ArrayList<User> blacklist) throws IOException {
        if (whitelist != null) {
            for (User recipient: clientList.getUsers()) {
                if (whitelist.contains(recipient)){
                    dataOutputStream = new DataOutputStream(clientList.getClientSocket(recipient).getOutputStream());
                    dataOutputStream.writeUTF(envelope.toJson());
                }
            }
        } else if (blacklist != null) {
            for (User recipient: clientList.getUsers()) {
                if (!blacklist.contains(recipient)) {
                    dataOutputStream = new DataOutputStream(clientList.getClientSocket(recipient).getOutputStream());
                    dataOutputStream.writeUTF(envelope.toJson());
                }
            }
        }
        else {
            for (User recipient: clientList.getUsers()) {
                dataOutputStream = new DataOutputStream(clientList.getClientSocket(recipient).getOutputStream());
                dataOutputStream.writeUTF(envelope.toJson());
            }
        }
    }

    private void execute(Command command) throws IOException {
        if (command.getCommandType()== Command.CommandType.LOGOUT_COMMAND){
            ServerEvent logOutConfirmation = new ServerEvent("You have successfully logged out.", ServerEvent.ServerEventType.LOGOUT_CONFIRMATION);
            ServerEvent userLeftNotification = new ServerEvent(command.getUser().getName() + " left the room.", ServerEvent.ServerEventType.PLAYER_LEFT_NOTIFICATION);

            Envelope logoutNotification = new Envelope(logOutConfirmation, Envelope.EnvelopeType.SERVER_EVENT);
            Envelope userLeftNotificationEnvelope = new Envelope(userLeftNotification, Envelope.EnvelopeType.SERVER_EVENT);
            broadcast(logoutNotification, new ArrayList<User>((Collection) command.getUser()), null);
            broadcast(userLeftNotificationEnvelope, null, new ArrayList<User>((Collection) command.getUser()));

            this.clientList.removeClient(command.getUser());
            this.parent.clientList.removeClient(command.getUser());
            Thread.currentThread().interrupt();

        } else if (command.getCommandType()== Command.CommandType.PRIVATE_MESSAHECOMMAND) {
            Envelope privateMessageEnvelope = new Envelope(command.getPrivateMessage(), Envelope.EnvelopeType.CHAT_MESSAGE);
            broadcast(privateMessageEnvelope, new ArrayList<>((Collection) command.getUser()), null);

        }  else if (command.getCommandType()== Command.CommandType.EMPTY_COMMAND) {
            Envelope messageEnvelope = new Envelope(command.getChatMessage(), Envelope.EnvelopeType.CHAT_MESSAGE);
            broadcast(messageEnvelope, null, null);

        }


    }
}