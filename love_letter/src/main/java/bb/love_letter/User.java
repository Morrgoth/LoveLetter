package bb.love_letter;

//Diese Klasse wurde von Veronika Heckel bearbeitet

import java.io.Serializable;

public class User implements Serializable {
    private String name;

    public User (String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
