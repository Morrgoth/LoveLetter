package bb.love_letter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkConnection {
    private static NetworkConnection instance = null;

    User user;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    NetworkConnection(){}

    public User getUser(){
        return user;
    }
    public Socket getSocket(){
        return socket;
    }
    public DataInputStream getInputStream(){
        return dataInputStream;
    }
    public DataOutputStream getOutputStream(){
        return dataOutputStream;
    }

    public void init(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream, User user) {
        this.socket = socket;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
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