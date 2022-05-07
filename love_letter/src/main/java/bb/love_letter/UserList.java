package bb.love_letter;
import java.util.ArrayList;
import java.util.Scanner;
// Create an ArrayList object

//Diese Klasse wurde von Veronika Heckel bearbeitet
public class UserList {
     private ArrayList<User> userList = new ArrayList<>();

    // adds a clientName to UserList-array
    public boolean addName(User user) {
        boolean foundName = false;
        for (User u : userList) {
            if (u.getName().equals(user.getName())) {
                foundName = true;
                System.out.println("This name is already taken.\nChoose another name, please.");
                break;
            }

        }  if (!foundName){
            userList.add(user);
            System.out.println("You can keep this name.");
        }
        return !foundName;
    }

    //removeName a clientName from UserList-array
    public boolean removeName(User user) {
        boolean foundName = true;
        for(User u: userList){
            if (u.getName().equals(user.getName())){
                foundName = false;
                System.out.println("This name is not on the list");
                break;
                }
            }
            if(foundName){
            userList.remove(user);
            System.out.println(user.getName() + " " + "has left the chat.");
        }
        return foundName;
    }

    //print whole userList to client
    public void printUserList(){
        for (User u: userList ) {
            System.out.println(u.getName());
        }
    }




    public static void main(String[] args) {
      //while(true)
        UserList list = new UserList();
        ArrayList<User> names = new ArrayList<>();
        list.addName(new User("Linda"));
        list.addName(new User("Alex"));
        list.addName(new User("Lilly"));
        list.addName(new User("Leon"));
        list.addName((new User("Linda")));

        list.printUserList();



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


