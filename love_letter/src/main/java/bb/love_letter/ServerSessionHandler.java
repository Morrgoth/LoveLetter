package bb.love_letter;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerSessionHandler implements Runnable{
    private Socket clientSocket;
    private ObjectOutputStream clientOutput;
    private ObjectInputStream clientInput;
    private Server server;

    public ServerSessionHandler(Socket socket, ObjectOutputStream output, ObjectInputStream input, Server server)
    {
        clientSocket = socket;
        clientOutput = output;
        clientInput = input;
        this.server = server;
    }

    public void sendMessage(Envelope envelope) throws IOException {
        clientOutput.writeObject(envelope);
    }

    @Override
    public void run() {
        System.out.println("Thread started running");
        try {
            while(true){
                Envelope envelope = (Envelope) clientInput.readObject();
                server.broadcast(envelope);
            }
        } catch (EOFException e) {
            System.out.println("BS");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}


