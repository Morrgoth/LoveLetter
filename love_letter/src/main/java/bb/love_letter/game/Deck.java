package bb.love_letter.game;

import bb.love_letter.game.characters.*;

import java.util.ArrayList;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    Guard guard1 = new Guard();
   // Guard guard2 = new Guard();
   // Guard guard3 = new Guard();
   // Guard guard4 = new Guard();
    Guard guard5 = new Guard();
    Priest priest1 = new Priest();
    Priest priest2 = new Priest();
    Baron baron1 = new Baron();
    Baron baron2 = new Baron();
    Handmaid handmaid1 = new Handmaid();
    Handmaid handmaid2 = new Handmaid();
    Prince prince = new Prince();
    King king = new King();
    Countess countess = new Countess();
    Princess princess = new Princess();

    ArrayList deck;

    {
        deck = new ArrayList<Cards>(guard1, guard2, guard3, guard4, guard5, priest1, priest2, baron1, baron2, handmaid1, handmaid2, prince, king, countess, princess);
    }

    //shuffle deck before play / pbly in GAME class/interface

}
