package bb.love_letter.game;

import javafx.event.Event;

public class GameEvent {
    String message;
    GameEventType type;


    public enum GameEventType{
        GAMEISREADY,
        DISCARDSUCCESSFULL,
        NOSUCHCARDINHAND,
        PLAYERELIMINATED,
        PLAYERIMMUNE,
        PLAYERCHOSEN,

        GAMEFINISHED

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
}
