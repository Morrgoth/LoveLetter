package bb.love_letter;
import java.util.ArrayList;
import java.util.Scanner;
//import java.io.*;

public class Main {

    public static boolean checkNames(ArrayList<String> players, String testName){
        return !players.contains(testName);
    }

    public static void main(String args[]){
        ArrayList<String> players = new ArrayList<>();
        players.add("abcd");
        players.add("agsgdh");
        players.add("qwerty");
        players.add("hahah");

        while(true){
            Scanner sc = new Scanner(System.in);
            System.out.print("Please enter your nickname.");

            String name = sc.nextLine();

            boolean ifAcceptable = checkNames(players, name);
            if(ifAcceptable){
                players.add(name);
                //System.out.println(players.toString());
                System.out.println("The entered nickname is acceptable.");
                break;
            }else{
                System.out.println("The entered nickname exists already. Please enter an another name:");
            }
        }


    }
}
