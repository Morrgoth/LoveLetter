package bb.love_letter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.*;
import java.net.*;

public class ClientSessionHandler implements Runnable {


    public ClientSessionHandler() {
    }

    @Override
    public void run() {
        System.out.println("Thread started running");
        while (true) {
            try {
                //Receive message from server
                NetworkConnection.getInstance().getObjectInputStream().readObject() = (ObjectInputStream) networkConnection.socket.getInputStream();
                byte[] readObject = new byte[64];
                serverInput.read(readObject);
                String msgFromClient = new String(readObject);
                System.out.println("The message from client: " + msgFromClient);

                try (
                        Socket clientSocket = serverSocket.accept();
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}