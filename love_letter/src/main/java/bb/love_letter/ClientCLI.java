package bb.love_letter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.*;

public class ClientCLI {

    public Socket client = null;
    public DataOutputStream os;
    public DataInputStream is;
    public ClientCLI(){

    }

    public static void main(String[] args)throws IOException{

        ClientCLI a = new ClientCLI();
        a.doConnections();

    }
    public void doConnections()throws IOException{
        try{
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            System.out.print("Enter Your Name: ");
            String clientName = br.readLine();
            client = new Socket("127.0.0.1",6556);
            os = new DataOutputStream(client.getOutputStream());
            is = new DataInputStream(client.getInputStream());
            //request as a client
            User user = new User(clientName);
            UserEvent userEvent = new UserEvent(user, UserEvent.UserEventType.LOGIN_REQUEST);
            Envelope request = new Envelope(userEvent, Envelope.TypeEnum.USEREVENT);
            Gson gson = new GsonBuilder().registerTypeAdapter(Envelope.class, new EnvelopeSerializer()).create();
            String json = gson.toJson(request);
            os.writeUTF(json);
            ClientReaderThread read = new ClientReaderThread(is, user);
            ClientWriterThread write = new ClientWriterThread(os,user);
            String response = is.readUTF();
            Envelope envelope = Envelope.deserializeEnvelopeFromJson(response);
            if (envelope.getType() == Envelope.TypeEnum.USEREVENT) {
                UserEvent loginResponseEvent = (UserEvent) envelope.getPayload();
                if (loginResponseEvent.getUserEventType() == UserEvent.UserEventType.LOGIN_CONFIRMATION) {
                    System.out.println("Welcome "+ clientName +" !");
                    read.start();
                    write.start();
                    read.join();
                    write.join();
                } else if (loginResponseEvent.getUserEventType() == UserEvent.UserEventType.LOGIN_ERROR) {
                    System.out.println("Error: The username " + user.getName() + " is already taken!");
                }
            } else {
                System.out.println("Error: Could Not Connect To Server!");
            }
        }
        catch(Exception e){
            System.out.println("Error Occured Oops!");
        }
    }
}