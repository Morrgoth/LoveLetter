package bb.love_letter.networking.data;

import bb.love_letter.game.User;
import bb.love_letter.networking.data.Envelope;

/**
 *
 * @author Tolga Engin
 */
public class LoginRequest implements EnvelopeSerializable{

    public User user;

    public LoginRequest(User user){
        this.user=user;
    }
    public LoginRequest(){
    }

    public User getUser() {
        return user;
    }

    public void setUser(User newUser) {
        this.user = newUser;
    }

    public Envelope toEnvelope() {
        Envelope envelope = new Envelope(this, Envelope.EnvelopeType.LOGIN_REQUEST);
        return envelope;
    }
}
