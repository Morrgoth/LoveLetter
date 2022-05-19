import bb.love_letter.game.Game;
import bb.love_letter.game.GameAction;
import bb.love_letter.game.GameEvent;
import bb.love_letter.game.User;
import bb.love_letter.game.characters.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    Game game;

    @BeforeEach
    void init() {
        game = new Game();
        game.init();
        game.addPlayer(new User("alice"));
        game.addPlayer(new User("bob"));
        game.addPlayer(new User("cedric"));
        game.startGame();
        game.startRound();
        game.startTurn();
    }

    @Test
    public void testPrincess() {
        game.getPlayerQueue().getPlayerByName("alice").setCard1(new Princess());
        GameAction gameAction = new GameAction(1);
        ArrayList<GameEvent> gameEvents = game.playCard(new User("alice"), gameAction);
        assertEquals(1, gameEvents.size());
        assertSame(GameEvent.GameEventType.VALID_ACTION, gameEvents.get(0).getGameEventType());
        assertEquals("alice discarded the Princess and was eliminated", gameEvents.get(0).getMessage());
        assertTrue(game.getPlayerQueue().getPlayerByName("alice").isEliminated());
    }

    @Test
    public void testCountessCheat() {
        game.getPlayerQueue().getPlayerByName("alice").setCard1(new King());
        game.getPlayerQueue().getPlayerByName("alice").setCard2(new Countess());
        GameAction gameAction = new GameAction(1, "bob");
        ArrayList<GameEvent> gameEvents = game.playCard(new User("alice"), gameAction);
        assertEquals(1, gameEvents.size());
        assertSame(GameEvent.GameEventType.VALID_ACTION, gameEvents.get(0).getGameEventType());
        assertEquals("alice discarded the Countess", gameEvents.get(0).getMessage());
    }

    @Test
    public void testCountessValid() {
        game.getPlayerQueue().getPlayerByName("alice").setCard1(new Countess());
        game.getPlayerQueue().getPlayerByName("alice").setCard2(new King());
        GameAction gameAction = new GameAction(1);
        ArrayList<GameEvent> gameEvents = game.playCard(new User("alice"), gameAction);
        assertEquals(1, gameEvents.size());
        assertSame(GameEvent.GameEventType.VALID_ACTION, gameEvents.get(0).getGameEventType());
        assertEquals("alice discarded the Countess", gameEvents.get(0).getMessage());
    }

    @Test
    public void testCountessNeutral() {
        game.getPlayerQueue().getPlayerByName("alice").setCard1(new Countess());
        game.getPlayerQueue().getPlayerByName("alice").setCard2(new Priest());
        GameAction gameAction = new GameAction(1, "bob");
        ArrayList<GameEvent> gameEvents = game.playCard(new User("alice"), gameAction);
        assertEquals(1, gameEvents.size());
        assertSame(GameEvent.GameEventType.VALID_ACTION, gameEvents.get(0).getGameEventType());
        assertEquals("alice discarded the Priest and targeted bob", gameEvents.get(0).getMessage());
    }

    @Test
    public void testKing() {
        game.getPlayerQueue().getPlayerByName("alice").setCard1(new King());
        game.getPlayerQueue().getPlayerByName("alice").setCard2(new Priest());
        game.getPlayerQueue().getPlayerByName("bob").setCard1(new Countess());
        GameAction gameAction = new GameAction(1, "bob");
        ArrayList<GameEvent> gameEvents = game.playCard(new User("alice"), gameAction);
        assertEquals(3, gameEvents.size());
        assertSame(GameEvent.GameEventType.VALID_ACTION, gameEvents.get(0).getGameEventType());
        assertSame(GameEvent.GameEventType.CARD_EFFECT, gameEvents.get(1).getGameEventType());
        assertSame(GameEvent.GameEventType.CARD_EFFECT, gameEvents.get(2).getGameEventType());
        assertEquals("alice", gameEvents.get(1).getTarget().getName());
        assertEquals("bob", gameEvents.get(2).getTarget().getName());
        assertEquals("alice discarded the King and targeted bob", gameEvents.get(0).getMessage());
        assertEquals("You switched hands with bob, you got a Countess", gameEvents.get(1).getMessage());
        assertEquals("You switched hands with alice, you got a Priest", gameEvents.get(1).getMessage());
        assertEquals("Countess", game.getPlayerQueue().getPlayerByName("alice").getCard1().getCardName());
        assertEquals("Priest", game.getPlayerQueue().getPlayerByName("bob").getCard1().getCardName());
    }

    @Test
    public void testKing_immune() {
        game.getPlayerQueue().getPlayerByName("alice").setCard1(new King());
        game.getPlayerQueue().getPlayerByName("bob").setImmune(true);
        GameAction gameAction = new GameAction(1, "bob");
        ArrayList<GameEvent> gameEvents = game.playCard(new User("alice"), gameAction);
        assertEquals(1, gameEvents.size());
        GameEvent gameEvent = gameEvents.get(0);
        assertSame(GameEvent.GameEventType.INVALID_ACTION, gameEvent.getGameEventType());
        assertEquals("Bob is immune, you cannot target them", gameEvent.getMessage());
        assertEquals("alice", gameEvent.getTarget().getName());
    }

    @Test
    public void testPrince() {
        game.getPlayerQueue().getPlayerByName("alice").setCard1(new Prince());
        GameAction gameAction = new GameAction(1, "bob");
        ArrayList<GameEvent> gameEvents = game.playCard(new User("alice"), gameAction);
        assertEquals(3, gameEvents.size());
        assertSame(GameEvent.GameEventType.VALID_ACTION, gameEvents.get(0).getGameEventType());
        assertSame(GameEvent.GameEventType.CARD_EFFECT, gameEvents.get(1).getGameEventType());
        assertSame(GameEvent.GameEventType.CARD_EFFECT, gameEvents.get(2).getGameEventType());
        assertEquals("alice discarded the Prince and targeted bob", gameEvents.get(0).getMessage());
        // TODO: write test to confirm bob discarded and drew a new card
    }

    @Test
    public void testPrince_immune() {
        game.getPlayerQueue().getPlayerByName("alice").setCard1(new Prince());
        game.getPlayerQueue().getPlayerByName("bob").setImmune(true);
        GameAction gameAction = new GameAction(1, "bob");
        ArrayList<GameEvent> gameEvents = game.playCard(new User("alice"), gameAction);
        assertEquals(1, gameEvents.size());
        GameEvent gameEvent = gameEvents.get(0);
        assertSame(GameEvent.GameEventType.INVALID_ACTION, gameEvent.getGameEventType());
        assertEquals("Bob is immune, you cannot target them", gameEvent.getMessage());
        assertEquals("alice", gameEvent.getTarget().getName());
    }

    @Test
    public void testHandmaid() {
        game.getPlayerQueue().getPlayerByName("alice").setCard1(new Handmaid());
        GameAction gameAction = new GameAction(1);
        ArrayList<GameEvent> gameEvents = game.playCard(new User("alice"), gameAction);
        assertEquals(1, gameEvents.size());
        GameEvent gameEvent = gameEvents.get(0);
        assertSame(GameEvent.GameEventType.VALID_ACTION, gameEvent.getGameEventType());
        assertEquals("alice discarded the Handmaid, and is immune until their next turn", gameEvent.getMessage());
        assertTrue(game.getPlayerQueue().getPlayerByName("alice").isImmune());
    }

    @Test
    public void testBaronWin() {
        game.getPlayerQueue().getPlayerByName("alice").setCard1(new King());
        game.getPlayerQueue().getPlayerByName("alice").setCard2(new Baron());
        game.getPlayerQueue().getPlayerByName("bob").setCard1(new Prince());
        GameAction gameAction = new GameAction(2, "bob");
        ArrayList<GameEvent> gameEvents = game.playCard(new User("alice"), gameAction);
        assertEquals(1, gameEvents.size());
        GameEvent gameEvent = gameEvents.get(0);
        assertSame(GameEvent.GameEventType.VALID_ACTION, gameEvent.getGameEventType());
        assertEquals("alice discarded the Baron, and targeted bob; bob was eliminated", gameEvent.getMessage());
    }

    @Test
    public void testBaronLose() {
        game.getPlayerQueue().getPlayerByName("alice").setCard1(new Prince());
        game.getPlayerQueue().getPlayerByName("alice").setCard2(new Baron());
        game.getPlayerQueue().getPlayerByName("bob").setCard1(new King());
        GameAction gameAction = new GameAction(2, "bob");
        ArrayList<GameEvent> gameEvents = game.playCard(new User("alice"), gameAction);
        assertEquals(1, gameEvents.size());
        GameEvent gameEvent = gameEvents.get(0);
        assertSame(GameEvent.GameEventType.VALID_ACTION, gameEvent.getGameEventType());
        assertEquals("alice discarded the Baron, and targeted bob; alice was eliminated", gameEvent.getMessage());
    }

    @Test
    public void testBaron_immune() {
        game.getPlayerQueue().getPlayerByName("alice").setCard1(new Baron());
        game.getPlayerQueue().getPlayerByName("bob").setImmune(true);
        GameAction gameAction = new GameAction(1, "bob");
        ArrayList<GameEvent> gameEvents = game.playCard(new User("alice"), gameAction);
        assertEquals(1, gameEvents.size());
        GameEvent gameEvent = gameEvents.get(0);
        assertSame(GameEvent.GameEventType.INVALID_ACTION, gameEvent.getGameEventType());
        assertEquals("Bob is immune, you cannot target them", gameEvent.getMessage());
        assertEquals("alice", gameEvent.getTarget().getName());
    }

    @Test
    public void testPriest() {
        game.getPlayerQueue().getPlayerByName("alice").setCard1(new Prince());
        game.getPlayerQueue().getPlayerByName("bob").setCard1(new Countess());
        GameAction gameAction = new GameAction(1, "bob");
        ArrayList<GameEvent> gameEvents = game.playCard(new User("alice"), gameAction);
        assertEquals(2, gameEvents.size());
        assertSame(GameEvent.GameEventType.VALID_ACTION, gameEvents.get(0).getGameEventType());
        assertEquals("alice discarded the Priest, and targeted bob", gameEvents.get(0).getMessage());
        assertSame(GameEvent.GameEventType.CARD_EFFECT, gameEvents.get(1).getGameEventType());
        assertEquals("bob has a Countess", gameEvents.get(1).getMessage());
        assertEquals("alice", gameEvents.get(1).getTarget().getName());
    }

    @Test
    public void testPriest_immune() {
        game.getPlayerQueue().getPlayerByName("alice").setCard1(new Priest());
        game.getPlayerQueue().getPlayerByName("bob").setImmune(true);
        GameAction gameAction = new GameAction(1, "bob");
        ArrayList<GameEvent> gameEvents = game.playCard(new User("alice"), gameAction);
        assertEquals(1, gameEvents.size());
        GameEvent gameEvent = gameEvents.get(0);
        assertSame(GameEvent.GameEventType.INVALID_ACTION, gameEvent.getGameEventType());
        assertEquals("Bob is immune, you cannot target them", gameEvent.getMessage());
        assertEquals("alice", gameEvent.getTarget().getName());
    }

    @Test
    public void testGuardCorrect() {
        game.getPlayerQueue().getPlayerByName("alice").setCard1(new Guard());
        game.getPlayerQueue().getPlayerByName("bob").setCard1(new King());
        GameAction gameAction = new GameAction(1, "bob", "King");
        ArrayList<GameEvent> gameEvents = game.playCard(new User("alice"), gameAction);
        assertEquals(1, gameEvents.size());
        GameEvent gameEvent = gameEvents.get(0);
        assertSame(GameEvent.GameEventType.VALID_ACTION, gameEvents.get(0).getGameEventType());
        assertEquals("alice discarded the Guard, and targeted bob and guessed King; bob was eliminated", gameEvents.get(0).getMessage());
    }

    @Test
    public void testGuardIncorrect() {
        game.getPlayerQueue().getPlayerByName("alice").setCard1(new Guard());
        game.getPlayerQueue().getPlayerByName("bob").setCard1(new King());
        GameAction gameAction = new GameAction(1, "bob", "Countess");
        ArrayList<GameEvent> gameEvents = game.playCard(new User("alice"), gameAction);
        assertEquals(1, gameEvents.size());
        GameEvent gameEvent = gameEvents.get(0);
        assertSame(GameEvent.GameEventType.VALID_ACTION, gameEvents.get(0).getGameEventType());
        assertEquals("alice discarded the Guard, and targeted bob and guessed Countess; bob was not eliminated", gameEvents.get(0).getMessage());
    }

    @Test
    public void testGuard_immune() {
        game.getPlayerQueue().getPlayerByName("alice").setCard1(new Guard());
        game.getPlayerQueue().getPlayerByName("bob").setImmune(true);
        GameAction gameAction = new GameAction(1, "bob", "Prince");
        ArrayList<GameEvent> gameEvents = game.playCard(new User("alice"), gameAction);
        assertEquals(1, gameEvents.size());
        GameEvent gameEvent = gameEvents.get(0);
        assertSame(GameEvent.GameEventType.INVALID_ACTION, gameEvent.getGameEventType());
        assertEquals("Bob is immune, you cannot target them", gameEvent.getMessage());
        assertEquals("alice", gameEvent.getTarget().getName());
    }
}