package bb.love_letter;

import java.util.ArrayList;


public class ListClientNames {

    static ArrayList<String> names = new ArrayList<>();

    public void addName(String clientName) {
        if (names.contains(clientName)) {
            System.out.println("This name is already taken.\nChoose another name, please.");
        } else {
            names.add(clientName);
            System.out.println("You can keep this name.");
        }
    }


    public static void removeName(String clientName) {
        if (names.contains(clientName)) {
            System.out.println(clientName + " " + "has left the chat.");
            names.remove(clientName);
        }

        else {
            System.out.println("This name is not on the list");
        }
    }

    public static void printNameList(){
        for (String name: names) {
            System.out.println(name);
        }
    }

    public static void main(String[] args) {
        names.add("abens");
        names.add("jdjoe");
        names.add("jksen");
        names.add("abens");
        printNameList();
        names.remove("abens");
        System.out.println("\n");
        printNameList();

    }
}


