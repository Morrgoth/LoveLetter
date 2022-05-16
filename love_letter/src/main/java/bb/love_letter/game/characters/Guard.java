package bb.love_letter.game.characters;

import bb.love_letter.game.GameApplication;
import bb.love_letter.game.GameEvent;
import bb.love_letter.game.Player;

/*
    Strength: 1
    AmountInDeck: 5
    Effects: Player may choose another player and name a card other than Guard.
    If the chosen player's hand contains that card, that player is eliminated from the round.
 */
public class Guard  extends Cards {
    private String name = "Guard";
    private final int cardPoints = 1;
    private String cardAction = "Choose a player and a specific card. If you're right and it's his in his hand, this player is terminated for the Round.";

    @Override
    public String getCardName() {
        return name;
    }

    @Override
    public String getCardAction() {
        return cardAction;
    }

    @Override
    public int getCardPoints() {
        return cardPoints;
    }

    //need hint for sourcePlayer to input an int corresponding to a cardValue
    public String guessCard(int cardPoints){
        String chosenCard = null;

        if ( cardPoints > 1 && cardPoints < 9){
            switch (cardPoints) {
                //case 1 -> chosenCard = "Guard";
                case 2 -> chosenCard = "Priest";
                case 3 -> chosenCard = "Baron";
                case 4 -> chosenCard = "Handmaid";
                case 5 -> chosenCard = "Prince";
                case 6 -> chosenCard = "King";
                case 7 -> chosenCard = "Countess";
                case 8 -> chosenCard = "Princess";
            }

        }
        else {
            System.out.println ("This card doesn't exist. Try again.");
        }
        return  chosenCard;
    }

    public void useGuard(Player chosenPlayer, String chosenCard){
        //how to set player1? and how to set it automatically during the game?

        //1.player1 chooses player2
        //      Player.choosePlayer();

        //choose card to guess
        //  Guard.guessCard();

        //compare name, if correct: terminate Round for player;
        //else do nothing;
        GameEvent guardEvent = new GameEvent(GameEvent.GameEventType.PLAYERIMMUNE);
        guardEvent.changeState(GameEvent.GameEventType.GUARDACTION);

        if (chosenPlayer.getCard1().getCardName().equals(chosenCard)) {
            GameApplication.playersInRound.remove(chosenPlayer);
            chosenPlayer.setInGame(false);
            guardEvent.changeState(GameEvent.GameEventType.PLAYERELIMINATED);
        }
    }
}
