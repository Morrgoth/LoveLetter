package bb.love_letter.networking.client;

import bb.love_letter.game.User;

import java.io.*;
import java.net.Socket;

/**
 * This is a Singleton, a central storage location for the Network Connection information required by several classes
 * on the Client Side. After the initialization it provides access to the Socket, as well as the Input- and OutputStreams
 * so the Client can send and receive messages.
 *
 * @author Tolga Engin
 */
public class NetworkConnection {
    private static NetworkConnection instance = null;

    User user;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    NetworkConnection(){}

    /**
     * @return The logged-in User
     */
    public User getUser(){
        return user;
    }

    /**
     * @return The Socket of the logged-in User.
     */
    public Socket getSocket(){
        return socket;
    }

    /**
     * @return The InputStream of the logged-in User, all messages from the Server are received through it.
     */
    public DataInputStream getInputStream(){
        return dataInputStream;
    }

    /**
     * @return The OutputStream of the logged-in User, all messages are sent through it to the Server.
     */
    public DataOutputStream getOutputStream(){
        return dataOutputStream;
    }

    /**
     * The initialization method of this Singleton should be called only once, upon the successful login of the User.
     * @param socket The Socket of the already logged-in User.
     * @param dataInputStream The InputStream of the already logged-in User
     * @param dataOutputStream The OutputStream of the already logged-in User.
     * @param user The User information of the already logged-in User.
     */
    public void init(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream, User user) {
        this.socket = socket;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
        this.user = user;
    }

    public void reset() {
        this.socket = null;
        this.dataInputStream = null;
        this.dataOutputStream = null;
        this.user = null;
    }

    /**
     * @return The only instance of the NetworkConnection class.
     */
    public static NetworkConnection getInstance() {
        if (instance == null) {
            instance = new NetworkConnection();
            return instance;
        } else {
            return instance;
        }
    }
}