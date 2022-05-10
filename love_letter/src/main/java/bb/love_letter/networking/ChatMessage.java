package bb.love_letter.networking;


import bb.love_letter.game.User;

import java.io.Serializable;

/**
 * This is the Class for storing Messages created by a User. It is the format chat messages are sent and received
 * in.
 *
 * @author Muqiu Wang
 */
public class ChatMessage implements Serializable {
    public User sender;
    public String message;

    public ChatMessage(User sender, String message){
        this.sender = sender;
        this.message = message;
    }

    /**
     * @return The User, that sent the message.
     */
    public User getSender(){
        return sender;
    }

    /**
     * @return The contents of the message.
     */
    public String getMessage(){
        return message;
    }

    /**
     * This method is only intended for deserialization.
     * @param user The User, that sent the message.
     */
    public void setSender(User user) {
        this.sender = user;
    }
}
