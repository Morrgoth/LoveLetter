package bb.love_letter.user_interface;

import bb.love_letter.networking.NetworkConnection;
import bb.love_letter.networking.data.ChatMessage;
import bb.love_letter.networking.data.ServerEvent;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ChatMessageDisplay {
    private HBox hBox;

    public ChatMessageDisplay (ChatMessage chatMessage){
        hBox = buildDisplay(chatMessage);
    }
    public ChatMessageDisplay(ServerEvent serverEvent){
        hBox = buildDisplay(serverEvent);
    }
     private HBox buildDisplay (ChatMessage chatMessage){
        VBox vBox1 = new VBox();
        HBox hBox1 = new HBox();
        vBox1.getChildren().add(new Text(chatMessage.getSender().getName()));
        vBox1.getChildren().add(new Text(chatMessage.getMessage()));
        hBox1.getChildren().add(vBox1);
        if (NetworkConnection.getInstance().getUser().equals(chatMessage.getSender())){
             hBox1.setAlignment(Pos.BASELINE_RIGHT);
        }
        return hBox1;
     }
    private HBox buildDisplay (ServerEvent serverEvent){
        return null;
    }

    public HBox gethBox(){
        return hBox;
    }



}
