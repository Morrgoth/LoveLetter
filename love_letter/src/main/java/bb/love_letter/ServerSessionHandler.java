package bb.love_letter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerSessionHandler implements Runnable{
    private ServerSocket clientSocket;

    private Server server;

    public ServerSessionHandler(ServerSocket socket, Server server)
    {
        clientSocket = socket;
        this.server = server;
    }

    public void sendMessage(Envelope envelope) throws IOException {
        Socket socket = clientSocket.accept();
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        Gson gson = new GsonBuilder().registerTypeAdapter(Envelope.class, new EnvelopeSerializer()).create();
        String json = gson.toJson(envelope);
        printWriter.println(json);
    }

    @Override
    public void run() {
        System.out.println("ServerSessionHandler Thread started running");
        while (true) {
            try (Socket socket = this.clientSocket.accept()){
                InputStream input = socket.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                String json = in.readLine();
                Envelope envelope = Util.jsonToEnvelope(json);
                server.broadcast(envelope);
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


