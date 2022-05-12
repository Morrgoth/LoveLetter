package bb.love_letter.user_interface;


import bb.love_letter.game.User;
import bb.love_letter.networking.ChatMessage;
import bb.love_letter.networking.Envelope;
import bb.love_letter.networking.ServerEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Zeynab Baiani
 */
public class ChatController {
    public ChatModel model;

    public ChatController(ChatModel chatModel) {
        this.model= chatModel;
    }

    public void addChatMessage(ChatMessage chatMessage) {
        this.model.addChatMessage(chatMessage);
        this.model.addChatMessageString(chatMessage);
    }

    // This is just a workaround, this method should be deleted when we have the Display class for ChatMessages
    public void addChatMessage(Envelope envelope) {
        if (envelope.getType() == Envelope.EnvelopeType.CHAT_MESSAGE) {
            ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
            this.model.addChatMessage(chatMessage);
            this.model.addChatMessageString(chatMessage);
        } else if (envelope.getType() == Envelope.EnvelopeType.SERVER_EVENT) {
            ServerEvent serverEvent = (ServerEvent) envelope.getPayload();
            ChatMessage chatMessage = new ChatMessage(new User("Server"), serverEvent.getMessage());
            this.model.addChatMessage(chatMessage);
            this.model.addChatMessageString(chatMessage);
        }
    }

    public void addUser(User user){
        this.model.addUser(user);
    }

}




