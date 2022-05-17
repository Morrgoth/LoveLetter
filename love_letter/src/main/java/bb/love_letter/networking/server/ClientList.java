package bb.love_letter.networking.server;
import bb.love_letter.game.User;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 * The ClientList is used by the Server and ServerThread to keep track of the authenticated users, and store their
 * Socket connections to do server. This class is also responsible for logging out users and making sure their
 * connections are closed.
 *
 * @author Veronika Heckel
 * @author Philipp Keyzman
 * @author Bence Ament
 */
public class ClientList {
    public HashMap<User, Socket> clientList = new HashMap<User,Socket>();

    /**
     * @param user The User to be logged in, the username of this user must be unique in the list
     * @param socket The connection of the User to the Server
     * @return Returns true if the user was added successfully, false if some error occured (e.g. the username
     * is already in use)
     */
    public boolean addClient(User user, Socket socket) {
        if (!containsClient(user)) {
            clientList.put(user, socket);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return Returns a Set of the currently logged-in Users without their Socket connections
     */
    public Set<User> getUsers() {
        return clientList.keySet();
    }

    /**
     * @param user The User whose Socket connection is needed
     * @return The Socket connection of the User
     */
    public Socket getClientSocket(User user) {
        return clientList.get(user);
    }

    /**
     * @param user The User to be checked against the list
     * @return Returns true if a User with the name of the provided user parameter is already in the list, false otherwise
     */
    public boolean containsClient(User user) {
        return clientList.containsKey(user);
    }

    /**
     * @param user The User to be removed from the list, and whose connection should be closed
     * @return Returns true if the User was successfully removed, false otherwise
     * @throws IOException
     */
    public boolean removeClient(User user) throws IOException {
        // TODO: test this when ServerEvents are displayed again
        if (containsClient(user)) {
            Socket socket = getClientSocket(user);
            socket.close();
            clientList.remove(user);
            return true;
        }
        return false;
    }

    /**
     * Prints the usernames of the logged-in Users to the command line
     */
    public void printClientList(){
        for (User u: clientList.keySet() ) {
            System.out.println(u.getName());
        }
    }
}


