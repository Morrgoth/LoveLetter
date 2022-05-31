import bb.love_letter.game.Game;
import bb.love_letter.game.GameEvent;
import bb.love_letter.game.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    Game game;

    @BeforeEach
    void init() {
        game = new Game();
        game.init();
    }

    @Test
    public void testInit() {
        Game newGame = new Game();
        GameEvent gameEvent = newGame.init();
        assertSame(GameEvent.GameEventType.GAME_INITIALIZED, gameEvent.getGameEventType());
        GameEvent secondInit = newGame.init();
        assertSame(GameEvent.GameEventType.ERROR, secondInit.getGameEventType());
        assertEquals("A Game is already active, wait for it to finish!", secondInit.getMessage());
    }

    @Test
    public void testAddPlayer_GameFull() {
        game.addPlayer(new User("alice"));
        game.addPlayer(new User("bob"));
        game.addPlayer(new User("cedric"));
        game.addPlayer(new User("dustin"));
        GameEvent gameEvent = game.addPlayer(new User("earl"));
        assertSame(GameEvent.GameEventType.ERROR, gameEvent.getGameEventType());
        assertEquals("The Game is already full! You cannot join.", gameEvent.getMessage());
    }

    @Test
    public void testAddPlayer_Join() {
        User user = new User("alice");
        GameEvent gameEvent = game.addPlayer(user);
        assertSame(GameEvent.GameEventType.PLAYER_ADDED, gameEvent.getGameEventType());
        assertEquals("alice has joined the Game!", gameEvent.getMessage());
    }

    @Test
    public void testAddPlayer_GameAlreadyStarted() {
        game.addPlayer(new User("alice"));
        game.addPlayer(new User("bob"));
        game.startGame();
        User user = new User("cedric");
        GameEvent gameEvent = game.addPlayer(user);
        assertSame(GameEvent.GameEventType.ERROR, gameEvent.getGameEventType());
        assertEquals("The Game has already started! Wait for the next game to start.",
                gameEvent.getMessage());
    }

    @Test
    public void testStartGame_1Player() {
        game.addPlayer(new User("alice"));
        ArrayList<GameEvent> gameEvents = game.startGame();
        assertEquals(1, gameEvents.size());
        GameEvent gameEvent = gameEvents.get(0);
        assertSame(GameEvent.GameEventType.ERROR, gameEvent.getGameEventType());
        assertEquals("At least 2 Players must be in the lobby for the game to start!", gameEvent.getMessage());
    }

    @Test
    public void testStartGame_2Players() {
        game.addPlayer(new User("alice"));
        game.addPlayer(new User("bob"));
        ArrayList<GameEvent> gameEvents = game.startGame();
        assertSame(GameEvent.GameEventType.GAME_STARTED, gameEvents.get(0).getGameEventType());
        assertEquals("A new game has started!", gameEvents.get(0).getMessage());
    }

    @Test
    public void testStartGame_AlreadyStarted() {
        game.addPlayer(new User("alice"));
        game.addPlayer(new User("bob"));
        game.startGame();
        ArrayList<GameEvent> gameEvents = game.startGame();
        assertEquals(1, gameEvents.size());
        GameEvent gameEvent = gameEvents.get(0);
        assertSame(GameEvent.GameEventType.ERROR, gameEvent.getGameEventType());
        assertEquals("A Game has already started, wait for it to end!", gameEvent.getMessage());
    }

    @Test
    public void testStartRound_2Players() {
        User user1 = new User("alice");
        game.addPlayer(user1);
        User user2 = new User("bob");
        game.addPlayer(user2);
        game.startGame();
        ArrayList<GameEvent> gameEvents = game.startRound();
        assertEquals(2, gameEvents.size());
        assertSame(GameEvent.GameEventType.DISCARD_NOTIFICATION, gameEvents.get(0).getGameEventType());
        assertEquals(10, game.getDeck().getDeck().size());
        assertNotNull(game.getPlayerQueue().getPlayersInRound().get(0).getCard1());
        assertNotNull(game.getPlayerQueue().getPlayersInRound().get(1).getCard1());
        assertSame(GameEvent.GameEventType.ROUND_STARTED, gameEvents.get(1).getGameEventType());
        assertEquals("A new round has started!", gameEvents.get(1).getMessage());
    }

    @Test
    public void testStartRound_3Players() {
        User user1 = new User("alice");
        game.addPlayer(user1);
        User user2 = new User("bob");
        game.addPlayer(user2);
        User user3 = new User("cedric");
        game.addPlayer(user3);
        game.startGame();
        ArrayList<GameEvent> gameEvents = game.startRound();
        assertEquals(1, gameEvents.size());
        assertEquals(12, game.getDeck().getDeck().size());
        assertNotNull(game.getPlayerQueue().getPlayersInRound().get(0).getCard1());
        assertNotNull(game.getPlayerQueue().getPlayersInRound().get(1).getCard1());
        assertNotNull(game.getPlayerQueue().getPlayersInRound().get(2).getCard1());
        assertSame(GameEvent.GameEventType.ROUND_STARTED, gameEvents.get(0).getGameEventType());
        assertEquals("A new round has started!", gameEvents.get(0).getMessage());
    }

    @Test void testStartRound_RoundNotOver() {
        game.addPlayer(new User("alice"));
        game.addPlayer(new User("bob"));
        game.startGame();
        game.startRound();
        ArrayList<GameEvent> gameEvents = game.startRound();
        assertEquals(1, gameEvents.size());
        assertSame(GameEvent.GameEventType.ERROR, gameEvents.get(0).getGameEventType());
        assertEquals("The current round hasn't ended yet!", gameEvents.get(0).getMessage());
    }

    @Test
    public void testStartTurn() {
        game.addPlayer(new User("alice"));
        game.addPlayer(new User("bob"));
        game.startGame();
        game.startRound();
        ArrayList<GameEvent> gameEvents = game.startTurn();
        assertEquals(4, gameEvents.size());
        assertNotNull(game.getPlayerQueue().getPlayersInRound().get(0).getCard1());
        assertNotNull(game.getPlayerQueue().getPlayersInRound().get(0).getCard2());
        assertFalse(game.getPlayerQueue().getPlayersInRound().get(0).isImmune());
        assertSame(GameEvent.GameEventType.TURN_STARTED, gameEvents.get(0).getGameEventType());
        assertEquals("The turn of alice has started!", gameEvents.get(0).getMessage());
        assertSame(GameEvent.GameEventType.CARD_ADDED, gameEvents.get(1).getGameEventType());
    }

    @Test
    public void testStartTurn_NotYetOver() {
        game.addPlayer(new User("alice"));
        game.addPlayer(new User("bob"));
        game.startGame();
        game.startRound();
        game.startTurn();
        ArrayList<GameEvent> gameEvents = game.startTurn();
        assertEquals(1, gameEvents.size());
        assertSame(GameEvent.GameEventType.ERROR, gameEvents.get(0).getGameEventType());
        assertEquals("The current turn is not yet over!", gameEvents.get(0).getMessage());
    }

    @Test
    public void testFinishTurn_TurnOver() {
        game.addPlayer(new User("alice"));
        game.addPlayer(new User("bob"));
        game.startGame();
        game.startRound();
        game.startTurn();
        game.endTurn();
        GameEvent gameEvent = game.finishTurn();
        assertSame(GameEvent.GameEventType.TURN_ENDED, gameEvent.getGameEventType());
        assertEquals("The turn of alice ended!", gameEvent.getMessage());
    }

    @Test
    public void testFinishTurn_TurnNotOver() {
        game.addPlayer(new User("alice"));
        game.addPlayer(new User("bob"));
        game.startGame();
        game.startRound();
        game.startTurn();
        GameEvent gameEvent = game.finishTurn();
        assertSame(GameEvent.GameEventType.ERROR, gameEvent.getGameEventType());
        assertEquals("The current player (alice) hasn't discarded their card, yet!", gameEvent.getMessage());
    }

    @Test
    public void testEliminatePlayer_CurrentPlayer() {
        game.addPlayer(new User("alice"));
        game.addPlayer(new User("bob"));
        game.addPlayer(new User("cedric"));
        game.startGame();
        game.startRound();
        game.startTurn();
        game.getPlayerQueue().getCurrentPlayer().eliminate();
        game.endTurn();
        GameEvent gameEvent = game.finishTurn();
        assertSame(GameEvent.GameEventType.TURN_ENDED, gameEvent.getGameEventType());
        assertEquals("The turn of alice ended!", gameEvent.getMessage());
    }

    @Test
    public void testEliminatePlayer_OtherPlayer() {
        game.addPlayer(new User("alice"));
        game.addPlayer(new User("bob"));
        game.addPlayer(new User("cedric"));
        game.startGame();
        game.startRound();
        game.startTurn();
        game.getPlayerQueue().getPlayersInRound().get(1).eliminate();
        game.endTurn();
        GameEvent gameEvent = game.finishTurn();
        assertSame(GameEvent.GameEventType.TURN_ENDED, gameEvent.getGameEventType());
        assertEquals("The turn of alice ended!", gameEvent.getMessage());
        ArrayList<GameEvent> nextTurnEvents = game.startTurn();
        assertSame(GameEvent.GameEventType.TURN_STARTED, nextTurnEvents.get(0).getGameEventType());
        assertEquals("The turn of cedric has started!", nextTurnEvents.get(0).getMessage());
        assertSame(GameEvent.GameEventType.CARD_ADDED, nextTurnEvents.get(1).getGameEventType());
    }

    @Test
    public void testFinishTurn_RoundOver_1() {
        game.addPlayer(new User("alice"));
        game.addPlayer(new User("bob"));
        game.startGame();
        game.startRound();
        game.startTurn();
        game.getPlayerQueue().getCurrentPlayer().eliminate();
        game.endTurn();
        GameEvent gameEvent = game.finishTurn();
        assertSame(GameEvent.GameEventType.ROUND_ENDED, gameEvent.getGameEventType());
        assertEquals("This round has ended. The winner is bob!", gameEvent.getMessage());
    }

    @Test
    public void testFinishTurn_RoundOver_2() {
        game.addPlayer(new User("alice"));
        game.addPlayer(new User("bob"));
        game.startGame();
        game.startRound();
        game.startTurn();
        game.getPlayerQueue().getPlayersInRound().get(1).eliminate();
        game.endTurn();
        GameEvent gameEvent = game.finishTurn();
        assertSame(GameEvent.GameEventType.ROUND_ENDED, gameEvent.getGameEventType());
        assertEquals("This round has ended. The winner is alice!", gameEvent.getMessage());
        ArrayList<GameEvent> newRound = game.startRound();
        assertEquals(2, newRound.size());
        assertNotNull(game.getPlayerQueue().getPlayersInRound().get(0).getCard1());
        ArrayList<GameEvent> turn = game.startTurn();
        assertSame(GameEvent.GameEventType.TURN_STARTED, turn.get(0).getGameEventType());
        assertEquals("The turn of alice has started!", turn.get(0).getMessage());
        assertSame(GameEvent.GameEventType.CARD_ADDED, turn.get(1).getGameEventType());
    }

    @Test
    public void testFinishTurn_GameOver() {
        game.addPlayer(new User("alice"));
        game.addPlayer(new User("bob"));
        game.startGame();
        game.startRound();
        game.startTurn();
        game.getPlayerQueue().getCurrentPlayer().setScore(6);
        game.getPlayerQueue().getPlayersInRound().get(1).eliminate();
        game.endTurn();
        GameEvent gameEvent = game.finishTurn();
        assertSame(GameEvent.GameEventType.GAME_ENDED, gameEvent.getGameEventType());
        assertEquals("This round has ended. The winner is alice!\nThis game has ended. " +
                "The winner is alice! Congratulations!", gameEvent.getMessage());
    }
}
