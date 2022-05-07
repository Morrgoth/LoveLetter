package bb.love_letter;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Server implements Runnable{
    static final int PORT = 6868;
    private static UserList userList = new UserList();
    private static ArrayList<Thread> userThreads = new ArrayList<>();

    //broadcast - Methode von Veronika Heckel bearbeitet
    private void broadcast(Envelope envelope){
        //sendet nachricht an alle clients + ruft Thread notify auf
        /*
        check von wem nachricht gekommen ist + check über userList
        sende nachricht an alle anderen clients außer senderClient bzw. alle mit CLient
         */
        for(Thread thread: userThreads){
            thread.sendMessage(envelope);
        }
    }




    //login -Methode von Veronika Heckel bearbeitet
    private void login(Envelope envelope, Socket socket, ObjectOutputStream output, ObjectInputStream input, Server server){
        if (envelope.getType().equals("USER")) {
            User user = (User)envelope.getPayload();
            if (userList.addName(user)) {
                System.out.println(user.getName() + " has entered Chatroom!");
                ServerSessionHandler serverSessionHandler = new ServerSessionHandler(socket, output, input, server);
                Thread thread = new Thread(serverSessionHandler);
                thread.start();
                userThreads.add(thread);
            }
        }else{
            System.out.println("Error: Unauthorized request!");
        }
    }



    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setReuseAddress(true);
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("The server is running on " + inetAddress.getHostAddress() + ":" + PORT);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    Envelope envelope = (Envelope) input.readObject();
                    login(envelope, socket, output, input, this);
                }catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
