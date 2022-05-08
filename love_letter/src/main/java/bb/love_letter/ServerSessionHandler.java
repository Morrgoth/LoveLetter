package bb.love_letter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerSessionHandler implements Runnable{
    private Socket clientSocket;
    private InputStream inputStream;

    private Server server;

    public ServerSessionHandler(Socket socket, InputStream inputStream, Server server)
    {
        clientSocket = socket;
        this.inputStream = inputStream;
        this.server = server;
    }

    public void sendMessage(Envelope envelope) throws IOException {
        PrintWriter printWriter = new PrintWriter(this.clientSocket.getOutputStream());
        Gson gson = new GsonBuilder().registerTypeAdapter(Envelope.class, new EnvelopeSerializer()).create();
        String json = gson.toJson(envelope);
        printWriter.println(json);
        printWriter.flush();
    }

    @Override
    public void run() {
        System.out.println("ServerSessionHandler Thread started running");
        try {
            UserEvent event  = new UserEvent(new User("Hello"), UserEvent.UserEventType.LOGIN_CONFIRMATION);
            Envelope responseEnvelope = new Envelope(event, Envelope.TypeEnum.USEREVENT);
            PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());
            printWriter.println(responseEnvelope);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(this.inputStream));
                String json = null;
                while (in.ready()) {
                    //json = in.readLine();
                    int c = in.read();
                    System.out.println(c);
                    System.out.println("ready");
                }
                if (json != null) {
                    System.out.println(json);
                }
                Envelope envelope = Util.jsonToEnvelope(json);
                server.broadcast(envelope);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


