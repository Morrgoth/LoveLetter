package bb.love_letter.game;

import bb.love_letter.game.characters.Cards;

import java.util.ArrayList;

public class Game {
    private final Deck deck;
    private final PlayerQueue playerQueue;
    private boolean isGameStarted;
    private boolean isGameOver;
    private boolean isRoundOver;
    private boolean isTurnOver;

    public Game() {
        deck = new Deck();
        playerQueue = new PlayerQueue();
        isGameOver = true;
        isGameStarted = true;
    }

    public GameEvent init() {
        if (isGameOver && isGameStarted) {
            deck.reset();
            playerQueue.clear();
            isGameOver = false;
            isGameStarted = false;
            isRoundOver = true;
            isTurnOver = true;
            return new GameEvent(GameEvent.GameEventType.GAME_INITIALIZED); // TODO: print the available commands
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
        if (isTurnOver) {
            isTurnOver = false;
            Player player = playerQueue.getCurrentPlayer();
            Cards card = deck.draw();
            player.addCard(card);
            player.setImmune(false);
            gameEvents.add(new GameEvent(GameEvent.GameEventType.TURN_STARTED, "The turn of " + player.getName()
                    + " has started!"));
            gameEvents.add(new GameEvent(GameEvent.GameEventType.CARD_ADDED, "You drew a " + card.getCardName() +
                    ".\n Your current hand is: \n" + player.printHand(), player));
        } else {
            gameEvents.add(new GameEvent(GameEvent.GameEventType.ERROR, "The current turn is not yet over!"));
        }
        return gameEvents;
    }

    public ArrayList<GameEvent> playCard(User user, GameAction action) {
        ArrayList<GameEvent> gameEvents = new ArrayList<>();
        if (playerQueue.getCurrentPlayer().equals(user)) {
            Player player = playerQueue.getCurrentPlayer();
            // TODO: Countess check - Anti-cheat clause -> automatically returns VALID_ACTION

            // TODO: Check if the action is valid or invalid -> return either VALID_ACTION or INVALID_ACTION, if valid change the game state and apply effects
            return null;
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
                Player roundWinner = playerQueue.findRoundWinner();
                roundWinner.setScore(roundWinner.getScore() + 1);
                if (playerQueue.findGameWinner() != null) {
                    // GAME OVER: A Player has at least 4 tokens
                    Player gameWinner = playerQueue.findGameWinner();
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
                playerQueue.setCurrentPlayerToNext();
                return new GameEvent(GameEvent.GameEventType.TURN_ENDED, "The turn of " +
                        player.getName() + " ended!");
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

    /**
     * Used for testing purposes
     * @return
     */
    public PlayerQueue getPlayerQueue() {
        return playerQueue;
    }
}
