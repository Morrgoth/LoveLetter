package bb.love_letter.user_interface;


import bb.love_letter.game.User;
import bb.love_letter.networking.data.ChatMessage;
import bb.love_letter.networking.data.Envelope;
import bb.love_letter.networking.data.ServerEvent;

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
    public void addChatMessageDisplay (ChatMessage chatMessage){
        ChatMessageDisplay chatMessageDisplay = new ChatMessageDisplay(chatMessage);
        model.addVBox(chatMessageDisplay.gethBox());
    }

    // This is just a workaround, this method should be deleted when we have the Display class for ChatMessages
    public void addChatMessage(Envelope envelope) {
        if (envelope.getType() == Envelope.EnvelopeType.CHAT_MESSAGE) {
            ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
           addChatMessageDisplay(chatMessage);
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




