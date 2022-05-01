package bb.love_letter;

import java.util.ArrayList;


public class ListClientNames {

    //new list to store clients' names
    ArrayList<String> names = new ArrayList<>();

    /**
     * method adds a new name to the list
     * @param clientName is the input from the User for the chosen name
     */
    public void addName(String clientName) {
        if (names.contains(clientName)) {
            System.out.println("This name is already taken.\nChoose another name, please.");
        } else {
            names.add(clientName);
            //test purpose
            System.out.println("You can keep this name.");
        }
    }


    /**
     * method to remove the names from the list - checks also if it is possible
     * @param clientName is the input from the User
     */
    public void removeName(String clientName) {
        if (names.contains(clientName)) {
            System.out.println(clientName + " " + "has left the chat.");
            names.remove(clientName);
        }
        //test purpose
        else {
            System.out.println("This name is not on the list");
        }
    }

    // method prints out the whole List to the User
    public void printNameList(){
        for (String name: names) {
            System.out.println(name);
        }
    }
}


