package bb.love_letter.game;

import bb.love_letter.game.characters.Cards;

import java.util.ArrayList;

public class Player extends User{

    private Cards card1;
    private Cards card2;
    private int token = 0;
    private ArrayList<Cards> discarded = new ArrayList<>();

    public Player(String name, int token) {
        super(name);
        this.token = token;
    }

    public Cards getCard1(){
        return card1;
    }

    public void setCard1(Cards card1){
        this.card1 = card1;
    }

    public void setCard2(Cards card2){
        this.card2 = card2;
    }

    public Cards getCard2(){
        return card2;
    }

    public int getToken(){
        return token;
    }

    public void setToken(){
        token++;
    }

    public int discardedPoints(ArrayList<Cards> discarded){
        int sum = 0;
        for(Cards card: discarded){
            sum += card.getCardPoints();
        }
        return sum;
    }

}
