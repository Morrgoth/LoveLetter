package bb.love_letter.networking;

import bb.love_letter.user_interface.ChatController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.*;
/**
 * This is the Thread that sends all the messages from the Client to the Server. It waits for lines on the command line
 * and if a line is fed into it, it forwards it the Server.
 *
 * It is used by the GUI version of Love Letter.
 *
 * @author Bence Ament
 */
public class ClientWriterThreadUI extends Thread{
    private ChatController chatController;
    public ClientWriterThreadUI(ChatController chatController){
        this.chatController = chatController;
    }
    /**
     * This method listens to the changes of the ChatModel, and upon detecting a new Message written by the User
     * it forwards it to the Server.
     */
    public void run()
    {
        System.out.println("ClientWriterThreadUI started running");
        ServerEvent loginEvent = new ServerEvent(NetworkConnection.getInstance().getUser(), ServerEvent.UserEventType.LOGIN_CONFIRMATION);
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