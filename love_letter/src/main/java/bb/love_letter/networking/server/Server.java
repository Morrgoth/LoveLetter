package bb.love_letter.networking.server;

import bb.love_letter.game.User;
import bb.love_letter.networking.data.Envelope;
import bb.love_letter.networking.data.LoginRequest;
import bb.love_letter.networking.data.ServerEvent;

import java.io.*;
import java.net.*;

/**
 * This is the primary Server Thread, it handles the logging in of new Users and upon successful registers the user.
 *
 * @author Bence Ament
 */
public class Server {
    private final int PORT = 6868;
    public ServerSocket server;
    public Socket client = null;
    public DataOutputStream dataOutputStream;
    public DataInputStream dataInputStream;
    public static void main(String[] args){
        Server server = new Server();
        server.establishConnections();
    }

    /**
     * Waits for and handles the Login Requests of Users.
     */
    public void establishConnections(){
        try{
            server = new ServerSocket(PORT);
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("Server started running on " + inetAddress.getHostAddress() + ":" + PORT);
            ServerThread messageRouterThread = new ServerThread();
            messageRouterThread.start();
            while(true)
            {
                client = server.accept();
                if(client != null)
                {
                    dataOutputStream = new DataOutputStream(client.getOutputStream());
                    dataInputStream = new DataInputStream(client.getInputStream());
                    String json = dataInputStream.readUTF();
                    Envelope envelope = Envelope.fromJson(json);
                    if (envelope.getType()== Envelope.EnvelopeType.LOGIN_REQUEST) {
                        LoginRequest loginRequest = (LoginRequest) envelope.getPayload();
                        User user = loginRequest.getUser();
                        if (!messageRouterThread.getClientList().containsClient(user)) {
                            messageRouterThread.registerClient(user, client); // LOGIN
                        } else {
                            ServerEvent loginError = new ServerEvent("Username is already in use. Please " +
                                    "choose another username.",
                                    ServerEvent.ServerEventType.NAME_ALREADY_TAKEN);
                            dataOutputStream.writeUTF(loginError.toEnvelope().toJson()); // LOGIN_ERROR
                            client.close();
                        }
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println("Server Error: " +  e.getMessage());
        }
    }
}
