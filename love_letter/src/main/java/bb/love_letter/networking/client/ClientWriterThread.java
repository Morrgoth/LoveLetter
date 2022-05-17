package bb.love_letter.networking.client;

import bb.love_letter.networking.data.ChatMessage;
import bb.love_letter.user_interface.controller.ChatController;
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
 * @author Tolga Engin
 */
public class ClientWriterThread extends Thread{
    private ChatController chatController;
    public ClientWriterThread(ChatController chatController){
        this.chatController = chatController;
    }
    /**
     * This method listens to the changes of the ChatModel, and upon detecting a new Message written by the User
     * it forwards it to the Server.
     */
    public void run()
    {
        System.out.println("ClientWriterThreadUI started running");
        // Keep listening for messages to send
        chatController.model.currentMessageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                if (!newVal.equals("")) {
                    ChatMessage chatMessage = new ChatMessage(NetworkConnection.getInstance().getUser(), newVal);
                    try {
                        NetworkConnection.getInstance().getOutputStream().writeUTF(chatMessage.toEnvelope().toJson());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    chatController.model.resetCurrentMessage();
                }
            }
        });
    }
}