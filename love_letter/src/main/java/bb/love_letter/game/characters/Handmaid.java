package bb.love_letter.game.characters;

import bb.love_letter.game.GameEvent;
import bb.love_letter.game.Player;

public class Handmaid extends Cards {
    private String name = "Handmaid";
    private final int cardPoints = 4;
    private static String cardAction = "This card grants you immunity until your next turn.";

    @Override
    public String getCardName() {
        return name;
    }


    public static String getCardAction() {
        return cardAction;
    }

    @Override
    public int getCardPoints() {
        return cardPoints;
    }

    public GameEvent useHandmaid(Player sourcePlayer){
        GameEvent handmaidEvent = new GameEvent (GameEvent.GameEventType.VALID_ACTION, sourcePlayer.getName() +
                " discarded the Handmaid, and is immune until their next turn");
        sourcePlayer.setImmune(true);
        return handmaidEvent;
    }
}
