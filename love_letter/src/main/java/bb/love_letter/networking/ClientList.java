package bb.love_letter.networking;
import bb.love_letter.game.User;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;
// Create an ArrayList object

//Diese Klasse wurde von Veronika Heckel bearbeitet
public class ClientList {
     public HashMap<User, Socket> clientList = new HashMap<User,Socket>();

    // adds a clientName to UserList-array
    public boolean addClient(User user, Socket socket) {
        if (!containsClient(user)) {
            clientList.put(user, socket);
            System.out.println("You can keep this name.");
            return true;
        } else {
            return false;
        }
    }

    public Set<User> getUsers() {
        return clientList.keySet();
    }

    public Socket getClientSocket(User user) {
        return clientList.get(user);
    }
    public boolean containsClient(User user) {
        for (User u : clientList.keySet()) {
            if (u.equals(user)) {
                return true;
            }
        }
        return false;
    }

    //removeName a clientName from UserList-array
    public boolean removeClient(User user) throws IOException {
        if (containsClient(user)) {
            Socket socket = clientList.get(user);
            socket.close();
            clientList.remove(user);
            return true;
        } else {
            return false;
        }
    }

    //print whole userList to client
    public void printClientList(){
        for (User u: clientList.keySet() ) {
            System.out.println(u.getName());
        }
    }
}


