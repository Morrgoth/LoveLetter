package bb.love_letter;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerSessionHandler implements Runnable{
    private Socket clientSocket;
    private ObjectOutputStream clientOutput;
    private ObjectInputStream clientInput;

    public ServerSessionHandler(Socket socket, ObjectOutputStream output, ObjectInputStream input)
    {
        clientSocket = socket;
        clientOutput = output;
        clientInput = input;
    }

    @Override
    public void run() {
        System.out.println("Thread started running");
        try {
            clientOutput.writeUTF("Hi there, I am the server! beep boop");
            clientOutput.flush();


            //Receive message from client
            clientInput = (ObjectInputStream) clientSocket.getInputStream();
            byte[] readObject = new byte[64];
            clientInput.read(readObject);
            String msgFromClient = new String(readObject);
            System.out.println("The message from client: "+ msgFromClient);

            //Deal with the message from client
            clientOutput = (ObjectOutputStream) clientSocket.getOutputStream();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
