package bb.love_letter.user_interface;

import bb.love_letter.game.User;
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
        //vBox1.getStyleClass().add("ChatWindow");
        Label username = new Label(chatMessage.getSender().getName());
        username.getStyleClass().add("userName");
        vBox1.getChildren().add(username);
        Label message = new Label(chatMessage.getMessage());

        vBox1.getChildren().add(message);
        hBox1.getChildren().add(vBox1);
        message.getStyleClass().add("messageBubble");

        if (NetworkConnection.getInstance().getUser().equals(chatMessage.getSender())){
             hBox1.setAlignment(Pos.BASELINE_RIGHT);
            message.getStyleClass().add("sent");
        } else {
            message.getStyleClass().add("received");

        }
        return hBox1;
     }
    private HBox buildDisplay (ServerEvent serverEvent){
        ChatMessage chatMessage = new ChatMessage(new User("Server"), serverEvent.getMessage());
        HBox hBox1 = new HBox();
        VBox vBox1 = new VBox();
        Label username = new Label(chatMessage.getSender().getName());
        username.getStyleClass().add("userName");
        vBox1.getChildren().add(username);
        Label message = new Label(chatMessage.getMessage());

        vBox1.getChildren().add(message);
        hBox1.getChildren().add(vBox1);
        message.getStyleClass().add("messageBubble");
        message.getStyleClass().add("received");
        return hBox1;
    }

    public HBox gethBox(){
        return hBox;
    }



}
