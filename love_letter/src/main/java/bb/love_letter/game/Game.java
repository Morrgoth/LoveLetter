package bb.love_letter.game;

import bb.love_letter.game.characters.*;

import java.util.ArrayList;
import java.util.HashMap;

import static bb.love_letter.game.GameEvent.GameEventType.*;

public class Game {
    private Deck deck;
    private final PlayerQueue playerQueue;
    private boolean isGameStarted;
    private boolean isGameOver;
    private boolean isRoundOver;
    private boolean isTurnOver;
    public static ArrayList<Cards> history;
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
            history.add(extraDiscarded1);
            history.add(extraDiscarded2);
            history.add(extraDiscarded3);
            GameEvent discardNotification = new GameEvent(GameEvent.GameEventType.DISCARD_NOTIFICATION, "The" +
                    " following cards were removed from the deck: " + extraDiscarded1.getCardName() + ", "
                    + extraDiscarded2.getCardName() + ", " + extraDiscarded3.getCardName());
            return discardNotification;
        }
        return null;
    }
    public GameEvent init() {
        System.out.println(isGameOver);
        System.out.println(isGameStarted);
        if (isGameOver && isGameStarted) {
            deck.reset();
            playerQueue.clear();
            isGameOver = false;
            isGameStarted = false;
            isRoundOver = true;
            isTurnOver = true;
            history.clear();
            return new GameEvent(GameEvent.GameEventType.GAME_INITIALIZED,"The Game was successfully initialized. " +
                    "Use #help to see more information.");
        } else {
            return new GameEvent(GameEvent.GameEventType.ERROR, "A Game is already active, wait for it to finish!");
        }
    }

    public GameEvent addPlayer(User user) {
        if (isGameOver) {
            return new GameEvent(GameEvent.GameEventType.ERROR, "The Game has not yet been created! You can " +
                    "initialize a new game with #create!", user);
        } else if (isGameStarted) {
            return new GameEvent(GameEvent.GameEventType.ERROR, "The Game has already started! Wait for the next" +
                    " game to start.", user);
        } else {
            return playerQueue.addPlayer(user);
        }
    }

    public ArrayList<GameEvent> startGame() {
        ArrayList<GameEvent> gameEvents = new ArrayList<>();
        if (!isGameStarted) {
            if (playerQueue.getPlayerCount() >= 2) {
                isGameStarted = true;
                gameEvents.add(new GameEvent(GameEvent.GameEventType.GAME_STARTED, "A new game has started!"));
                gameEvents.addAll(startRound());
            } else {
                gameEvents.add(new GameEvent(GameEvent.GameEventType.ERROR, "At least 2 Players must be in the lobby " +
                        "for the game to start!"));
            }
        } else {
            gameEvents.add(new GameEvent(GameEvent.GameEventType.ERROR, "A Game has already started, wait for it to end!"));
        }
        return gameEvents;
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
            gameEvents.addAll(startTurn());
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
                    ".\nYour current hand is: \n" + player.printHand(), ((User) player)));
        } else {
            gameEvents.add(new GameEvent(GameEvent.GameEventType.ERROR, "The current turn is not yet over!"));
        }
        return gameEvents;
    }

    //später nach Server schieben damit es direkt die userCommand aus dem chat holen kann
    public GameEvent getHelp (User user){
        return new GameEvent(GameEvent.GameEventType.INFO, "The following commands are available:\n " +
                "#create: initializes the so Players can join. \n" +
                "#join: join the lobby if the game hasn't started yet. \n" +
                "#start: start the game if there are at least 2 Players in the lobby. \n" +
                "#discard <cardIndex> (<target>) (<guess>): discard card 1 or 2, target and guess are needed for some cards (see (#cards)) \n" +
                "#score: see the current player scores. \n " +
                "#cards: get information, about the distinct card effects.\n " +
                "#hand: check the card(s) you currently have in your hand. \n" +
                "#history: see what cards have been played in this round.", user);
    }

    public GameEvent getScore (User user){
        return new GameEvent(GameEvent.GameEventType.INFO, " " + playerQueue.printScores(), user);
    }

    public GameEvent getCards (User user){
        return new GameEvent(GameEvent.GameEventType.INFO, "Here you can see the type of the card, the value and the cardaction: " + "\n" +
                "Guard (1): " + Guard.getCardAction() +
                "\n" + "Priest (2): " + Priest.getCardAction() +
                "\n" + "Baron (3): " + Baron.getCardAction() +
                "\n"+ "Handmaid (4): " +Handmaid.getCardAction() +
                "\n" + "Prince (5): " + Prince.getCardAction() +
                "\n" + "King (6): " +King.getCardAction() +
                "\n" + "Countess (7): " + Countess.getCardAction() +
                "\n"+"Princess (8): " + Princess.getCardAction(), user);
    }

    public GameEvent getHand (User user){
        String message = "Here is your current Hand: \n" + playerQueue.getPlayerByName(user.getName()).printHand();
        return new GameEvent(INFO, message, user);
    }

    public GameEvent getHistory (User user){
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < history.size(); i++) {
            message.append(i).append(": ")
                    .append(history.get(i).getCardName())
                    .append("\n");
        }
        return new GameEvent(GameEvent.GameEventType.INFO, message.toString(), user);
    }

    public ArrayList<GameEvent> playCard(User user, GameAction action) {
        System.out.println(action.getCardIndex() + " " + action.getTarget() + " " + action.getGuess());
        ArrayList<GameEvent> gameEvents = new ArrayList<>();
        if (!playerQueue.getPlayerByName(user.getName()).getInGame()) {
            gameEvents.add(new GameEvent(INVALID_ACTION, "You are eliminated, wait for the next round to start!", user));
            return gameEvents;
        }
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
            System.out.println(card.getCardName());
            //Check if the card is PRINCESS
            if(checkIfPrincess(card)){
                discardCard(action.getCardIndex(), player);
                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() +
                        " discarded the Princess and was eliminated"));
                player.setInGame(false);
                endTurn();
            }//Check if the player has COUNTESS and PRINCE or KING at the same time
            else if(checkIfCountess(player.getCard1(), player.getCard2())){
                if(player.getCard1() instanceof Countess){
                    discardCard(1, player);
                }else {
                    discardCard(2, player);
                }
                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName()
                        + " discarded the Countess"));
                endTurn();
            }//Discard Countess without King or Prince in hand
            else if(card instanceof Countess){
                discardCard(action.getCardIndex(), player);
                gameEvents.add(new GameEvent(VALID_ACTION, player.getName() + " discarded the Countess"));
                endTurn();
            }
            else if(card instanceof Handmaid){
                discardCard(action.getCardIndex(), player);
                gameEvents.add(((Handmaid) card).useHandmaid(player));
                endTurn();
            }//Exclude the effect of COUNTESS, PRINCESS and HANDMAID
            else{
                if(playerQueue.existsChoosablePlayer()) {
                    Player targetPlayer = playerQueue.getPlayerByName(action.getTarget());
                    if(targetPlayer != null){
                        if(!targetPlayer.isImmune() && targetPlayer.getInGame()){
                            if(card instanceof Baron){
                                discardCard(action.getCardIndex(), player);
                                gameEvents.add(((Baron) card).useBaron(player, targetPlayer));
                                endTurn();
                            }else if(card instanceof Guard){
                                GameEvent gameEvent = ((Guard) card).useGuard(player, targetPlayer, action.getGuess());
                                gameEvents.add(gameEvent);
                                if (gameEvent.getGameEventType() == VALID_ACTION) {
                                    discardCard(action.getCardIndex(), player);
                                    endTurn();
                                }
                            }else if(card instanceof King){
                                discardCard(action.getCardIndex(), player);
                                gameEvents.add(((King) card).useKing(player, targetPlayer));
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.CARD_EFFECT,
                                        "You switched hands with " +
                                                targetPlayer.getName() + ", you got a " + player.getCard1().getCardName(), player));
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.CARD_EFFECT,
                                        "You switched hands with " +
                                                player.getName() + ", you got a " + targetPlayer.getCard1().getCardName(), targetPlayer));
                                endTurn();
                            }else if(card instanceof Prince){
                                discardCard(action.getCardIndex(), player);
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() +
                                        " discarded the Prince and targeted " + targetPlayer.getName()));
                                ArrayList<GameEvent> princeEvent = usePrince(player, targetPlayer);
                                for(int i = 0; i<princeEvent.size(); i++){
                                    gameEvents.add(princeEvent.get(i));
                                }
                                endTurn();
                            }else if(card instanceof Priest){
                                discardCard(action.getCardIndex(), player);
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() +
                                        " discarded the Priest and targeted " + targetPlayer.getName()));
                                gameEvents.add(((Priest) card).usePriest(player, targetPlayer));
                                endTurn();
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
                } else {
                    discardCard(action.getCardIndex(), player);
                    gameEvents.add(new GameEvent(VALID_ACTION, player.getName() + " discarded the " + card.getCardName() +
                            " without action!"));
                    endTurn();
                }
            }

        } else {
            gameEvents.add(new GameEvent(GameEvent.GameEventType.ERROR, "It is not your turn. It is the turn of " +
                    playerQueue.getCurrentPlayer().getName() + "!", user));
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

    private ArrayList<Player> findRoundWinner() {
        ArrayList <Player> roundWinner = new ArrayList<>();
        ArrayList<Player> playersInRound = playerQueue.getPlayersInRound();
        if (playerQueue.getPlayersInRound().size() == 1) {
            roundWinner.add(playerQueue.getPlayersInRound().get(0));
            playerQueue.setWinnerIndex(playerQueue.getPlayersInRound().get(0));
        } else if (deck.size() == 0 && playerQueue.getPlayersInRoundCount() >= 2) {
            if (playersInRound.get(0).getCard1().getCardPoints() > playersInRound.get(1).getCard1().getCardPoints()) {
                roundWinner.add(playersInRound.get(0));
                playerQueue.setWinnerIndex(playerQueue.getPlayersInRound().get(0));
            } else if (playersInRound.get(0).getCard1().getCardPoints() < playersInRound.get(1).getCard1().getCardPoints()) {
                roundWinner.add(playersInRound.get(1));
                playerQueue.setWinnerIndex(playerQueue.getPlayersInRound().get(1));
            } else {
                if (discardedPoints(playersInRound.get(0).getDiscarded(), playersInRound.get(0)) > discardedPoints(playersInRound.get(1).getDiscarded(), playersInRound.get(1))) {
                    roundWinner.add(playersInRound.get(0));
                    playerQueue.setWinnerIndex(playerQueue.getPlayersInRound().get(0));
                } else if (discardedPoints(playersInRound.get(0).getDiscarded(), playersInRound.get(0)) < discardedPoints(playersInRound.get(1).getDiscarded(), playersInRound.get(1))) {
                    roundWinner.add(playersInRound.get(1));
                    playerQueue.setWinnerIndex(playerQueue.getPlayersInRound().get(1));
                } else {
                    roundWinner.add(playersInRound.get(0));
                    roundWinner.add(playersInRound.get(1));
                    playerQueue.setWinnerIndex(playerQueue.getPlayersInRound().get(1));
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
            Cards newCard = deck.draw();
            addCard(newCard, targetPlayer);
            princeEvent.add(new GameEvent(CARD_EFFECT, "You got a " + newCard.getCardName() +
                    " from deck.", targetPlayer));
        }
        return princeEvent;
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


    public void addCard(Cards card, Player player) {
        if (player.getCard1() == null) {
            player.setCard1(card);
        } else {
            player.setCard2(card);
        }
    }

    public GameEvent removeLoggedOutUser(User user) {
        if (playerQueue.getPlayerByName(user.getName()) != null) {
            if (playerQueue.getPlayerCount() == 2) {
                isGameOver = true;
                return new GameEvent(ERROR, user.getName() + " has left. There are not enough Players left, Game Over");
            }
            if (playerQueue.getCurrentPlayer().getName().equals(user.getName())) {
                playerQueue.setCurrentPlayerToNext();
            }
            playerQueue.removePlayer(user);
            return new GameEvent(ERROR, user.getName() + " has left the Game.");
        } else {
            return null;
        }
    }

    public PlayerQueue getPlayerQueue() {
        return playerQueue;
    }
}
