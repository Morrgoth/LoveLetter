package bb.love_letter.networking;

import bb.love_letter.game.User;

/**
 *
 * @author Tolga Engin
 */
public class LoginRequest {

    public User user;

    public LoginRequest(User user){
        this.user=user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User newUser) {
        this.user = newUser;
    }
}
