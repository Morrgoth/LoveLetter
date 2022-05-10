package bb.love_letter.networking;

import bb.love_letter.game.User;
import bb.love_letter.user_interface.ChatController;
import javafx.application.Platform;

/**
 * This is the Thread that receives all messages from the Server and handles them as needed. It is used by
 * the GUI version of Love Letter.
 *
 * @author Bence Ament
 */
public class ClientReaderThreadUI extends Thread{
    ChatController chatController;
    public ClientReaderThreadUI(ChatController chatController){
        this.chatController = chatController;
    }
    /**
     * Handling of messages received from the Server
     */
    public void run()
    {
        System.out.println("ClientReaderThreadUI started running");
        try{
            String json=null;
            while(true){
                // RECEIVE MESSAGE FROM SERVER
                json = NetworkConnection.getInstance().getInputStream().readUTF();
                if(json != null) {
                    Envelope envelope = Envelope.deserializeEnvelopeFromJson(json);
                    if (envelope.getType() == Envelope.TypeEnum.USEREVENT) {
                        UserEvent userEvent = (UserEvent) envelope.getPayload();
                        if (userEvent.getUserEventType() == UserEvent.UserEventType.LOGIN_CONFIRMATION) {
                            User newUser = userEvent.getUser();
                            if (!newUser.getName().equals(NetworkConnection.getInstance().getUser().getName())) {
                                ChatMessage userJoinedMessage =
                                        new ChatMessage(new User("Server"), newUser.getName() + " joined the room");
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        chatController.addChatMessage(userJoinedMessage);
                                    }
                                });
                            }
                        } else if (userEvent.getUserEventType() == UserEvent.UserEventType.LOGOUT_CONFIRMATION) {
                            ChatMessage userLeftMessage =
                                    new ChatMessage(new User("Server"), userEvent.getUser().getName() + " left the room");
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    chatController.addChatMessage(userLeftMessage);
                                }
                            });
                        }
                    } else if (envelope.getType() == Envelope.TypeEnum.CHATMESSAGE) {
                        ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                chatController.addChatMessage(chatMessage);
                            }
                        });
                    }
                }
                json = null;
            }
        }
        catch(Exception e){
            // Error Message should be added here
        }
    }
}