package bb.love_letter.game;

//Diese Klasse wurde von Veronika Heckel bearbeitet

import java.io.Serializable;

/**
 *
 * @author Veronika Heckel
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
