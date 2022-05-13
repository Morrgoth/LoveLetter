package bb.love_letter.user_interface;

import bb.love_letter.networking.data.ChatMessage;
import bb.love_letter.networking.data.ServerEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ChatMessageDisplay {
    private VBox vBox;

    public ChatMessageDisplay (ChatMessage chatMessage){
        vBox = buildDisplay(chatMessage);
    }
    public ChatMessageDisplay(ServerEvent serverEvent){
        vBox = buildDisplay(serverEvent);
    }
     private VBox buildDisplay (ChatMessage chatMessage){
        VBox vBox1 = new VBox();
        vBox1.getChildren().add(new Text(chatMessage.getSender().getName()));
         vBox1.getChildren().add(new Text(chatMessage.getMessage()));
         return vBox1;
     }
    private VBox buildDisplay (ServerEvent serverEvent){
        return null;
    }



}
