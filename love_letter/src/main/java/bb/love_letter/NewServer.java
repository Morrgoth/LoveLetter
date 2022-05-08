package bb.love_letter;

import java.io.*;
import java.net.*;
import java.util.*;
public class NewServer {
    public ServerSocket server;
    public Socket client = null;
    public DataOutputStream os;
    public DataInputStream is;
    public HashMap<User, Socket> clientList = new HashMap<User,Socket>();
    public static void main(String[] args){
        NewServer a = new NewServer();
        a.doConnections();
    }
    public void doConnections(){
        try{
            server = new ServerSocket(6556);
            MyThreadServer messageRouterThread = new MyThreadServer();
            messageRouterThread.start();
            while(true)
            {
                client = server.accept();
                if(client != null)
                {
                    os = new DataOutputStream(client.getOutputStream());
                    is = new DataInputStream(client.getInputStream());
                    String json = is.readUTF();
                    Envelope envelope = Util.deserializeJsontoEnvelope(json);
                    UserEvent userEvent = (UserEvent) envelope.getPayload();
                    User user = userEvent.getUser();
                    clientList.put(user, client);
                    os.writeUTF("#accepted");
                    messageRouterThread.clientList.put(user,client);
                }
            }
        }
        catch(Exception e){
            System.out.println("Error Occured Oops!" +  e.getMessage());
        }
    }
}
class MyThreadServer extends Thread{
    public HashMap<User,Socket> clientList = new HashMap<User,Socket>();
    public DataInputStream is= null;
    public DataOutputStream os=null;
    public void run(){
        String msg = "";
        int i = 0;
        System.out.println("Chat Server Running .....");
        while(true){
            try{
                if(clientList != null){
                    for(User user: clientList.keySet())
                    {
                        is= new DataInputStream(clientList.get(user).getInputStream());
                        if(is.available()>0)
                        {
                            // RECEIVE MESSAGE FROM USERS
                            msg=is.readUTF();
                            System.out.println(msg);
                            // Code for logging in upon receiving "bye"
                            //Envelope envelope = Util.deserializeJsontoEnvelope(msg);
                            for (User recepient: clientList.keySet()) {
                                os= new DataOutputStream(clientList.get(recepient).getOutputStream());
                                os.writeUTF(msg);
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