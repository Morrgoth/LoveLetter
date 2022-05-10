package bb.love_letter;


import java.io.Serializable;


/**
 * This class represents the User in the Chat as a Person with its name.
 * Each Username will be stored in a Hashmap (created in the Class ClientList)
 * paired with the relating Socket to ensure that the login/logout process can be handled correctly.
 * equals - Methods checks if two Users as an Object are the same and if the Names for both Users are equal
 *
 * @author Veronika
 */

public class User implements Serializable {
    public String name;

    public User (String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        User other = (User) obj;

        if(this.getName().equals(other.getName())) {
            return true;
        } else {
            return false;
        }
    }
}
