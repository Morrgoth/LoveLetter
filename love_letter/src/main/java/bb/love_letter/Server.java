package bb.love_letter;

import java.io.*;
import java.net.*;

public class Server {
    static final int PORT = 6868;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setReuseAddress(true);
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("The server is running on " + inetAddress.getHostAddress() + ":" + PORT);
            while(true) {
                try {
                    Socket socket = serverSocket.accept();
                    DataOutput output = new DataOutputStream(socket.getOutputStream());
                    DataInput input = new DataInputStream(socket.getInputStream());
                    ClientHandler clientSock = new ClientHandler(socket, output, input);
                    Thread thread = new Thread(clientSock);
                    thread.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}