package bb.love_letter;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkConnection {
    private static NetworkConnection instance = null;

    Socket socket;
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;

    private NetworkConnection (){}

    public Socket getSocket(){
        return socket;
    }
    public ObjectInputStream getObjectInputStream(){
        return inputStream;
    }
    public ObjectOutputStream getObjectOutputStream(){
        return outputStream;
    }

    public void init(Socket socket,ObjectInputStream inputStream,ObjectOutputStream outputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public static NetworkConnection getInstance() {
        if (instance == null) {
            instance = new NetworkConnection();
            return instance;
        } else {
            return instance;
        }
    }
}