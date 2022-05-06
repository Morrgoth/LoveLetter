package bb.love_letter;

import java.io.*;
import java.net.Socket;

public class ServerSessionHandler implements Runnable {
    private final Socket clientSocket;
    private final DataOutput clientOutput;
    private final DataInput clientInput;

    public ServerSessionHandler(Socket socket, DataOutput output, DataInput input)
    {
        this.clientSocket = socket;
        this.clientOutput = output;
        this.clientInput = input;
    }

    @Override
    public void run() {
        System.out.println("Thread started running");
        try {
            this.clientOutput.writeUTF("Hi there, I am the server! beep boop");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
