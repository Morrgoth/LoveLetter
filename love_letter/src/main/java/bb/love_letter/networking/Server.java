package bb.love_letter.networking;

import bb.love_letter.game.User;

import java.io.*;
import java.net.*;

/**
 * This is the primary Server Thread, it handles the logging in of new Users and upon successful login starts
 * the separate ServerThreads for the individual users.
 *
 * @author Bence Ament
 */
public class Server {
    private final int PORT = 6868;
    public ServerSocket server;
    public Socket client = null;
    public DataOutputStream dataOutputStream;
    public DataInputStream dataInputStream;
    public ClientList clientList = new ClientList();
    public static void main(String[] args){
        Server server = new Server();
        server.doConnections();
    }

    /**
     * Waits for and handles the Login Requests of Users.
     */
    public void doConnections(){
        try{
            server = new ServerSocket(PORT);
            ServerThread messageRouterThread = new ServerThread(this);
            messageRouterThread.start();
            while(true)
            {
                client = server.accept();
                if(client != null)
                {
                    dataOutputStream = new DataOutputStream(client.getOutputStream());
                    dataInputStream = new DataInputStream(client.getInputStream());
                    String json = dataInputStream.readUTF();
                    Envelope envelope = Envelope.deserializeEnvelopeFromJson(json);
                    if (envelope.getType()== Envelope.EnvelopeType.LOGIN_REQUEST) {
                        LoginRequest loginRequest=(LoginRequest) envelope.getPayload();
                        System.out.println("loginrequest");
                        User user = loginRequest.getUser();
                        if (!clientList.containsClient(user)) {
                            ServerEvent loginConfirmationEvent = new ServerEvent("Welcome " + user.getName() + "!", ServerEvent.ServerEventType.LOGIN_CONFIRMATION);
                            Envelope loginConfirmation = new Envelope(loginConfirmationEvent, Envelope.EnvelopeType.SERVER_EVENT);
                            dataOutputStream.writeUTF(loginConfirmation.toJson()); // LOGIN_CONFIRMATION
                            clientList.addClient(user, client);
                            messageRouterThread.clientList.addClient(user,client);
                        } else {
                            ServerEvent loginErrorEvent = new ServerEvent("Username is already in use. Please choose another username.", ServerEvent.ServerEventType.NAME_ALREADY_TAKEN);
                            Envelope loginError = new Envelope(loginErrorEvent, Envelope.EnvelopeType.SERVER_EVENT);
                            dataOutputStream.writeUTF(loginError.toJson()); // LOGIN_ERROR
                            client.close();
                        }
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println("Error Occured Oops!" +  e.getMessage());
        }
    }
}
