package bb.love_letter.user_interface;

import bb.love_letter.game.User;
import bb.love_letter.networking.data.ChatMessage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Zeynab Baiani
 */
public class ChatModel {
   private ObservableList <ChatMessage> chatMessageObservableList = FXCollections.observableArrayList();
   private ObservableList <String> chatMessageStringObservableList = FXCollections.observableArrayList();
   private ObservableList<User> userObservableList = FXCollections.observableArrayList();

   private ObservableList<VBox> vBoxObservableList = FXCollections.observableArrayList();
   public ObservableList<VBox> getvBoxObservableList(){
       return vBoxObservableList;
   }
   public void addVBox (VBox vbox){
       vBoxObservableList.add(vbox);
   }

   private StringProperty currentMessage = new SimpleStringProperty("");

    public ObservableList<ChatMessage> getChatMessageObservableList() {
        return chatMessageObservableList;
    }

    public void addChatMessage(ChatMessage chatMessage){
        chatMessageObservableList.add(chatMessage);
    }

    public ObservableList<User> getUserObservableList() {
        return userObservableList;
    }

    public ObservableList<String> getChatMessageStringObservableList() {return chatMessageStringObservableList;}
    public void addChatMessageString(ChatMessage chatMessage) {
        chatMessageStringObservableList.add(chatMessage.getSender().getName() + ": " + chatMessage.getMessage());
    }

    public void addUser(User user){
        userObservableList.add(user);
    }

    public StringProperty currentMessageProperty() {
        return currentMessage;
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
}
