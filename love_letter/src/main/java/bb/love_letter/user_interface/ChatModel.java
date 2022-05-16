package bb.love_letter.user_interface;

import bb.love_letter.game.User;
import bb.love_letter.networking.data.ChatMessage;
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
    public ObservableList<HBox> getHBoxObservableList(){
       return vBoxObservableList;
   }
   public void addVBox (HBox hbox){
       vBoxObservableList.add(hbox);
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
