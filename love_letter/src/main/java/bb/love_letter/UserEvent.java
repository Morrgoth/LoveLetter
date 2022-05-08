package bb.love_letter;

import java.io.Serializable;

public class UserEvent implements Serializable {
    //wrapper klasse für User

    //attributes
    public User user;
    public UserEventType userEventType;

    //konstruktor
    public UserEvent(User user, UserEventType userEventType){
        this.user=user;
        this.userEventType=userEventType;
    }

    //getter methode
    public User getUser(){
        return user;
    }
    public UserEventType getUserEventType(){
        return userEventType;
    }

    public static enum UserEventType {
        LOGIN_REQUEST,
        LOGOUT_REQUEST,
        LOGIN_CONFIRMATION,
        LOGOUT_CONFIRMATION,
        LOGIN_ERROR

    }

}





