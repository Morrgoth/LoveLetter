package bb.love_letter.game;

import javafx.event.Event;

public class GameEvent {
    String message;
    GameEventType type;

    public enum GameEventType{
        GAMEISREADY,
        NOSUCHCARDONHAND,
        PLAYERELIMINATED,
        PLAYERIMMUNE,
        GAMEFINISHED

    }

    public void setType(GameEventType type) {
        this.type = type;
    }
}
