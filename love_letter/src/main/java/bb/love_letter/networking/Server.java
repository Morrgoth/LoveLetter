package bb.love_letter.networking;

import bb.love_letter.game.User;

import java.io.*;
import java.net.*;

public class Server {
    public ServerSocket server;
    public Socket client = null;
    public DataOutputStream dataOutputStream;
    public DataInputStream dataInputStream;
    public ClientList clientList = new ClientList();
    public static void main(String[] args){
        Server server = new Server();
        server.doConnections();
    }
    public void doConnections(){
        try{
            server = new ServerSocket(6556);
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
                    UserEvent userEvent = (UserEvent) envelope.getPayload();
                    if (userEvent.getUserEventType() == UserEvent.UserEventType.LOGIN_REQUEST) {
                        User user = userEvent.getUser();
                        if (!clientList.containsClient(user)) {
                            UserEvent loginConfirmationEvent = new UserEvent(user, UserEvent.UserEventType.LOGIN_CONFIRMATION);
                            Envelope loginConfirmation = new Envelope(loginConfirmationEvent, Envelope.TypeEnum.USEREVENT);
                            dataOutputStream.writeUTF(loginConfirmation.toJson()); // LOGIN_CONFIRMATION
                            clientList.addClient(user, client);
                            messageRouterThread.clientList.addClient(user,client);
                        } else {
                            UserEvent loginErrorEvent = new UserEvent(user, UserEvent.UserEventType.LOGIN_ERROR);
                            Envelope loginError = new Envelope(loginErrorEvent, Envelope.TypeEnum.USEREVENT);
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
