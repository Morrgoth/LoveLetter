package bb.love_letter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.*;
import java.util.*;
public class NewServer {
    public ServerSocket server;
    public Socket client = null;
    public DataOutputStream dataOutputStream;
    public DataInputStream dataInputStream;
    public HashMap<User, Socket> clientList = new HashMap<User,Socket>();
    public static void main(String[] args){
        NewServer newServer = new NewServer();
        newServer.doConnections();
    }
    public void doConnections(){
        try{
            server = new ServerSocket(6556);
            MyThreadServer messageRouterThread = new MyThreadServer(this);
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
                    User user = userEvent.getUser();
                    clientList.put(user, client);
                    dataOutputStream.writeUTF("#accepted"); // LOGIN_CONFIRMATION
                    messageRouterThread.clientList.put(user,client);
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
class MyThreadServer extends Thread{
    public HashMap<User,Socket> clientList = new HashMap<User,Socket>();
    public DataInputStream dataInputStream = null;
    public DataOutputStream dataOutputStream = null;
    public NewServer parent;

    public MyThreadServer(NewServer parent) {
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
            //goes through all clientList and reads the UTF and Writes to that Client
        }
    }
}