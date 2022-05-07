package bb.love_letter;

public class UserEvent {
    //wrapper klasse für User

    //attributes
    private User user;
    private UserEventType userEventType;

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





