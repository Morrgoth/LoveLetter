package bb.love_letter;

import java.io.*;
import java.net.*;
import java.util.*;
public class NewServer{
    public ServerSocket server;
    public Socket client = null;
    public DataOutputStream os;
    public DataInputStream is;
    public HashMap<String ,Socket> clientList = new HashMap<String,Socket>();
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
                    String requestedClientName = is.readUTF();
                    clientList.put(requestedClientName,client);
                    os.writeUTF("#accepted");
                    messageRouterThread.clientList.put(requestedClientName,client);
                }
            }
        }
        catch(Exception e){
            System.out.println("Error Occured Oops!" +  e.getMessage());
        }
    }
}
class MyThreadServer extends Thread{
    public HashMap<String,Socket> clientList = new HashMap<String,Socket>();
    public DataInputStream is= null;
    public DataOutputStream os=null;
    public void run(){
        String msg = "";
        int i = 0;
        System.out.println("Chat Server Running .....");
        while(true){
            try{
                if(clientList != null){
                    for(String key: clientList.keySet())
                    {
                        is= new DataInputStream(clientList.get(key).getInputStream());
                        if(is.available()>0)
                        {
                            msg=is.readUTF();
                            System.out.println(key + ": " + msg);
                            for (String recepient: clientList.keySet()) {
                                os= new DataOutputStream(clientList.get(recepient).getOutputStream());
                                os.writeUTF( key + ": " + msg);
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