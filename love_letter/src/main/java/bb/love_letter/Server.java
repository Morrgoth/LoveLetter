package bb.love_letter;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    static final int PORT = 6868;
    private static UserList userList = new UserList();
    private static ArrayList<Thread> userThreads = new ArrayList<>();


    //login -Methode wurde von Veronika Heckel bearbeitet
    private static void login(Envelope envelope, Socket socket, ObjectOutputStream output, ObjectInputStream input){
        if (envelope.getType().equals("User")) {
            User user = (User)envelope.getPayload();
            if (userList.addName(user)) {
                System.out.println(user.getName() + " has entered Chatroom!");
                ServerSessionHandler serverSessionHandler = new ServerSessionHandler(socket, output, input);
                Thread thread = new Thread(serverSessionHandler);
                thread.start();
                userThreads.add(thread);
            }
        }else{
            System.out.println("Error: Unauthorized request!");
        }
    }



    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setReuseAddress(true);
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("The server is running on " + inetAddress.getHostAddress() + ":" + PORT);

            //while -Schleife wurden von Veronika Heckel bearbeitet
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    Envelope envelope = (Envelope) input.readObject();
                    login(envelope, socket, output, input);
                }catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
