package bb.love_letter.game.characters;


import javafx.event.EventType;

public class GameEvent {
    private String message;
    public GameEventType type;


    public enum GameEventType{
        GAMEISREADY,
        NOSUCHCARDINHAND,
        PLAYERELIMINATED,
        PLAYERIMMUNE,
        GAMEFINISHED

    }

    public GameEvent(String message, GameEventType type){
        this.message = message;
        this.type = type;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setGameEvent(GameEventType type){
        this.type = type;
    }
}
