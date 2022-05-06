package bb.love_letter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChatModel {
   private ObservableList <ChatMessage> chatMessageObservableList = FXCollections.observableArrayList();
   private ObservableList<User> userObservableList = FXCollections.observableArrayList();

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
}
