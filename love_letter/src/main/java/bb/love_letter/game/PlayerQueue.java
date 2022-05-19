package bb.love_letter.game;

import java.util.ArrayList;

public class PlayerQueue {
    ArrayList<Player> players;
    private final int PLAYER_LIMIT = 4;

    private int currentPlayerIndex;

    public PlayerQueue() {
        players = new ArrayList<>();
        currentPlayerIndex = 0;
    }

    public GameEvent addPlayer(User user) {
        if (getPlayerCount() < PLAYER_LIMIT) {
            players.add(new Player(user));
            return new GameEvent(GameEvent.GameEventType.PLAYER_ADDED, user.getName() + " has joined the Game!");
        } else {
            return new GameEvent(GameEvent.GameEventType.ERROR, "The Game is already full! You cannot join.");
        }
    }
/*
    public int getPlayerCount() {
        return players.size();
    }

    public int getPlayersInRoundCount() {
        int uneliminatedPlayerCount = 0;
        for (Player player: players) {
            if (!player.isEliminated()) {
                uneliminatedPlayerCount += 1;
            }
        }
        return uneliminatedPlayerCount;
    }
*/
    public void resetRound() {
        for (Player player: players) {
            player.setCard1(null);
            player.setCard2(null);
            player.setEliminated(false);
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

    public void setCurrentPlayerToNext() {
        currentPlayerIndex = (currentPlayerIndex + 1) % getPlayerCount();
        while (players.get(currentPlayerIndex).isEliminated()) {
            currentPlayerIndex = (currentPlayerIndex + 1) % getPlayerCount();
        }
    }

    public Player getPlayerByName(String name) {
        for (Player player: players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    public Player findRoundWinner() {
        if (getPlayersInRoundCount() == 1) {
            return getPlayersInRound().get(0);
        } else {
            // TODO: Find the winner if the deck is empty and there are at least 2 players still in the round
            return null;
        }
    }

    public Player findGameWinner() {
        for (Player player: players) {
            if (getPlayerCount() == 4 && player.getScore() >= 4) {
                return player;
            }else if(getPlayerCount() == 3 && player.getScore() >= 5){
                return player;
            }else if (getPlayerCount() == 2 && player.getScore() >= 7){
                return player;
            }
        }
        return null;
    }

    public ArrayList<Player> getPlayersInRound() {
        ArrayList<Player> playersInRound = new ArrayList<>();
        for (Player player: players) {
            if (!player.isEliminated()) {
                playersInRound.add(player);
            }
        }
        return playersInRound;
    }

}
