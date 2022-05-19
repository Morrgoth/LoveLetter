package bb.love_letter.game;

import bb.love_letter.game.characters.Baron;
import bb.love_letter.game.characters.Cards;
import bb.love_letter.game.characters.Countess;
import bb.love_letter.game.characters.Guard;

import java.util.ArrayList;

public class Game {
    private Deck deck;
    private ArrayList<Player> playersInGame;
    private ArrayList<Player> playersInRound;
    private int currentPlayer;
    private ArrayList<Player> roundWinner;
    private ArrayList <Player> gameWinner;
    private boolean isGameStarted;
    private boolean isGameOver;
    private boolean isRoundOver;

    //list of all cards played in the round
    public static ArrayList<Cards> history;

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
        player.addCard(card);
        player.setImmune(false);
        gameEvents.add(new GameEvent(GameEvent.GameEventType.TURN_STARTED, "The turn of " + player.getName()
                + " started!"));
        gameEvents.add(new GameEvent(GameEvent.GameEventType.CARD_ADDED, "You drew a " + card.getCardName() +
                ".\n Your current hand is: \n" + player.printHand(), player));
        return gameEvents;
    }

    public ArrayList<GameEvent> playCard(User user, GameAction action) {
        ArrayList<GameEvent> gameEvents = new ArrayList<>();
        if (getCurrentPlayer().equals(user)) {
            Player player = playersInRound.get(currentPlayer);
            // TODO: Countess check - Anti-cheat clause -> automatically returns VALID_ACTION
            switch(action.getCardIndex()){
                case 1:
                    Cards card1 = player.getCard1();
                    //Firstly, check if it is the effect of COUNTESS
                    if(player.checkIfCountess(player.getCard1(), player.getCard2())){
                        if(card1 instanceof Countess){
                            player.discardCard(1);
                            gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() + " discarded " + player.getCard1().getCardName() + "."));
                        }else{
                            gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION, "You can only discard COUNTESS.", player));
                            player.discardCard(2);
                        }
                    }//Exclude the effect of COUNTESS
                    else{
                        Player targetPlayer = null;
                        if(card1 instanceof Baron){
                            for(Player target: playersInRound){
                                if(target.getName().equals(action.getTarget())){
                                    targetPlayer = target;
                                }
                            }
                            if(targetPlayer == null){
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.INVALID_ACTION, "Please choose a target player."));
                            }else{
                                gameEvents.add(new GameEvent(GameEvent.GameEventType.VALID_ACTION, player.getName() + " discarded " + player.getCard1().getCardName() + "."));
                                gameEvents.add(((Baron) card1).useBaron(player, targetPlayer));
                            }
                        }else if(card1 instanceof Guard){

                        }
                    }
            }

            // TODO: Check if the action is valid or invalid -> return either VALID_ACTION or INVALID_ACTION, if valid change the game state and apply effects
            return null;
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
    }
