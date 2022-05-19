package bb.love_letter.networking.server;

import bb.love_letter.game.User;
import bb.love_letter.networking.data.ChatMessage;
import bb.love_letter.networking.data.Envelope;
import bb.love_letter.networking.data.ServerEvent;
import bb.love_letter.networking.server.ClientList;
import bb.love_letter.networking.server.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

/**
 * This is the Thread started for each user, it waits for messages from its corresponding user and then forwards
 * the received messages to all the other logged-in Users.
 *
 * @author Bence Ament
 * @author Zeynab Baiani
 */
public class ServerThread extends Thread{
    private final Socket clientSocket;
    private final Server server;

    public ServerThread(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    /**
     * Receives, processes and forwards messages from Clients.
     */
    public void run(){
        String json = "";
        System.out.println("ServerThread started");
        try{
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
            while(!clientSocket.isClosed()) {
                // Receive messages from User and forward them to the Server for execution
                json = dataInputStream.readUTF();
                System.out.println(json);
                Envelope envelope = Envelope.fromJson(json);
                if (envelope.getType() == Envelope.EnvelopeType.CHAT_MESSAGE) {
                    ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
                    Command command = new Command(chatMessage);
                    server.execute(command);
                } else {
                    System.out.println("Invalid Message received from a Client.");
                }
            }
        }
        catch(Exception e) {
            System.out.println("ServerThreadError: " + e.getMessage());
        }
        System.out.println("ServerThread stopped");
    }
}