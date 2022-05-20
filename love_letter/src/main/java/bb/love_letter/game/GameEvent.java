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

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public void setGameEventType(GameEventType type) {
        this.gameEventType = type;
    }

    public void setMessage(String message) {
        this.message = message;
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
        CARD_EFFECT,
        TURN_ENDED,
        ERROR,
        GAMEISREADY,
        GAMEISNOTREADY,
        DISCARDSUCCESSFULL,
        PLAYERELIMINATED,
        PLAYERIMMUNE,
        INVALIDCHOICE,
        GAMEFINISHED,
        INFO,
        NO_SUCH_CARD_IN_HAND, POST_METHODS
    }

    public GameEventType getGameEventType() {
        return gameEventType;
    }

    public String getMessage() {
        return message;
    }

}
