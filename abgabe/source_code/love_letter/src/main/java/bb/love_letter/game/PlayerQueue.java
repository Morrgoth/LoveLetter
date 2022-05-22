package bb.love_letter.game;

import java.util.ArrayList;

/**
 *
 * @author Bence Ament
 */
public class PlayerQueue {
    ArrayList<Player> players;
    private final int PLAYER_LIMIT = 4;

    private int currentPlayerIndex;

    public PlayerQueue() {
        players = new ArrayList<>();
        currentPlayerIndex = 0;
    }

    /**
     * Adds a new User to the PlayerQueue and handles possible errors
     * @param user The User to be added
     * @return Notifications and Errors to be forwarded to the Server
     */
    public GameEvent addPlayer(User user) {
        if (getPlayerCount() < PLAYER_LIMIT) {
            if (getPlayerByName(user.getName()) == null) {
                players.add(new Player(user));
                return new GameEvent(GameEvent.GameEventType.PLAYER_ADDED, user.getName() + " has joined the Game!");
            } else {
                return new GameEvent(GameEvent.GameEventType.ERROR, " You have already joined the Game!", user);
            }
        } else {
            return new GameEvent(GameEvent.GameEventType.ERROR, "The Game is already full! You cannot join.", user);
        }
    }

    public int getPlayerCount() {
        return players.size();
    }

    /**
     * @return the number of Player who have not yet been eliminated from the Round
     */
    public int getPlayersInRoundCount() {
        int uneliminatedPlayerCount = 0;
        for (Player player: players) {
            if (player.getInGame()) {
                uneliminatedPlayerCount += 1;
            }
        }
        return uneliminatedPlayerCount;
    }

    /**
     * Prepares the PlayerQueue for a new Round
     */
    public void resetRound() {
        for (Player player: players) {
            player.setCard1(null);
            player.setCard2(null);
            player.setInGame(true);
        }
    }

    public void clear() {
        players.clear();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * After a Turn is over this method finds the next Player who is not eliminated
     */
    public void setCurrentPlayerToNext() {
        currentPlayerIndex = (currentPlayerIndex + 1) % getPlayerCount();
        while (!players.get(currentPlayerIndex).getInGame()) {
            currentPlayerIndex = (currentPlayerIndex + 1) % getPlayerCount();
        }
    }

    /**
     * @param name
     * @return The Player with the provided name or null
     */
    public Player getPlayerByName(String name) {
        for (Player player: players) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    /**
     * @return List of the Players who are currently not immune
     */
    public boolean existsChoosablePlayer() {
        for (Player player: players) {
            if (!player.getName().equals(getCurrentPlayer().getName()) && !player.isImmune() && player.getInGame()) {
                return true;
            }
        }
        return false;
    }


    /**
     * @return List of the Players who have not yet been eliminated from the Round
     */
    public ArrayList<Player> getPlayersInRound() {
        ArrayList<Player> playersInRound = new ArrayList<>();
        for (Player player: players) {
            if (player.getInGame()) {
                playersInRound.add(player);
            }
        }
        return playersInRound;
    }

    /**
     * Used if the user logs out of the application.
     */
    public boolean removePlayer(User user) {
        return players.removeIf(player -> player.getName().equals(user.getName()));
    }

    /**
     * @return The scores of the Players who are in the game.
     */
    public String printScores() {
        StringBuilder scores = new StringBuilder();
        for (Player player: players) {
            scores.append(player.getName())
                    .append(": ")
                    .append(player.getScore())
                    .append("\n");
        }
        return scores.toString();
    }

    /**
     * The currentPlayerIndex is set to the index of the winner of the previous Round so that they start the next Round
     * @param winner The winner of the previous Round
     */
    public void setWinnerIndex(Player winner) {
        currentPlayerIndex = players.indexOf(winner);
    }

}
