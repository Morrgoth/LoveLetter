package bb.love_letter.networking.data;

import bb.love_letter.networking.data.Envelope;

import java.io.Serializable;

/**
 * This is the Class which determines the format for notifying Users of UserEvents (and eventually all Events) which
 * are currently Login and Logout Events.
 *
 * It can be thought of the Messages created and sent by the Server.
 *
 * @author Tolga Engin
 */
public class ServerEvent implements EnvelopeSerializable {

    public String message;
    public ServerEventType serverEventType;

    public ServerEvent(){}

    public ServerEvent(String message, ServerEventType serverEventType){
        this.message=message;
        this.serverEventType=serverEventType;
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
        NEW_PLAYER_NOTIFICATION,
        LOGOUT_CONFIRMATION,
        PLAYER_LEFT_NOTIFICATION,
    }

    /**
     * @return The User to whom an Event happened (Login or Logout)
     */
    public String getMessage(){
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The type of the UserEvent that happened.
     */
    public ServerEventType getServerEventType(){
        return serverEventType;
    }
    public void setServerEventType(ServerEventType serverEventType){
        this.serverEventType=serverEventType;

    }


    public Envelope toEnvelope() {
        Envelope envelope = new Envelope(this, Envelope.EnvelopeType.SERVER_EVENT);
        return envelope;
    }


}
