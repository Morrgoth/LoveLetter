package bb.love_letter.user_interface.view;

import bb.love_letter.game.User;
import bb.love_letter.networking.client.NetworkConnection;
import bb.love_letter.networking.data.ChatMessage;
import bb.love_letter.networking.data.ServerEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

/**
 *
 * @author Zeynab Baiani
 */
public class ChatMessageDisplayItem {
    private HBox hBox;

    public ChatMessageDisplayItem(ChatMessage chatMessage){
        hBox = buildDisplay(chatMessage);
    }
    public ChatMessageDisplayItem(ServerEvent serverEvent){
        hBox = buildDisplay(serverEvent);
    }

    /**
     * Converts ChatMessages to UI elements which can be displayed in the ListView
     * @param chatMessage
     * @return
     */
     private HBox buildDisplay (ChatMessage chatMessage){
        VBox vBox1 = new VBox();
        HBox hBox1 = new HBox();
        Label username;
        if (chatMessage.isPrivate()) {
            username = new Label(chatMessage.getSender().getName() + " (Private Message)");
        } else {
            username = new Label(chatMessage.getSender().getName());
        }
        username.getStyleClass().add("userName");
        vBox1.getChildren().add(username);
        Label message = new Label(chatMessage.getMessage());
        message.setWrapText(true);
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

    /**
     * Converts ChatMessages to UI elements which can be displayed in the ListView
     * @param serverEvent
     * @return
     */
    private HBox buildDisplay (ServerEvent serverEvent){
        ChatMessage chatMessage = new ChatMessage(new User("Server"), serverEvent.getMessage());
        HBox hBox1 = new HBox();
        VBox vBox1 = new VBox();
        Label username = new Label(chatMessage.getSender().getName());
        username.getStyleClass().add("userName");
        vBox1.getChildren().add(username);
        Label message = new Label(chatMessage.getMessage());
        message.setWrapText(true);
        vBox1.getChildren().add(message);
        hBox1.getChildren().add(vBox1);
        message.getStyleClass().add("messageBubble");
        message.getStyleClass().add("received");
        return hBox1;
    }

    public HBox getHBox(){
        return hBox;
    }
}
