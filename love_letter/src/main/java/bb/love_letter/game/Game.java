package bb.love_letter.game;

import bb.love_letter.game.characters.*;

import java.util.ArrayList;
import java.util.HashMap;

import static bb.love_letter.game.GameEvent.GameEventType.CARD_EFFECT;
import static bb.love_letter.game.GameEvent.GameEventType.GAMEISREADY;

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
    //list of players in the round that are not immune and can be choosen for a cardEffect
    public static ArrayList<Player> playerOption = new ArrayList<>();
    //list of current players still in the round

    public static HashMap<String, Integer> playerScores = new HashMap<String, Integer>();

    public Game() {
        deck = new Deck();
        playerQueue = new PlayerQueue();
        isGameOver = true;
        isGameStarted = true;
    }

    private GameEvent withdrawFirstCards(){
        Cards removedCard = deck.draw();
        if (playerQueue.getPlayerCount() == 2) {
            Cards extraDiscraded1 = deck.draw();
            Cards extraDiscarded2 = deck.draw();
            Cards extraDiscarded3 = deck.draw();
            GameEvent discardNotification = new GameEvent(GameEvent.GameEventType.DISCARD_NOTIFICATION, "The" +
                    " following cards were removed from the deck: " + extraDiscraded1.getCardName() + ", "
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
        if (playerQueue.getCurrentPlayer().equals(user)) {
            Player player = playerQueue.getCurrentPlayer();
            switch(action.getCardIndex()){
                case 1:
                    Cards card1 = player.getCard1();
                    //Check if the card is PRINCESS
                    if(checkIfPrincess(card1)){
                        discardCard(1, player);
                        gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() +
                                " discarded the Princess and was eliminated."));
                        player.setInGame(false);
                    }//Check if the player has COUNTESS and PRINCE or KING at the same time
                    else if(checkIfCountess(player.getCard1(), player.getCard2())){
                        if(card1 instanceof Countess){
                            discardCard(1, player);
                            gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName()
                                    + " discarded the Countess."));
                        }else{
                            gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                    "You can only discard COUNTESS.", player));
                            discardCard(2, player);
                            gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION,
                                    player.getName() + " discarded the Countess."));
                        }
                    }//Exclude the effect of COUNTESS and PRINCESS
                    else{
                        Player targetPlayer = null;
                        for(Player target: playerQueue.getPlayersInRound()){
                            if(target.getName().equalsIgnoreCase(action.getTarget()) && !target.isImmune()){
                                targetPlayer = target;
                            }
                        }
                        if(card1 instanceof Baron){
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                        "Please choose a (valid) target player.", player));
                            }else{
                                discardCard(1, player);
                                gameEvents.add(((Baron) card1).useBaron(player, targetPlayer));
                            }
                        }else if(card1 instanceof Guard){
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                        "Please choose a (valid) target player.", player));
                            }else{
                                discardCard(1, player);
                                gameEvents.add(((Guard) card1).useGuard(player, targetPlayer, action.getGuess()));
                            }
                        }else if(card1 instanceof Handmaid){
                            discardCard(1, player);
                            gameEvents.add(((Handmaid) card1).useHandmaid(player));
                        }else if(card1 instanceof King){
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                        "Please choose a (valid) target player.", player));
                            }else{
                                discardCard(1, player);
                                gameEvents.add(((King) card1).useKing(player, targetPlayer));
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.CARD_EFFECT, "You switched hands with " +
                                       targetPlayer.getName() + ", you got a " + player.getCard1().getCardName(), player));
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.CARD_EFFECT, "You switched hands with " +
                                        player.getName() + ", you got a " + targetPlayer.getCard1().getCardName(), targetPlayer));
                            }
                        }else if(card1 instanceof Prince){
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                        "Please choose a (valid) target player.", player));
                            }else{
                                discardCard(1, player);
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() +
                                        " discarded the Prince and targeted " + targetPlayer.getName()));
                                gameEvents.add(usePrince(player, targetPlayer, deck));
                            }

                        }else if(card1 instanceof Priest){
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                        "Please choose a (valid) target player.", player));
                            }else{
                                discardCard(1, player);
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() +
                                        " discarded the Priest, and targeted " + targetPlayer.getName()));
                                gameEvents.add(((Priest) card1).usePriest(player, targetPlayer));
                            }
                        }
                    }
                    break;

                case 2:
                    Cards card2 = player.getCard1();
                    //Check if the card is PRINCESS
                    if(checkIfPrincess(card2)){
                        discardCard(2, player);
                        gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() +
                                " discarded the Princess and was eliminated."));
                        player.setInGame(false);
                    }//Check if the player has COUNTESS and PRINCE or KING at the same time
                    else if(checkIfCountess(player.getCard1(), player.getCard2())){
                        if(card2 instanceof Countess){
                            discardCard(2, player);
                            gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName()
                                    + " discarded the Countess."));
                        }else{
                            gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                    "You can only discard COUNTESS.", player));
                            discardCard(1, player);
                            gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION,
                                    player.getName() + " discarded the Countess."));
                        }
                    }//Exclude the effect of COUNTESS and PRINCESS
                    else{
                        Player targetPlayer = null;
                        for(Player target: playerQueue.getPlayersInRound()){
                            if(target.getName().equalsIgnoreCase(action.getTarget()) && !target.isImmune()){
                                targetPlayer = target;
                            }
                        }
                        if(card2 instanceof Baron){
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                        "Please choose a (valid) target player.", player));
                            }else{
                                discardCard(2, player);
                                gameEvents.add(((Baron) card2).useBaron(player, targetPlayer));
                            }
                        }else if(card2 instanceof Guard){
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                        "Please choose a (valid) target player.", player));
                            }else{
                                discardCard(2, player);
                                gameEvents.add(((Guard) card2).useGuard(player, targetPlayer, action.getGuess()));
                            }
                        }else if(card2 instanceof Handmaid){
                            discardCard(2, player);
                            gameEvents.add(((Handmaid) card2).useHandmaid(player));
                        }else if(card2 instanceof King){
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                        "Please choose a (valid) target player.", player));
                            }else{
                                discardCard(2, player);
                                gameEvents.add(((King) card2).useKing(player, targetPlayer));
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.CARD_EFFECT, "You switched hands with " +
                                        targetPlayer.getName() + ", you got a " + player.getCard1().getCardName(), player));
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.CARD_EFFECT, "You switched hands with " +
                                        player.getName() + ", you got a " + targetPlayer.getCard1().getCardName(), targetPlayer));
                            }
                        }else if(card2 instanceof Prince){
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                        "Please choose a (valid) target player.", player));
                            }else{
                                discardCard(2, player);
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() +
                                        " discarded the Prince and targeted " + targetPlayer.getName()));
                                gameEvents.add(usePrince(player, targetPlayer, deck));
                            }

                        }else if(card2 instanceof Priest){
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                        "Please choose a (valid) target player.", player));
                            }else{
                                discardCard(2, player);
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() +
                                        " discarded the Priest, and targeted " + targetPlayer.getName()));
                                gameEvents.add(((Priest) card2).usePriest(player, targetPlayer));
                            }
                        }
                    }
                    break;
                default:
                    gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION, "Please enter a (valid) card index: 1 or 2.", player));
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
        if (card.getCardName().equals("PRINCESS")) {
            return true;
        }
        return false;
    }

    public boolean checkIfCountess(Cards card1, Cards card2) {

        //check for COUNTESS
        if (card1.getCardName().equals("COUNTESS") && (card2.getCardName().equals("PRINCE") || card2.getCardName().equals("KING"))) {
            return true;
        }else if (card2.getCardName().equals("COUNTESS") && (card1.getCardName().equals("PRINCE") || card1.getCardName().equals("KING"))) {
            return true;
        } else {
            return false;
        }
    }

    public GameEvent usePrince(Player sourcePlayer, Player targetPlayer, Deck deck){
        discardCard(1, targetPlayer);
        if(checkIfPrincess(targetPlayer.getCard1())){
            targetPlayer.setInGame(false);
            return new GameEvent(CARD_EFFECT, targetPlayer.getName() + " has a Princess and was eliminated");
        }else{
            addCard(deck.draw(), targetPlayer);
            return new GameEvent(CARD_EFFECT, "You got a " + deck.draw().getCardName() + " from deck.", targetPlayer);
        }
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

    /*public void discard(int cardIndex, Player player) {
        if (cardIndex == 1) {
            player.setCard1(null);
        } else {
            player.setCard2(null);
        }
    }*/

    public PlayerQueue getPlayerQueue() {
        return playerQueue;
    }
}