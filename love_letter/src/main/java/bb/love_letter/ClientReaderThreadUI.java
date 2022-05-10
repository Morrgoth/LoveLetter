package bb.love_letter;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.DataInputStream;

class ClientReaderThreadUI extends Thread{
    ChatController chatController;
    public ClientReaderThreadUI(ChatController chatController){
        this.chatController = chatController;
    }
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