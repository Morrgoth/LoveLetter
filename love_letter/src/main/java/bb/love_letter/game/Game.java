package bb.love_letter.game;

import bb.love_letter.game.characters.Cards;

import java.util.ArrayList;

public class Game {
    private Deck deck;
    private ArrayList<Player> playersInGame;
    private ArrayList<Player> playersInRound;
    private int currentPlayer;
    private Player roundWinner;
    private Player gameWinner;
    private boolean isGameStarted;
    private boolean isGameOver;
    private boolean isRoundOver;

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
            return new GameEvent(GameEvent.GameEventType.GAME_INITIALIZED); // After this the Server will publish the possible commands
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
        gameEvents.add(new GameEvent(GameEvent.GameEventType.TURN_STARTED, "The turn of " + player.getName()
                + " started!"));
        gameEvents.add(new GameEvent(GameEvent.GameEventType.CARD_ADDED, "You drew a " + card.getCardName() +
                ".\n Your current hand is: \n" + player.printHand(), true));
        return gameEvents;
    }

    public GameEvent playCard(User user, GameAction action) {
        if (getCurrentPlayer().equals(user)) {
            Player player = playersInRound.get(currentPlayer);
            // TODO: Countess check - Anti-cheat clause -> automatically returns VALID_ACTION

            // TODO: Check if the action is valid or invalid -> return either VALID_ACTION or INVALID_ACTION, if valid change the game state and apply effects
            return null;
        } else {
            return new GameEvent(GameEvent.GameEventType.ERROR, "It is not your turn. It is the turn of " +
                    playersInRound.get(currentPlayer) + "!", true);
        }
    }

    private GameEvent finishTurn() {
        if (deck.size() == 0 || playersInRound.size() == 1) {
            // ROUND IS OVER
            isRoundOver = true;
            roundWinner = findRoundWinner();
            roundWinner.setScore(roundWinner.getScore() + 1);
            if (findGameWinner() != null) {
                // GAME OVER: A Player has at least 4 tokens
                gameWinner = findGameWinner();
                isGameOver = true;
                String message = "This round has ended. The winner is " + roundWinner.getName() + "!\n"
                        + "This game has ended. The winner is " + gameWinner.getName() + "! Congratulations!";
                return new GameEvent(GameEvent.GameEventType.GAME_ENDED, message);
            } else {
                // Next round can begin
                return new GameEvent(GameEvent.GameEventType.ROUND_ENDED, "This round has ended. The winner is " +
                        roundWinner.getName() + "!");
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

    private Player findRoundWinner() {
        if (playersInRound.size() == 1) {
            return playersInRound.get(0);
        } else {
            // Find the winner if the deck is empty and there are at least 2 players still in the round
            return null;
        }
    }

    private Player findGameWinner() {
        for (Player player: playersInGame) {
            if (player.getScore() >= 4) {
                return player;
            }
        }
        return null;
    }
}
