package bb.love_letter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.util.Pair;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class Server implements Runnable{
    static final int PORT = 6868;
    private static UserList userList = new UserList();
    private static ArrayList<Pair<ServerSessionHandler, Thread>> sessionList= new ArrayList<>();

    //broadcast - Methode von Veronika Heckel bearbeitet
    public void broadcast(Envelope envelope) throws IOException {
        //sendet nachricht an alle clients + ruft Thread notify auf
        /*
        check von wem nachricht gekommen ist + check über userList
        sende nachricht an alle anderen clients außer senderClient bzw. alle mit CLient
         */
        for(Pair pair: sessionList){
            ((ServerSessionHandler)pair.getKey()).sendMessage(envelope);
        }
    }




    //login -Methode von Veronika Heckel bearbeitet
    private void login(Envelope requestEnvelope, ServerSocket serverSocket, Socket socket, Server server) throws IOException {
        if (requestEnvelope.getType() == Envelope.TypeEnum.USEREVENT) {
            UserEvent userEvent = (UserEvent) requestEnvelope.getPayload();
            User user = userEvent.getUser();
            if (userList.addName(user)) {
                UserEvent event  = new UserEvent(user, UserEvent.UserEventType.LOGIN_CONFIRMATION);
                Envelope responseEnvelope = new Envelope(event, Envelope.TypeEnum.USEREVENT);
                Gson gson = new GsonBuilder().registerTypeAdapter(Envelope.class, new EnvelopeSerializer()).create();
                String json = gson.toJson(responseEnvelope);
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.println(json);
                System.out.println("Error: " + printWriter.checkError());
                System.out.println("Response:" + json);
                System.out.println(user.getName() + " has entered Chatroom!");
                //Start server thread
                ServerSessionHandler serverSessionHandler = new ServerSessionHandler(serverSocket, server);
                Thread thread = new Thread(serverSessionHandler);
                thread.start();
                sessionList.add(new Pair<>(serverSessionHandler, thread));
            }
        }else{
            System.out.println("Error: Unauthorized request!");
        }
    }

    //Logout von User durch Message "bye"
    private void logout(Envelope envelope){

    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setReuseAddress(true);
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("The server is running on " + inetAddress.getHostAddress() + ":" + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                InputStream input = socket.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                String json = in.readLine();
                System.out.println("Request: " + json);
                Envelope request = Util.deserializeJsontoEnvelope(json);
                login(request, serverSocket, socket, this);
                socket.close();
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
