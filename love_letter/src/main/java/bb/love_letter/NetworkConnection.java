package bb.love_letter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkConnection {
    private static NetworkConnection instance = null;

    User user;
    Socket socket;
    InputStream inputStream;
    OutputStreamWriter outputStream;
    String ip;
    int port;
    NetworkConnection(){}

    public User getUser(){
        return user;
    }
    public Socket getSocket(){
        return socket;
    }
    public InputStream getInputStream(){
        return inputStream;
    }
    public OutputStreamWriter getOutputStream(){
        return outputStream;
    }

    public void init(String ip, int port, User user) {
        this.ip = ip;
        this.port = port;
        this.user = user;
    }
    public String getIp(){
        return this.ip;
    }

    public int getPort() {
        return port;
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