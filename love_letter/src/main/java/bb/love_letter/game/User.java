package bb.love_letter.game;

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
    public User (){}

    /**
     * @return The username of the User
     */
    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public int hashCode() {
        // TODO
        return super.hashCode();
    }
}
