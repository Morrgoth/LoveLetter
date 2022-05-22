package bb.love_letter.networking.data;

import bb.love_letter.game.GameEvent;
import bb.love_letter.game.User;
import bb.love_letter.networking.data.Envelope;

import java.io.Serializable;

/**
 * This is the Class which determines the format for notifying Users of UserEvents (and eventually all Events) which
 * are currently Login and Logout Events.
 *
 * It can be thought of the Messages created and sent by the Server.
 *
 * @author Tolga Engin
 * @author Zeynab Baiani
 */
public class ServerEvent implements EnvelopeSerializable {

    private String message;
    private ServerEventType serverEventType;
    public ServerEvent(){}
    private transient User target= null;

    public ServerEvent(String message, ServerEventType serverEventType){
        this.message=message;
        this.serverEventType=serverEventType;
    }

    public ServerEvent (GameEvent gameEvent){
        setMessage(gameEvent.getMessage());
        setServerEventType(ServerEventType.GAME_EVENT);
        setTarget(gameEvent.getTarget());
    }

    public ServerEvent (String message, ServerEventType serverEventType, User target){
        this.message = message;
        this.serverEventType = serverEventType;
        this.target = target;
    }

    /**
     * It is used to store the type of Event that happened.
     */
    public enum ServerEventType {
        LOGIN_ERROR,
        NAME_ALREADY_TAKEN,
        LOGIN_CONFIRMATION,
        NEW_PLAYER_NOTIFICATION,
        LOGOUT_CONFIRMATION,
        PLAYER_LEFT_NOTIFICATION,
        GAME_EVENT,
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

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }


}
