package bb.love_letter.game;

public class GameEvent {
    private GameEventType gameEventType;
    private String message;
    private User target = null;
    public boolean eventState = false;

    public GameEvent (GameEventType gameEventType){
        this.gameEventType = gameEventType;
    }

    public GameEvent (GameEventType gameEventType, String message){
        this.gameEventType = gameEventType;
        this.message = message;
    }

    public GameEvent (GameEventType gameEventType, String message, User target){
        this.gameEventType = gameEventType;
        this.message = message;
        this.target = target;
    }
    public enum GameEventType{
        GAME_INITIALIZED,
        GAME_STARTED,
        GAME_ENDED,
        PLAYER_ADDED,
        ROUND_STARTED,
        ROUND_ENDED,
        DISCARD_NOTIFICATION,
        TURN_STARTED,
        CARD_ADDED,
        VALID_ACTION,
        INVALID_ACTION,
        PLAYER_EFFECT,
        TURN_ENDED,
        ERROR,
        GAMEISREADY,
        GAMEISNOTREADY,
        DISCARDSUCCESSFULL,
        NOSUCHCARDINHAND,
        PLAYERELIMINATED,
        PLAYERIMMUNE,
        PLAYERCHOSEN,
        INVALIDCHOICE,
        GUARDACTION,
        PRIESTACTION,
        BARONACTION,
        HANDMAIDACTION,
        PRINCEACTION,
        KINGACTION,
        COUNTESSACTION,
        PRINCESSACTION,
        GAMEFINISHED

    }

    public GameEventType getGameEventType() {
        return gameEventType;
    }

    public String getMessage() {
        return message;
    }

    public void changeState (GameEventType newState){
        GameEventType currentStateType = newState;
        boolean eventState = true;
        int counterState = 0;
    }
}
