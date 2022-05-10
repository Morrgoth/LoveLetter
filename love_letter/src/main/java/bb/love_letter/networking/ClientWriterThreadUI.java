package bb.love_letter.networking;

import bb.love_letter.user_interface.ChatController;
import bb.love_letter.user_interface.ChatMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.*;

public class ClientWriterThreadUI extends Thread{
    private ChatController chatController;
    public ClientWriterThreadUI(ChatController chatController){
        this.chatController = chatController;
    }
    public void run()
    {
        System.out.println("ClientWriterThreadUI started running");
        UserEvent loginEvent = new UserEvent(NetworkConnection.getInstance().getUser(), UserEvent.UserEventType.LOGIN_CONFIRMATION);
        Envelope loginNotification = new Envelope(loginEvent, Envelope.TypeEnum.USEREVENT);
        Gson gson = new GsonBuilder().registerTypeAdapter(Envelope.class, new EnvelopeSerializer()).create();
        try {
            NetworkConnection.getInstance().getOutputStream().writeUTF(gson.toJson(loginNotification));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Keep listening for changes
        chatController.model.currentMessageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                if (!newVal.equals("")) {
                    ChatMessage chatMessage = new ChatMessage(NetworkConnection.getInstance().getUser(), newVal);
                    Envelope envelope = new Envelope(chatMessage, Envelope.TypeEnum.CHATMESSAGE);
                    String json = gson.toJson(envelope);
                    try {
                        NetworkConnection.getInstance().getOutputStream().writeUTF(json);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    chatController.model.resetCurrentMessage();
                }
            }
        });
    }
}