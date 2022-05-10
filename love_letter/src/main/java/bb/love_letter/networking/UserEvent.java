package bb.love_letter.networking;

import bb.love_letter.game.User;

import java.io.Serializable;

/**
 * This is the Class which determines the format for notifying Users of UserEvents (and eventually all Events) which
 * are currently Login and Logout Events.
 *
 * It can be thought of the Messages created and sent by the Server.
 *
 * @author Tolga Engin
 */
public class UserEvent implements Serializable {

    public User user;
    public UserEventType userEventType;

    public UserEvent(User user, UserEventType userEventType){
        this.user=user;
        this.userEventType=userEventType;
    }

    /**
     * @return The User to whom an Event happened (Login or Logout)
     */
    public User getUser(){
        return user;
    }

    /**
     * @return The type of the UserEvent that happened.
     */
    public UserEventType getUserEventType(){
        return userEventType;
    }

    /**
     * It is used to store the type of Event that happened.
     */
    public enum UserEventType {
        LOGIN_REQUEST,
        LOGOUT_REQUEST,
        LOGIN_CONFIRMATION,
        LOGOUT_CONFIRMATION,
        LOGIN_ERROR

    }

}





