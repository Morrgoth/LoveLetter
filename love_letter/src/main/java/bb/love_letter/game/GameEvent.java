package bb.love_letter.game;

import javafx.event.Event;

public class GameEvent {
    String message;
    GameEventType type;

    public boolean eventState;
    public enum GameEventType{
        GAMEISREADY,
        DISCARDSUCCESSFULL,
        NOSUCHCARDINHAND,
        PLAYERELIMINATED,
        PLAYERIMMUNE,
        PLAYERCHOSEN,
        INVALIDCHOICE,
        GAMEFINISHED,
        KINGACTION

    }

    public GameEvent (String message, GameEventType type){
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(GameEventType type) {
        this.type = type;
    }

    public void changeState (boolean eventState, GameEventType newState){
        GameEventType currentStateType = newState;
        this.eventState = true;
        int counterState = 0;
    }
}
