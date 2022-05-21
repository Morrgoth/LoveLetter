package bb.love_letter.user_interface.model;

import bb.love_letter.game.User;
import bb.love_letter.networking.data.ChatMessage;
import bb.love_letter.networking.data.Envelope;
import bb.love_letter.networking.data.ServerEvent;
import bb.love_letter.user_interface.view.ChatMessageDisplayItem;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;

/**
 *
 * @author Zeynab Baiani
 */
public class ChatModel {

    private ObservableList<HBox> vBoxObservableList = FXCollections.observableArrayList();
    private StringProperty currentMessage = new SimpleStringProperty("");
    private BooleanProperty isLoggedOut = new SimpleBooleanProperty(false);
    public ObservableList<HBox> getHBoxObservableList(){
       return vBoxObservableList;
   }
   public void addVBox (HBox hbox){
       vBoxObservableList.add(hbox);
   }

    public StringProperty currentMessageProperty() {
        return currentMessage;
    }

    public void setIsLoggedOut(boolean loggedOut){
        isLoggedOut.set(loggedOut);
    }

    public BooleanProperty isLoggedOutProperty() {
        return isLoggedOut;
    }

    public String getCurrentMessage() {
        return currentMessage.get();
    }

    public void setCurrentMessage(String message) {
        currentMessage.set(message);
    }

    public void resetCurrentMessage() {
        currentMessage.set("");
    }

    public void processMessage(Envelope envelope) {
        if (envelope.getType() == Envelope.EnvelopeType.CHAT_MESSAGE) {
            ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
            addDisplayItem(chatMessage);
        } else if (envelope.getType() == Envelope.EnvelopeType.SERVER_EVENT) {
            ServerEvent serverEvent = (ServerEvent) envelope.getPayload();
            if (serverEvent.getServerEventType() == ServerEvent.ServerEventType.LOGOUT_CONFIRMATION) {
                setIsLoggedOut(true);
            }
            addDisplayItem(serverEvent);
        }
    }

    public void addDisplayItem(ChatMessage chatMessage){
        ChatMessageDisplayItem chatMessageDisplay = new ChatMessageDisplayItem(chatMessage);
        addVBox(chatMessageDisplay.getHBox());
    }

    public void addDisplayItem(ServerEvent serverEvent){
        ChatMessageDisplayItem chatMessageDisplay = new ChatMessageDisplayItem(serverEvent);
        addVBox(chatMessageDisplay.getHBox());
    }

}
