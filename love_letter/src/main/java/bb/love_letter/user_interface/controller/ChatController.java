package bb.love_letter.user_interface.controller;


import bb.love_letter.networking.data.ChatMessage;
import bb.love_letter.networking.data.Envelope;
import bb.love_letter.networking.data.ServerEvent;
import bb.love_letter.user_interface.Client;
import bb.love_letter.user_interface.view.ChatMessageDisplayItem;
import bb.love_letter.user_interface.model.ChatModel;

/**
 *
 * @author Zeynab Baiani
 */
public class ChatController {
    public ChatModel model;
    private Client client;

    public ChatController(ChatModel chatModel, Client client) {
        this.model= chatModel;
        this.client = client;
    }

    public void processMessage(Envelope envelope) {
        if (envelope.getType() == Envelope.EnvelopeType.CHAT_MESSAGE) {
            ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
            addDisplayItem(chatMessage);
        } else if (envelope.getType() == Envelope.EnvelopeType.SERVER_EVENT) {
            ServerEvent serverEvent = (ServerEvent) envelope.getPayload();
            if (serverEvent.getServerEventType() == ServerEvent.ServerEventType.LOGOUT_CONFIRMATION) {
                client.logout();
            }
            addDisplayItem(serverEvent);
        }
    }

    public void addDisplayItem(ChatMessage chatMessage){
        ChatMessageDisplayItem chatMessageDisplay = new ChatMessageDisplayItem(chatMessage);
        model.addVBox(chatMessageDisplay.getHBox());
    }

    public void addDisplayItem(ServerEvent serverEvent){
        ChatMessageDisplayItem chatMessageDisplay = new ChatMessageDisplayItem(serverEvent);
        model.addVBox(chatMessageDisplay.getHBox());
    }
}




