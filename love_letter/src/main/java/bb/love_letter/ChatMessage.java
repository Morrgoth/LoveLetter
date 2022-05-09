package bb.love_letter;


import java.io.Serializable;

public class ChatMessage implements Serializable {
    public User sender;
    public String message;

    public ChatMessage(User sender, String message){
        this.sender = sender;
        this.message = message;
    }

    public User getSender(){
        return sender;
    }

    public String getMessage(){
        return message;
    }

    public void setSender(User user) {
        this.sender = user;
    }
}
