package bb.love_letter;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChatModel {
   private ObservableList <ChatMessage> chatMessageObservableList = FXCollections.observableArrayList();
   private ObservableList<User> userObservableList = FXCollections.observableArrayList();

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
