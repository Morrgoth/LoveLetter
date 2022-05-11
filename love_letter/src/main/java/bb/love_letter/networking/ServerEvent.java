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
public class ServerEvent implements Serializable {

    public String message;
    public ServerEventType serverEventType;

    public ServerEvent(String message, ServerEventType serverEventType){
        this.message=message;
        this.serverEventType=serverEventType;
    }

    /**
     * @return The User to whom an Event happened (Login or Logout)
     */
    public String getMessage(){
        return message;
    }

    /**
     * @return The type of the UserEvent that happened.
     */
    public ServerEventType getServerEventType(){
        return serverEventType;
    }

    /**
     * It is used to store the type of Event that happened.
     */
    public enum ServerEventType {
        LOGIN_ERROR,
        NAME_ALREADY_TAKEN,
        PLAYER_LOGGED_OUT,
        ILLEGAL_MOVE,
        GAME_ERROR,
        LOGIN_CONFIRMATION,
        NEW_PLAYER_NOTIFICATION

    }

}
