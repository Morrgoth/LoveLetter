package bb.love_letter.game;

import bb.love_letter.game.characters.*;

import java.time.temporal.ValueRange;
import java.util.ArrayList;

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

    //list of all cards played in the round
    public static ArrayList<Cards> history;
    //list of players in the round that are not immune and can be choosen for a cardEffect
    public static ArrayList<Player> playerOption = new ArrayList<>();
    //list of current players still in the round

    public Game() {
        deck = new Deck();
        playersInGame = new ArrayList<>();
        playersInRound = new ArrayList<>();
        roundWinner = null;
        gameWinner = null;
        isGameOver = false;
        isGameStarted = false;
        isRoundOver = false;
    }

    public GameEvent init() {
        if (isGameOver && isGameStarted) {
            deck.reset();
            playersInGame.clear();
            playersInRound.clear();
            roundWinner = null;
            gameWinner = null;
            isGameOver = false;
            isGameStarted = false;
            isRoundOver = false;
            return new GameEvent(GameEvent.GameEventType.GAME_INITIALIZED); // TODO: print the available commands
        } else {
            return new GameEvent(GameEvent.GameEventType.ERROR, "A Game is already active, wait for it to finish!");
        }
    }

    public GameEvent addPlayer(User user) {
        if (isGameStarted) {
            return new GameEvent(GameEvent.GameEventType.ERROR, "The Game has already started! Wait for the next" +
                    " game to start.");
        } else if (playersInGame.size() >= 4) {
            return new GameEvent(GameEvent.GameEventType.ERROR, "The Game is already full! You cannot join.");
        } else {
            playersInGame.add((new Player(user)));
            return new GameEvent(GameEvent.GameEventType.PLAYER_ADDED, user.getName() + " has joined the Game!");
        }
    }

    public GameEvent startGame() {
        isGameStarted = true;
        return new GameEvent(GameEvent.GameEventType.GAME_STARTED, "A new game has started!");
    }

    public GameEvent startRound() {
        if (isRoundOver) {
            deck.reset();
            playersInRound.clear();
            playersInRound.addAll(playersInGame);
            currentPlayer = 0;
            return new GameEvent(GameEvent.GameEventType.ROUND_STARTED, "A new round has started!");
        } else {
            return new GameEvent(GameEvent.GameEventType.ERROR, "The current round hasn't ended yet!");
        }
    }

    public ArrayList<GameEvent> startTurn() {
        ArrayList<GameEvent> gameEvents = new ArrayList<>();
        Player player = playersInRound.get(currentPlayer);
        Cards card = deck.draw();
        Game.initializePlayerOption();
        player.addCard(card);
        player.setImmune(false);
        gameEvents.add(new GameEvent(GameEvent.GameEventType.TURN_STARTED, "The turn of " + player.getName()
                + " started!"));
        gameEvents.add(new GameEvent(GameEvent.GameEventType.CARD_ADDED, "You drew a " + card.getCardName() +
                ".\n The effect of the card is: " + card.getCardAction() + "\n Your current hand is: \n" +
                player.printHand() + ".\n The effect of the card is: " + player.getCard1().getCardAction(), player));
        return gameEvents;
    }

    public ArrayList<GameEvent> playCard(User user, GameAction action) {
        ArrayList<GameEvent> gameEvents = new ArrayList<>();
        if (getCurrentPlayer().equals(user)) {
            Player player = playersInRound.get(currentPlayer);
            switch(action.getCardIndex()){
                case 1:
                    Cards card1 = player.getCard1();
                    //Check if the card is PRINCESS
                    if(player.checkIfPrincess(card1)){
                        player.discardCard(1);
                        gameEvents.add(new GameEvent(GameEvent.GameEventType.PLAYER_EFFECT, player.getName() + " discarded PRINCESS."));
                        player.setInGame(false);
                        gameEvents.add(new GameEvent(GameEvent.GameEventType.PLAYERELIMINATED, player.getName()
                                + " is out because of discarding the PRINCESS."));
                    }//Check if the player has COUNTESS and PRINCE or KING at the same time
                    else if(player.checkIfCountess(player.getCard1(), player.getCard2())){
                        if(card1 instanceof Countess){
                            player.discardCard(1);
                            gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() + " discarded COUNTESS."));
                        }else{
                            gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION, "You can only discard COUNTESS.", player));
                            player.discardCard(2);
                            gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() + " discarded COUNTESS."));
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
                                        "Please choose a (valid) target player."));
                            }else{
                                player.discardCard(1);
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION,
                                        player.getName() + " discarded BARON."));
                                gameEvents.add(((Baron) card1).useBaron(player, targetPlayer));
                            }
                        }else if(card1 instanceof Guard){
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                        "Please choose a (valid) target player."));
                            }else{
                                player.discardCard(1);
                                gameEvents.add(((Guard) card1).useGuard(player, targetPlayer, action.getGuess()));
                            }
                        }else if(card1 instanceof Handmaid){
                            player.discardCard(1);
                            gameEvents.add(((Handmaid) card1).useHandmaid(player));
                        }else if(card1 instanceof King){
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                        "Please choose a (valid) target player."));
                            }else{
                                player.discardCard(1);
                                gameEvents.add(((King) card1).useKing(player, targetPlayer));
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.PLAYER_EFFECT, "You get the card "
                                        + player.getCard1().getCardName() + " from " + targetPlayer.getName()));
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.PLAYER_EFFECT, "You get the card "
                                        + targetPlayer.getCard1().getCardName() + " from " + player.getName()));
                            }
                        }else if(card1 instanceof Prince){
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                        "Please choose a (valid) target player."));
                            }else{
                                player.discardCard(1);
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() +
                                        " discarded PRINCE on " + targetPlayer.getName() + "."));
                                gameEvents.add(((Prince) card1).usePrince(player, targetPlayer, deck));
                            }

                        }else if(card1 instanceof Priest){
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                        "Please choose a (valid) target player."));
                            }else{
                                player.discardCard(1);
                                gameEvents.add(((Priest) card1).usePriest(player, targetPlayer));
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName()
                                        + " discarded PRIEST and saw the hand of " + targetPlayer.getName()));
                            }
                        }
                    }
                    break;

                case 2:
                    Cards card2 = player.getCard2();
                    //Check if the card is PRINCESS
                    if(player.checkIfPrincess(card2)){
                        player.discardCard(2);
                        gameEvents.add(new GameEvent(GameEvent.GameEventType.PLAYER_EFFECT, player.getName() + " discarded PRINCESS."));
                        player.setInGame(false);
                        gameEvents.add(new GameEvent(GameEvent.GameEventType.PLAYERELIMINATED, player.getName()
                                + " is out because of discarding the PRINCESS."));
                    }//Check if the player has COUNTESS and PRINCE or KING at the same time
                    else if(player.checkIfCountess(player.getCard1(), player.getCard2())){
                        if(card2 instanceof Countess){
                            player.discardCard(2);
                            gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() + " discarded COUNTESS."));
                        }else{
                            gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION, "You can only discard COUNTESS.", player));
                            player.discardCard(1);
                            gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() + " discarded COUNTESS."));
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
                                        "Please choose a (valid) target player."));
                            }else{
                                player.discardCard(2);
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION,
                                        player.getName() + " discarded BARON."));
                                gameEvents.add(((Baron) card2).useBaron(player, targetPlayer));
                            }
                        }else if(card2 instanceof Guard){
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                        "Please choose a (valid) target player."));
                            }else{
                                player.discardCard(2);
                                gameEvents.add(((Guard) card2).useGuard(player, targetPlayer, action.getGuess()));
                            }
                        }else if(card2 instanceof Handmaid){
                            player.discardCard(2);
                            gameEvents.add(((Handmaid) card2).useHandmaid(player));
                        }else if(card2 instanceof King){
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                        "Please choose a (valid) target player."));
                            }else{
                                player.discardCard(2);
                                gameEvents.add(((King) card2).useKing(player, targetPlayer));
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, "You get the card "
                                        + player.getCard1().getCardName() + " from " + targetPlayer.getName()));
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, "You get the card "
                                        + targetPlayer.getCard1().getCardName() + " from " + player.getName()));
                            }
                        }else if(card2 instanceof Prince){
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                        "Please choose a (valid) target player."));
                            }else{
                                player.discardCard(2);
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() +
                                        " discarded PRINCE on " + targetPlayer.getName() + "."));
                                gameEvents.add(((Prince) card2).usePrince(player, targetPlayer, deck));
                            }

                        }else if(card2 instanceof Priest){
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION,
                                        "Please choose a (valid) target player."));
                            }else{
                                player.discardCard(2);
                                gameEvents.add(((Priest) card2).usePriest(player, targetPlayer));
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName()
                                        + " discarded PRIEST and saw the hand of " + targetPlayer.getName()));
                            }
                        }
                    }
                    break;
                default:
                    gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION, "Please enter a (valid) card index: 1 or 2.", player));
            }
        } else {
            gameEvents.add(new GameEvent(GameEvent.GameEventType.ERROR, "It is not your turn. It is the turn of " +
                    playersInRound.get(currentPlayer) + "!", user));
        }
        return gameEvents;
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
            // ROUND IS NOT YET OVER
            return new GameEvent(GameEvent.GameEventType.TURN_ENDED, "The turn of " +
                    playersInRound.get(currentPlayer++).getName() + " ended!");
        }

    }

    private User getCurrentPlayer() {
        return (User) playersInRound.get(currentPlayer);
    }

    private void eliminatePlayer(Player player) {
        if (player.equals(playersInRound.get(currentPlayer))) {
            currentPlayer -= 1;
        }
        playersInRound.remove(player);
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
                if (playersInRound.get(0).discardedPoints(playersInRound.get(0).discarded) > playersInRound.get(1).discardedPoints(playersInRound.get(1).discarded)) {
                    roundWinner.add(playersInRound.get(0));
                } else if (playersInRound.get(0).discardedPoints(playersInRound.get(0).discarded) < playersInRound.get(1).discardedPoints(playersInRound.get(1).discarded)) {
                    roundWinner.add(playersInRound.get(1));
                }
                else{
                    roundWinner.add(playersInRound.get(0));
                    roundWinner.add(playersInRound.get(1));
                    }
                }
            // TODO: Find the winner if the deck is empty and there are at least 2 players still in the round

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


    //player's action - draw the top most  card from deck
    public void drawCard(Player player) {

        player.setCard2(deck.getDeck().get(0));;
        deck.getDeck().remove(0);
    }

    public int discardedPoints(ArrayList<Cards> discarded) {
        int sum = 0;
        for (Cards card : discarded) {
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

    public boolean checkIfPrincess(Cards card) {
        if (card.getCardName().equals("PRINCESS")) {
            return true;
        }
        return false;
    }
    public void addCard(Cards card) {
        if (card1 == null) {
            setCard1(card);
        } else {
            setCard2(card);
        }
    }

    public void discard(int cardIndex) {
        if (cardIndex == 1) {
            setCard1(null);
        } else {
            setCard2(null);
        }
    }
}
