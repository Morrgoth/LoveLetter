package bb.love_letter;
import java.util.ArrayList;

// Create an ArrayList object
public class NameList {
    ArrayList<String> nameList = new ArrayList<String>();

    // adds a clientName to NameList-array
    public void addName(String clientName){

        if(nameList.contains(clientName)) {

            System.out.println("This name is already taken.\nChoose another name, please.");
            break;
        }
        else {
            nameList.add(clientName);
            //test purpose
            System.out.println("You can keep this name.");
        }
    }

    //removeName a clientName from NameList-array
    public void removeName(String clientName) {

        if (nameList.contains(clientName)) {
            System.out.println(clientName + " " + "has left the chat.");
            nameList.remove(clientName);
            break;
        }
        //test purpose
        else {
            System.out.println("This name is not on the list");
        }
    }

    //print whole nameList to client
    public void printNameList(){
        for (int i = 0; i < nameList.size(); i++) {
            System.out.println(i);
        }
    }




}

