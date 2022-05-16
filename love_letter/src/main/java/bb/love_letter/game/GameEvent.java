package bb.love_letter.game;

public class GameEvent {
    private GameEventType gameEventType;
    GameEventType type;

    public boolean eventState = false;

    public GameEvent (GameEventType gameEventType){
        this.gameEventType = gameEventType;
    }
    public enum GameEventType{
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



    public void changeState (GameEventType newState){
        GameEventType currentStateType = newState;
        boolean eventState = true;
        int counterState = 0;
    }
}
