package bb.love_letter.networking;

import bb.love_letter.game.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
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

    public ServerThread() {}

    /**
     * Receives, processes and forwards messages from Clients.
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
                            } else {
                                System.out.println("Invalid Message received from a Client.");
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

    public synchronized void registerClient(User user, Socket socket) throws IOException {
        clientList.addClient(user, socket);
        ServerEvent loginConfirmation = new ServerEvent("Welcome " + user.getName() + "!",
                ServerEvent.ServerEventType.LOGIN_CONFIRMATION);
        ServerEvent newUserNotification = new ServerEvent(user.getName() + " joined the room!",
                ServerEvent.ServerEventType.NEW_PLAYER_NOTIFICATION);
        broadcast(loginConfirmation.toEnvelope(), new User[] {user}, null);
        broadcast(newUserNotification.toEnvelope(), null, new User[] {user});
    }

    public synchronized ClientList getClientList() {
        return this.clientList;
    }

    private void broadcast(Envelope envelope, User[] whitelist, User[] blacklist) throws IOException {
        if (whitelist != null) {
            for (User recipient: clientList.getUsers()) {
                if (Arrays.asList(whitelist).contains(recipient)){
                    dataOutputStream = new DataOutputStream(clientList.getClientSocket(recipient).getOutputStream());
                    dataOutputStream.writeUTF(envelope.toJson());
                }
            }
        } else if (blacklist != null) {
            for (User recipient: clientList.getUsers()) {
                if (!Arrays.asList(blacklist).contains(recipient)) {
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
            ServerEvent logOutConfirmation = new ServerEvent("You have successfully logged out.",
                    ServerEvent.ServerEventType.LOGOUT_CONFIRMATION);
            ServerEvent userLeftNotification = new ServerEvent(command.getUser().getName() +
                    " left the room.", ServerEvent.ServerEventType.PLAYER_LEFT_NOTIFICATION);
            broadcast(logOutConfirmation.toEnvelope(), new User[] {command.getUser()}, null);
            broadcast(userLeftNotification.toEnvelope(), null, new User[] {command.getUser()});
            this.clientList.removeClient(command.getUser());

        } else if (command.getCommandType()== Command.CommandType.PRIVATE_MESSAGE_COMMAND) {
            Envelope privateMessageEnvelope = new Envelope(command.getPrivateMessage(),
                    Envelope.EnvelopeType.CHAT_MESSAGE);
            broadcast(privateMessageEnvelope, new User[] {command.getUser()}, null);

        }  else if (command.getCommandType()== Command.CommandType.EMPTY_COMMAND) {
            Envelope messageEnvelope = new Envelope(command.getChatMessage(), Envelope.EnvelopeType.CHAT_MESSAGE);
            broadcast(messageEnvelope, null, null);

        }


    }
}