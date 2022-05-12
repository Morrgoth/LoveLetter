package bb.love_letter.game.characters;

public interface Playable {

    String getCardName();
    String getCardAction();
    int getCardPoints();

    int pickUp();
    int putDown();
    int see();



}
