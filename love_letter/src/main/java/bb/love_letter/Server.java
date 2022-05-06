package bb.love_letter;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    static final int PORT = 6868;
    private static UserList userList = new UserList();

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setReuseAddress(true);
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("The server is running on " + inetAddress.getHostAddress() + ":" + PORT);

            //while -Schleife + Switch - Case - Fälle wurden von Veronika Heckel bearbeitet
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    Envelope envelope = (Envelope) input.readObject();
                    ServerSessionHandler clientSock = new ServerSessionHandler(socket, output, input);
                    Thread thread = new Thread(clientSock);
                    thread.start();
                    switch (envelope.getType()) {
                        case "User":
                            boolean login = false;
                            while (!login) {
                                Scanner sc = new Scanner(System.in);
                                User user = new User(sc.nextLine());
                                if (userList.addName(user)) {
                                    login = true;
                                    System.out.println(user.getName() + " has entered Chatroom!");
                                }
                            }
                        case "ChatMessage":
                            ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
                            if(chatMessage.getMessage().equals("bye")){
                               if(userList.removeName(chatMessage.getSender())){
                                   System.out.println(chatMessage.getSender() + " leaves the Chatroom.");
                               }
                            }else{
                                  System.out.println(chatMessage.getMessage());


                            }

                    }

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
