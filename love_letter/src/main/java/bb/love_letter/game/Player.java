package bb.love_letter.game;

public class Player extends User{

    private int token;

    public Player(String name, int token) {
        super(name);
        this.token = token;
    }

    public int getToken(){
        return token;
    }

}
