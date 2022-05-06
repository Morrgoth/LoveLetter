package bb.love_letter;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkConnection {
    private static NetworkConnection instance = null;

    User user;
    Socket socket;
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;

    private NetworkConnection(){}

    public User getUser(){
        return user;
    }
    public Socket getSocket(){
        return socket;
    }
    public ObjectInputStream getObjectInputStream(){
        return inputStream;
    }
    public ObjectOutputStream getObjectOutputStream(){
        return outputStream;
    }

    public void init(Socket socket,ObjectInputStream inputStream,ObjectOutputStream outputStream,User user) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.user = user;
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