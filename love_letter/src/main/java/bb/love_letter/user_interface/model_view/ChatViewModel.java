package bb.love_letter.user_interface.model_view;

import bb.love_letter.networking.data.ChatMessage;
import bb.love_letter.networking.data.Envelope;
import bb.love_letter.networking.data.ServerEvent;
import bb.love_letter.user_interface.Client;
import bb.love_letter.user_interface.model.ChatModel;
import bb.love_letter.user_interface.view.ChatMessageDisplayItem;
import bb.love_letter.user_interface.view.ChatView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


public class ChatViewModel{

    private ChatModel model;
    private ChatView view;

    private Client client;

    public ChatViewModel(Client client, ChatModel chatModel, ChatView chatView) {
        this.client = client;
        model = chatModel;
        view = chatView;
        observeModelandUpdate();
    }

    private void observeModelandUpdate() {
        model.isLoggedOutProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldVal, Boolean newVal) {
                if (newVal) {
                    client.logout();
                    model.setIsLoggedOut(false);
                }
            }
        });
    }

}
