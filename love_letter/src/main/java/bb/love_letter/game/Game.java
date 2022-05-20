package bb.love_letter.game;

import bb.love_letter.game.characters.*;

import java.util.ArrayList;
import java.util.HashMap;

import static bb.love_letter.game.GameEvent.GameEventType.*;

public class Game {
    private Deck deck;
    private int currentPlayer;
    private PlayerQueue playerQueue;
    private boolean isGameStarted;
    private boolean isGameOver;
    private boolean isRoundOver;
    private boolean isTurnOver;

    //list of all cards played in the round
    public static ArrayList<Cards> history;
    //list of players in the round that are not immune and can be chosen for a cardEffect
    public static ArrayList<Player> playerOption = new ArrayList<>();
    //list of current players still in the round

    public static HashMap<String, Integer> playerScores = new HashMap<String, Integer>();

    public Game() {
        deck = new Deck();
        playerQueue = new PlayerQueue();
        isGameOver = true;
        isGameStarted = true;
        history = new ArrayList<>();
    }

    private GameEvent withdrawFirstCards(){
        Cards removedCard = deck.draw();
        if (playerQueue.getPlayerCount() == 2) {
            Cards extraDiscarded1 = deck.draw();
            Cards extraDiscarded2 = deck.draw();
            Cards extraDiscarded3 = deck.draw();
            GameEvent discardNotification = new GameEvent(GameEvent.GameEventType.DISCARD_NOTIFICATION, "The" +
                    " following cards were removed from the deck: " + extraDiscarded1.getCardName() + ", "
                    + extraDiscarded2.getCardName() + ", " + extraDiscarded3.getCardName());
            return discardNotification;
        }
        return null;
    }
    public GameEvent init() {
        if (isGameOver && isGameStarted) {
            deck.reset();
            playerQueue.clear();
            isGameOver = false;
            isGameStarted = false;
            isRoundOver = true;
            isTurnOver = true;
            history.clear();
            return new GameEvent(GameEvent.GameEventType.GAME_INITIALIZED,"Print #help to see more information."); // TODO: print the available commands
        } else {
            return new GameEvent(GameEvent.GameEventType.ERROR, "A Game is already active, wait for it to finish!");
        }
    }

    public GameEvent addPlayer(User user) {
        if (isGameStarted) {
            return new GameEvent(GameEvent.GameEventType.ERROR, "The Game has already started! Wait for the next" +
                    " game to start.");
        } else {
            return playerQueue.addPlayer(user);
        }
    }

    public GameEvent startGame() {
        if (!isGameStarted) {
            if (playerQueue.getPlayerCount() >= 2) {
                isGameStarted = true;
                return new GameEvent(GameEvent.GameEventType.GAME_STARTED, "A new game has started!");
            } else {
                return new GameEvent(GameEvent.GameEventType.ERROR, "At least 2 Players must be in the lobby " +
                        "for the game to start!");
            }
        } else {
            return new GameEvent(GameEvent.GameEventType.ERROR, "A Game has already started, wait for it to end!");
        }
    }

    public ArrayList<GameEvent> startRound() {
        ArrayList<GameEvent> gameEvents = new ArrayList<>();
        if (isRoundOver) {
            isRoundOver = false;
            deck.reset();
            playerQueue.resetRound();
            GameEvent discardNotification = withdrawFirstCards();
            if (discardNotification != null) {
                gameEvents.add(discardNotification);
            }
            for (Player player: playerQueue.getPlayers()) {
                player.addCard(deck.draw());
            }

            gameEvents.add(new GameEvent(GameEvent.GameEventType.ROUND_STARTED, "A new round has started!"));
        } else {
            gameEvents.add(new GameEvent(GameEvent.GameEventType.ERROR, "The current round hasn't ended yet!"));
        }
        return gameEvents;
    }

    public ArrayList<GameEvent> startTurn() {
        ArrayList<GameEvent> gameEvents = new ArrayList<>();
        if (isTurnOver) {
            Player player = playerQueue.getCurrentPlayer();
            isTurnOver = false;
            Cards card = deck.draw();
            addCard(card, player);
            player.setImmune(false);
            gameEvents.add(new GameEvent(GameEvent.GameEventType.TURN_STARTED, "The turn of " + player.getName()
                    + " has started!"));
            gameEvents.add(new GameEvent(GameEvent.GameEventType.CARD_ADDED, "You drew a " + card.getCardName() +
                    ".\n The effect of the card is: " + card.getCardAction() + "\n Your current hand is: \n" +
                    player.printHand() + ".\n The effect of the card is: " + player.getCard1().getCardAction(), player));
        } else {
            gameEvents.add(new GameEvent(GameEvent.GameEventType.ERROR, "The current turn is not yet over!"));
        }
        return gameEvents;
    }

    //später nach Server schieben damit es direkt die userCommand aus dem chat holen kann
    public final GameEvent infoPost (String userCommand){
        try {
            switch (userCommand) {
                case "#help":
                    return new GameEvent(GameEvent.GameEventType.POSTHELP, "Print the following commands to receive further information:\n #score: see the current player scores. \n #cards: get information, about the distinct card effects.\n #history: see what cards have been played in this round.");
                case "#score":
                    return new GameEvent(GameEvent.GameEventType.POSTSCORE, " " + playerScores);
                case "#cards":
                    return new GameEvent(GameEvent.GameEventType.POSTCARDS,"Guard: " + Guard.getCardAction()+"\n"+ "Priest: " + Priest.getCardAction()+"\n"+ "Baron: " + Baron.getCardAction()+"\n"+ "Handmaid: " +Handmaid.getCardAction()+"\n"+ "Prince: " + Prince.getCardAction()+"\n"+ "King: " +King.getCardAction()+"\n"+ "Countess: " +Countess.getCardAction()+"\n"+"Princess: " + Princess.getCardAction());
                case "#history":
                    return new GameEvent(GameEvent.GameEventType.POSTHISTORY, " " + history);
            }
        }catch (NullPointerException e)
            {
                System.out.print("Not a valid command.");
            }
        return null;
    }

    public ArrayList<GameEvent> playCard(User user, GameAction action) {
        ArrayList<GameEvent> gameEvents = new ArrayList<>();
        if (playerQueue.getCurrentPlayer().getName().equals(user.getName())) {
            Player player = playerQueue.getCurrentPlayer();
            Cards card = null;
            if(action.getCardIndex() == 1){
                card = player.getCard1();
            }else if(action.getCardIndex() == 2){
                card = player.getCard2();
            } else {
                gameEvents.add(new GameEvent(INVALID_ACTION, "Please enter a valid card index!", player));
                return gameEvents;
            }
            //Check if the card is PRINCESS
            if(checkIfPrincess(card)){
                discardCard(action.getCardIndex(), player);
                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() +
                        " discarded the Princess and was eliminated"));
                player.setInGame(false);
            }//Check if the player has COUNTESS and PRINCE or KING at the same time
            else if(checkIfCountess(player.getCard1(), player.getCard2())){
                if(player.getCard1() instanceof Countess){
                    discardCard(1, player);
                }else {
                    discardCard(2, player);
                }
                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName()
                        + " discarded the Countess"));
            }//Discard Countess without King or Prince in hand
            else if(card instanceof Countess){
                discardCard(action.getCardIndex(), player);
                gameEvents.add(new GameEvent(VALID_ACTION, player.getName() + " discarded the Countess"));
            }
            else if(card instanceof Handmaid){
                discardCard(action.getCardIndex(), player);
                gameEvents.add(((Handmaid) card).useHandmaid(player));
            }//Exclude the effect of COUNTESS and PRINCESS
            else{
                Player targetPlayer = playerQueue.getPlayerByName(action.getTarget());
                if(targetPlayer != null){
                    if(!targetPlayer.isImmune() && targetPlayer.getInGame()){
                        if(card instanceof Baron){
                            discardCard(action.getCardIndex(), player);
                            gameEvents.add(((Baron) card).useBaron(player, targetPlayer));
                        }else if(card instanceof Guard){
                            discardCard(action.getCardIndex(), player);
                            gameEvents.add(((Guard) card).useGuard(player, targetPlayer, action.getGuess()));
                        }else if(card instanceof King){
                            discardCard(action.getCardIndex(), player);
                            gameEvents.add(((King) card).useKing(player, targetPlayer));
                            gameEvents.add(new GameEvent(GameEvent.GameEventType.CARD_EFFECT,
                                    "You switched hands with " +
                                            targetPlayer.getName() + ", you got a " + player.getCard1().getCardName(), player));
                            gameEvents.add(new GameEvent(GameEvent.GameEventType.CARD_EFFECT,
                                    "You switched hands with " +
                                            player.getName() + ", you got a " + targetPlayer.getCard1().getCardName(), targetPlayer));
                        }else if(card instanceof Prince){
                            discardCard(action.getCardIndex(), player);
                            gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() +
                                    " discarded the Prince and targeted " + targetPlayer.getName()));
                            ArrayList<GameEvent> princeEvent = usePrince(player, targetPlayer);
                            for(int i = 0; i<princeEvent.size(); i++){
                                gameEvents.add(princeEvent.get(i));
                            }

                        }else if(card instanceof Priest){
                            discardCard(action.getCardIndex(), player);
                            gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() +
                                    " discarded the Priest and targeted " + targetPlayer.getName()));
                            gameEvents.add(((Priest) card).usePriest(player, targetPlayer));
                        }
                    }else if(targetPlayer.isImmune()){
                        gameEvents.add(new GameEvent(INVALID_ACTION, targetPlayer.getName() +
                                " is immune, you cannot target them.", player));
                    }else if (!targetPlayer.getInGame()){
                        gameEvents.add(new GameEvent(INVALID_ACTION, targetPlayer.getName() +
                                " is eliminated, you cannot target them.", player));
                    }

                }else{
                    gameEvents.add(new GameEvent(INVALID_ACTION, "Please enter a (valid) target player.", player));
                }
            }

        } else {
            gameEvents.add(new GameEvent(GameEvent.GameEventType.ERROR, "It is not your turn. It is the turn of " +
                    playerQueue.getCurrentPlayer() + "!", user));
        }
        return gameEvents;
    }

    /**
     * This method is to be called in each successful playCard() call, it is public for testing purposes
     */
    public void endTurn() {
        isTurnOver = true;
    }

    public GameEvent finishTurn() {
        Player player = playerQueue.getCurrentPlayer();
        if (isTurnOver) {
            if (deck.size() == 0 || playerQueue.getPlayersInRoundCount() == 1) {
                // ROUND IS OVER
                isRoundOver = true;
                ArrayList<Player> roundWinners = findRoundWinner();
                for (Player winner: roundWinners) {
                    winner.setScore(winner.getScore() + 1);
                }
                if (findGameWinner().size() > 0) {
                    // GAME OVER: A Player has at least 4 tokens
                    ArrayList<Player> gameWinners = findGameWinner();
                    isGameOver = true;
                    String message = "This round has ended. The winner is " + roundWinners.get(0).getName() + "!\n"
                            + "This game has ended. The winner is " + gameWinners.get(0).getName() + "! Congratulations!";
                    return new GameEvent(GameEvent.GameEventType.GAME_ENDED, message);
                } else {
                    // Next round can begin
                    return new GameEvent(GameEvent.GameEventType.ROUND_ENDED, "This round has ended. The winner is " +
                            roundWinners.get(0).getName() + "!");
                }
            } else {
                // ROUND IS NOT YET OVER
                playerQueue.setCurrentPlayerToNext();
                return new GameEvent(GameEvent.GameEventType.TURN_ENDED, "The turn of " +
                        player.getName() + " ended!");
            }
        } else {
            return new GameEvent(GameEvent.GameEventType.ERROR, "The current player ("
                    + player.getName() + ") hasn't discarded their card, yet!");

        }
    }

    private ArrayList<Player>  findRoundWinner() {
        ArrayList <Player> roundWinner = new ArrayList<>();
        ArrayList<Player> playersInRound = playerQueue.getPlayersInRound();
        if (playerQueue.getPlayersInRound().size() == 1) {
            roundWinner.add(playerQueue.getPlayersInRound().get(0));
        } else if (deck.size() == 0 && playerQueue.getPlayersInRoundCount() >= 2) {
            if (playersInRound.get(0).getCard1().getCardPoints() > playersInRound.get(1).getCard1().getCardPoints()) {
                roundWinner.add(playersInRound.get(0));
            } else if (playersInRound.get(0).getCard1().getCardPoints() < playersInRound.get(1).getCard1().getCardPoints()) {
                roundWinner.add(playersInRound.get(1));
            } else {
                if (discardedPoints(playersInRound.get(0).getDiscarded(), playersInRound.get(0)) > discardedPoints(playersInRound.get(1).getDiscarded(), playersInRound.get(1))) {
                    roundWinner.add(playersInRound.get(0));
                } else if (discardedPoints(playersInRound.get(0).getDiscarded(), playersInRound.get(0)) < discardedPoints(playersInRound.get(1).getDiscarded(), playersInRound.get(1))) {
                    roundWinner.add(playersInRound.get(1));
                } else {
                    roundWinner.add(playersInRound.get(0));
                    roundWinner.add(playersInRound.get(1));
                }
            }
        }
        return roundWinner;
    }


    private ArrayList<Player> findGameWinner () {
        ArrayList<Player> gameWinners = new ArrayList<>();
        for (Player player : playerQueue.getPlayers()) {
            if (playerQueue.getPlayerCount() == 4 && player.getScore() >= 4) {
                gameWinners.add(player);
            } else if (playerQueue.getPlayerCount() == 3 && player.getScore() >= 5) {
                gameWinners.add(player);
            } else if (playerQueue.getPlayerCount() == 2 && player.getScore() >= 7) {
                gameWinners.add(player);
            }
        }
        return gameWinners;
    }

    /**
     * Used for testing purposes
     * @return
     */
    public Deck getDeck() {
        return deck;
    }

    


    public boolean checkIfPrincess(Cards card) {
        if (card instanceof Princess) {
            return true;
        }
        return false;
    }

    public boolean checkIfCountess(Cards card1, Cards card2) {

        //check for COUNTESS
        if (card1 instanceof Countess && (card2 instanceof Prince || card2 instanceof King)) {
            return true;
        }else if (card2 instanceof Countess && (card1 instanceof Prince || card1 instanceof King)) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<GameEvent> usePrince(Player sourcePlayer, Player targetPlayer){
        ArrayList<GameEvent> princeEvent = new ArrayList<>();
        if(checkIfPrincess(targetPlayer.getCard1())){
            targetPlayer.setInGame(false);
            princeEvent.add(new GameEvent(CARD_EFFECT, targetPlayer.getName() + " has a Princess and was eliminated"));
        }else{
            princeEvent.add(new GameEvent(CARD_EFFECT, targetPlayer.getName() + " discarded a " +
                    targetPlayer.getCard1().getCardName() + " with Prince-effect and drew a new card from deck"));
            discardCard(1, targetPlayer);
            addCard(deck.draw(), targetPlayer);
            princeEvent.add(new GameEvent(CARD_EFFECT, "You got a " + deck.draw().getCardName() +
                    " from deck.", targetPlayer));
        }
        return princeEvent;
    }


    //player's action - draw the top most  card from deck
    public void drawCard(Player player) {

        player.setCard2(deck.getDeck().get(0));;
        deck.getDeck().remove(0);
    }

    public int discardedPoints(ArrayList<Cards> discarded, Player player) {
        int sum = 0;
        for (Cards card : player.getDiscarded()) {
            sum += card.getCardPoints();
        }
        return sum;
    }

    public void discardCard(int cardNumber, Player player) {
        switch (cardNumber) {
            case 1:
                Game.history.add(player.getCard1());
                player.discarded.add(player.getCard1());
                player.setCard1(player.getCard2());
                player.setCard2(null);
                break;

            case 2:
                Game.history.add(player.getCard2());
                player.discarded.add(player.getCard2());
                player.setCard2(null);
                break;

            default:
                break;
        }
    }

    //discard a Card during each round
    private void clearDiscardedList (ArrayList < Cards > discarded) {
        //delete all elements in List when a round end
        for (int i = 0; i < discarded.size(); i++) {
            discarded.remove(0);
        }
    }


    public void addCard(Cards card, Player player) {
        if (player.getCard1() == null) {
            player.setCard1(card);
        } else {
            player.setCard2(card);
        }
    }

    public PlayerQueue getPlayerQueue() {
        return playerQueue;
    }
}
