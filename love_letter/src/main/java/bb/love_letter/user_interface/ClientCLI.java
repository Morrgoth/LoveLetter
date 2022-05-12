package bb.love_letter.user_interface;
import bb.love_letter.game.User;
import bb.love_letter.networking.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.*;

/**
 *
 * @author Bence Ament
 */
public class ClientCLI {

    public Socket client = null;
    public DataOutputStream dataOutputStream;
    public DataInputStream dataInputStream;
    public ClientCLI(){}

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
            client = new Socket("127.0.0.1",6868);
            dataOutputStream = new DataOutputStream(client.getOutputStream());
            dataInputStream = new DataInputStream(client.getInputStream());
            //request as a client
            User user = new User(clientName);
            LoginRequest loginRequest = new LoginRequest(user);
            String json = loginRequest.toEnvelope().toJson();
            dataOutputStream.writeUTF(json);
            ClientReaderThread read = new ClientReaderThread(dataInputStream, user);
            ClientWriterThread write = new ClientWriterThread(dataOutputStream,user);
            String response = dataInputStream.readUTF();
            Envelope envelope = Envelope.deserializeEnvelopeFromJson(response);
            if (envelope.getType() == Envelope.EnvelopeType.SERVER_EVENT) {
                ServerEvent loginResponseEvent = (ServerEvent) envelope.getPayload();
                if (loginResponseEvent.getServerEventType() == ServerEvent.ServerEventType.LOGIN_CONFIRMATION) {
                    System.out.println("Welcome "+ clientName +" !");
                    read.start();
                    write.start();
                    read.join();
                    write.join();
                } else if (loginResponseEvent.getServerEventType() == ServerEvent.ServerEventType.NAME_ALREADY_TAKEN) {
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