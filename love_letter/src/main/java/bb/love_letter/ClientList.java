package bb.love_letter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


/**
 * ClientList - Class represents every user with its related socket listet in a hashmap.
 * contains all Methods for the related actions for the ClientList:
 * adding/removing a new User and to print out the whole list
 *
 * @author Veronika
 */
public class ClientList {
     public HashMap<User, Socket> clientList = new HashMap<User,Socket>();


    /**
     * Method checks if the name of a new incoming User already exists by  calling the Method contains Client
     * If not the new User and its socket connection  will be added to the ClientList.
     * @param user is an Object of the User class represented by the name
     * @param socket is the connection from the Client/ User to the server
     * @return true if the User could be added to the list
     */
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

    /**
     * checks if the User's name is already in the ClientList.
     * Method is called in the Methods addName and removeName
     * @param user is one Object of the Class user represented by the name
     * @return true if a User's name already exists in the ClientList
     */
    public boolean containsClient(User user) {
        for (User u : clientList.keySet()) {
            if (u.equals(user)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to remove a Client from the ClientList.
     * calls the Method containsClient to check if the given name exists in the list and can be removed
     * if yes the socket for the User that needs to be removed will be closed and the User deleted from the Clientlist
     * @param user is an Object of the Class User represented by the name
     * @return true if the socket can be closed properly and the user can be removed successfully from the ClientList
     * @throws IOException when given an invalid input
     *
     */
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

    /**
     * when called the Method prints out the whole list of the hashmap containing the Users' name and
     * their sockets in pairs
     */

    public void printClientList(){
        for (User u: clientList.keySet() ) {
            System.out.println(u.getName());
        }
    }

    public static void main(String[] args) {
      //while(true)
        ClientList list = new ClientList();
        ArrayList<User> names = new ArrayList<>();
        //list.addUser(new User("Linda"));
        //list.addUser(new User("Alex"));
        //list.addUser(new User("Lilly"));
        //list.addUser(new User("Leon"));
        //list.addUser((new User("Linda")));

        list.printClientList();



          //printUserList(userList);
          /*Scanner sc = new Scanner(System.in);
          System.out.print("Please enter your nickname.");
          String name = sc.nextLine();
          if(userList.contains(name) == false){
          userList.add(new User(name));
          System.out.println(userList.toString());
          System.out.println("The entered nickname is acceptable.");
          break;
          }else{
           System.out.println("The entered nickname exists already. Please enter an another name:");
            }*/
         }

    }


