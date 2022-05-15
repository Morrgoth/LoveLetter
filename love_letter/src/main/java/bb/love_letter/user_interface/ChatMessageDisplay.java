package bb.love_letter.user_interface;

import bb.love_letter.networking.NetworkConnection;
import bb.love_letter.networking.data.ChatMessage;
import bb.love_letter.networking.data.ServerEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;

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
        Label username = new Label(chatMessage.getSender().getName());
        username.getStyleClass().add("userName");
        vBox1.getChildren().add(username);
        Label label = new Label(chatMessage.getMessage());
        label.getStyleClass().add("messageBubble");
        vBox1.getChildren().add(label);
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
