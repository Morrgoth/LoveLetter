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
            if (!player.isEliminated()) {
                uneliminatedPlayerCount += 1;
            }
        }
        return uneliminatedPlayerCount;
    }

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
