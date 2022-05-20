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

    public int getPlayerCount() {
        return players.size();
    }

    public int getPlayersInRoundCount() {
        int uneliminatedPlayerCount = 0;
        for (Player player: players) {
            if (player.getInGame()) {
                uneliminatedPlayerCount += 1;
            }
        }
        return uneliminatedPlayerCount;
    }

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

    public void setCurrentPlayerToNext() {
        currentPlayerIndex = (currentPlayerIndex + 1) % getPlayerCount();
        while (!players.get(currentPlayerIndex).getInGame()) {
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

    public Player getChoosablePlayerByName(String name) {
        for (Player player: players) {
            if (player.getName().equals(name) && !player.isImmune() && player.getInGame()) {
                return player;
            }
        }
        return null;
    }


    public ArrayList<Player> getPlayersInRound() {
        ArrayList<Player> playersInRound = new ArrayList<>();
        for (Player player: players) {
            if (player.getInGame()) {
                playersInRound.add(player);
            }
        }
        return playersInRound;
    }

}
