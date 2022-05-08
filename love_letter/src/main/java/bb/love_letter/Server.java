package bb.love_letter;

import java.io.*;
import java.net.*;
import java.util.*;
public class Server {
    public ServerSocket server;
    public Socket client = null;
    public DataOutputStream dataOutputStream;
    public DataInputStream dataInputStream;
    public HashMap<User, Socket> clientList = new HashMap<User,Socket>(); // use/rewrite UserList
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
                    Envelope envelope = Util.deserializeJsontoEnvelope(json);
                    UserEvent userEvent = (UserEvent) envelope.getPayload();
                    if (userEvent.getUserEventType() == UserEvent.UserEventType.LOGIN_REQUEST) {
                        User user = userEvent.getUser();
                        if (!clientList.containsKey(user)) {
                            UserEvent loginConfirmationEvent = new UserEvent(user, UserEvent.UserEventType.LOGIN_CONFIRMATION);
                            Envelope loginConfirmation = new Envelope(loginConfirmationEvent, Envelope.TypeEnum.USEREVENT);
                            dataOutputStream.writeUTF(Util.getEnvelopGson().toJson(loginConfirmation)); // LOGIN_CONFIRMATION
                            clientList.put(user, client);
                            messageRouterThread.clientList.put(user,client);
                        } else {
                            UserEvent loginErrorEvent = new UserEvent(user, UserEvent.UserEventType.LOGIN_ERROR);
                            Envelope loginError = new Envelope(loginErrorEvent, Envelope.TypeEnum.USEREVENT);
                            dataOutputStream.writeUTF(Util.getEnvelopGson().toJson(loginError)); // LOGIN_ERROR
                            clientList.put(user, client);
                            messageRouterThread.clientList.put(user,client);
                        }
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println("Error Occured Oops!" +  e.getMessage());
        }
    }

    public void logoutUser(User user) throws IOException {
        this.clientList.get(user).close();
        this.clientList.remove(user);
    }
}
