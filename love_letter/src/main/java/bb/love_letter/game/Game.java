package bb.love_letter.game;

import bb.love_letter.game.characters.*;

import java.util.ArrayList;
import java.util.HashMap;

import static bb.love_letter.game.GameEvent.GameEventType.CARD_EFFECT;
import static bb.love_letter.game.GameEvent.GameEventType.GAMEISREADY;

public class Game {
    private Deck deck;
    private ArrayList<Player> playersInGame;
    public static ArrayList<Player> playersInRound;
    private int currentPlayer;
    private ArrayList<Player> roundWinner;
    private ArrayList <Player> gameWinner;
    private boolean isGameStarted;
    private boolean isGameOver;
    private boolean isRoundOver;
    private boolean isTurnOver;

    //list of all cards played in the round
    public static ArrayList<Cards> history;
    //list of players in the round that are not immune and can be choosen for a cardEffect
    public static ArrayList<Player> playerOption = new ArrayList<>();
    //list of current players still in the round
    static ArrayList<Player> playersInGame = new ArrayList<>();

    public static HashMap<String, Integer> playerScores = new HashMap<String, Integer>();

    public Game() {
        deck = new Deck();
        playerQueue = new PlayerQueue();
        isGameOver = true;
        isGameStarted = true;
    }

    private void withdrawFirstCards(Deck deck){
        GameEvent stateFirstCards = new GameEvent(GameEvent.GameEventType.PLAYERIMMUNE);
        //When there are 2 players, 4 cards out of the deck and last 3 cards from them should be seen by all
        if(playersInGame.size() == 2){
            for(int i = 0; i<4; i++){
                if(i > 0){
                    stateFirstCards.changeState(GAMEISREADY);
                }
                //The removed card is added to the history
                history.add(deck.getDeck().get(0));
                deck.getDeck().remove(0);
            }
        }//When there are more than 2 players, only 1 card is out, and it shouldn't be seen
        else if(playersInGame.size() == 3 || playersInGame.size() == 4){
            history.add(deck.getDeck().get(0));
            deck.getDeck().remove(0);

        }

        //Players get the first card in the beginning
        for(int i = 0; i< playersInGame.size(); i++){
            playersInGame.get(i).setCard1(deck.getDeck().get(0));
            deck.getDeck().remove(0);
        }
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
            Cards removedCard = deck.draw();
            if (playerQueue.getPlayerCount() == 2) {
                Cards extraDiscraded1 = deck.draw();
                Cards extraDiscarded2 = deck.draw();
                Cards extraDiscarded3 = deck.draw();
                GameEvent discardNotification = new GameEvent(GameEvent.GameEventType.DISCARD_NOTIFICATION, "The" +
                        " following cards were removed from the deck: " + extraDiscraded1.getCardName() + ", "
                        + extraDiscarded2.getCardName() + ", " + extraDiscarded3.getCardName());
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
        Player player = playersInRound.get(currentPlayer);
        Cards card = deck.draw();
        Game.initializePlayerOption();
        addCard(card, player);
        player.setImmune(false);
        gameEvents.add(new GameEvent(GameEvent.GameEventType.TURN_STARTED, "The turn of " + player.getName()
                + " started!"));
        gameEvents.add(new GameEvent(GameEvent.GameEventType.CARD_ADDED, "You drew a " + card.getCardName() +
                ".\n The effect of the card is: " + card.getCardAction() + "\n Your current hand is: \n" +
                player.printHand() + ".\n The effect of the card is: " + player.getCard1().getCardAction(), player));
        return gameEvents;
    }

    //später nach Server schieben damit es direkt die userCommand aus dem chat holen kann
    public final GameEvent infoPost (String userCommand){
        try {
            switch (userCommand) {
                case "#help":
                    return new GameEvent(GameEvent.GameEventType.POSTHELP, "Print the following commands to receive further information:\n #score: see the current player scores. \n #cards: get information, about the distinct card effects.\n #history: see what cards have been played in this round.");
                    break;
                case "#score":
                    return new GameEvent(GameEvent.GameEventType.POSTSCORE, " " + playerScores);
                    break;
                case "#cards":
                    return new GameEvent(GameEvent.GameEventType.POSTCARDS,"Guard: " + Guard.getCardAction()+"\n"+ "Priest: " + Priest.getCardAction()+"\n"+ "Baron: " + Baron.getCardAction()+"\n"+ "Handmaid: " +Handmaid.getCardAction()+"\n"+ "Prince: " + Prince.getCardAction()+"\n"+ "King: " +King.getCardAction()+"\n"+ "Countess: " +Countess.getCardAction()+"\n"+"Princess: " + Princess.getCardAction());
                    break;
                case "#history":
                    return new GameEvent(GameEvent.GameEventType.POSTHISTORY, " " + history);
                    break;
            }
        }catch (NullPointerException e)
            {
                System.out.print("Not a valid command.");
            }
    }

    public ArrayList<GameEvent> playCard(User user, GameAction action) {
        ArrayList<GameEvent> gameEvents = new ArrayList<>();
        if (getCurrentPlayer().equals(user)) {
            Player player = playersInRound.get(currentPlayer);
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
                        for(Player target: playersInRound){
                            if(target.getName().equalsIgnoreCase(action.getTarget()) && !target.getImmune()){
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
                        for(Player target: playersInRound){
                            if(target.getName().equalsIgnoreCase(action.getTarget()) && !target.getImmune()){
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
        if (deck.size() == 0 || playersInRound.size() == 1) {
            // ROUND IS OVER
            isRoundOver = true;
            for (Player player : roundWinner) {
                player.setScore(player.getScore() + 1);
            }
            if (findGameWinner(gameWinner) != null) {
                // GAME OVER: A Player has the required amount of tokens to win
                isGameOver = true;
                if(roundWinner.size() == 1 && gameWinner.size() == 1){
                    String message = "This round has ended. The winner is " + roundWinner.get(0).getName() + "!\n"
                            + "This game has ended. The winner is " + gameWinner.get(0).getName() + "! Congratulations!";
                    return new GameEvent(GameEvent.GameEventType.GAME_ENDED, message);
                }else if(roundWinner.size() > 1 && gameWinner.size() == 1){
                    String message = "This round has ended. The winner is " + roundWinner.get(0).getName() + " and " + roundWinner.get(1).getName() +"!\n"
                            + "This game has ended. The winner is " + gameWinner.get(0).getName() + "! Congratulations!";
                    return new GameEvent(GameEvent.GameEventType.GAME_ENDED, message);
                }else{
                    String message = "This round has ended. The winner is " + roundWinner.get(0).getName() + " and " + roundWinner.get(1).getName() +"!\n"
                            + "This game has ended. The winners are " + gameWinner.get(0).getName() + " and " + gameWinner.get(1).getName() + "! Congratulations!";
                    return new GameEvent(GameEvent.GameEventType.GAME_ENDED, message);
                }
            } else {
                // Next round can begin
                if(roundWinner.size() == 1){
                    return new GameEvent(GameEvent.GameEventType.ROUND_ENDED, "This round has ended. The winner is " +
                            roundWinner.get(0).getName() + "!");
                }else{
                    return new GameEvent(GameEvent.GameEventType.ROUND_ENDED, "This round has ended. The winners are " +
                        roundWinner.get(0).getName() + " and " + roundWinner.get(1).getName() + "!");

                }

            }
        } else {
            return new GameEvent(GameEvent.GameEventType.ERROR, "The current player ("
                    + player.getName() + ") hasn't discarded their card, yet!");
        }

    }

    /**
     * Used for testing purposes
     * @return
     */
    public Deck getDeck() {
        return deck;
    }

    private ArrayList<Player>  findRoundWinner(ArrayList <Player> roundWinner) {
        roundWinner = new ArrayList<>();
        if (playersInRound.size() == 1) {
            roundWinner.add(playersInRound.get(0));
        } else if (deck.size() == 0 && playersInRound.size() >= 2) {
            if (playersInRound.get(0).getCard1().getCardPoints() > playersInRound.get(1).getCard1().getCardPoints()) {
                roundWinner.add(playersInRound.get(0));
            } else if (playersInRound.get(0).getCard1().getCardPoints() < playersInRound.get(1).getCard1().getCardPoints()) {
                roundWinner.add(playersInRound.get(1));
            } else {
                if (discardedPoints(playersInRound.get(0).getDiscarded(), playersInRound.get(0)) > discardedPoints(playersInRound.get(1).getDiscarded(), playersInRound.get(1))) {
                    roundWinner.add(playersInRound.get(0));
                } else if (discardedPoints(playersInRound.get(0).getDiscarded(), playersInRound.get(0)) < discardedPoints(playersInRound.get(1).getDiscarded(), playersInRound.get(1))) {
                    roundWinner.add(playersInRound.get(1));
                }
                else{
                    roundWinner.add(playersInRound.get(0));
                    roundWinner.add(playersInRound.get(1));
                    }
                }
        }
        return roundWinner;
    }

    private ArrayList<Player> findGameWinner (ArrayList <Player> gameWinner) {
        for (Player player : playersInGame) {
            if (playersInGame.size() == 4 && player.getScore() >= 4) {
                gameWinner.add(player);
            } else if (playersInGame.size() == 3 && player.getScore() >= 5) {
                gameWinner.add(player);
            } else if (playersInGame.size() == 2 && player.getScore() >= 7) {
                gameWinner.add(player);
            }
        }
        return gameWinner;
    }

    public static void initializePlayerOption(){
        playerOption = (ArrayList<Player>) playersInRound.clone();
        for(int i = 0; i < playerOption.size(); i++){
            if(playerOption.get(i).getImmune() == true){
                playerOption.remove(i);
            }
        }
    }

    public void buildTurnQueue (ArrayList<Player> playersInRound) {
        if (playersInRound.get(0).getInGame()){
            Player currentPlayer = playersInRound.get(0);
            playersInRound.remove(0);
            playersInRound.add(currentPlayer);
        }
        else{
            playersInRound.remove(0);
        }
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
}
