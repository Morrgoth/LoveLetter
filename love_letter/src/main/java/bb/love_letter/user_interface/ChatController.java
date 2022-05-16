package bb.love_letter.user_interface;


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

    public void addChatMessageDisplay (ChatMessage chatMessage){
        ChatMessageDisplay chatMessageDisplay = new ChatMessageDisplay(chatMessage);
        model.addVBox(chatMessageDisplay.getHBox());
    }

    public void addChatMessageDisplay (ServerEvent serverEvent){
        ChatMessageDisplay chatMessageDisplay = new ChatMessageDisplay(serverEvent);
        model.addVBox(chatMessageDisplay.getHBox());
    }

    public void addMessage(Envelope envelope) {
        if (envelope.getType() == Envelope.EnvelopeType.CHAT_MESSAGE) {
            ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
            System.out.println(chatMessage.isPrivate());
            addChatMessageDisplay(chatMessage);
        } else if (envelope.getType() == Envelope.EnvelopeType.SERVER_EVENT) {
            ServerEvent serverEvent = (ServerEvent) envelope.getPayload();
            addChatMessageDisplay(serverEvent);
        }
    }
}




