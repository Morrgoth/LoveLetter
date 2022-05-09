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
                    Envelope envelope = Util.deserializeJsontoEnvelope(json);
                    if (envelope.getType() == Envelope.TypeEnum.USEREVENT) {
                        UserEvent userEvent = (UserEvent) envelope.getPayload();
                        if (userEvent.getUserEventType() == UserEvent.UserEventType.LOGIN_CONFIRMATION) {
                            User newUser = userEvent.getUser();
                            if (!newUser.getName().equals(NetworkConnection.getInstance().getUser().getName())) {
                                System.out.println(newUser.getName() + " joined the room");
                            }
                        } else if (userEvent.getUserEventType() == UserEvent.UserEventType.LOGOUT_CONFIRMATION) {
                            System.out.println(userEvent.getUser().getName() + " left the room");
                        }
                    } else if (envelope.getType() == Envelope.TypeEnum.CHATMESSAGE) {
                        ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                chatController.addChatMessage(chatMessage);
                            }
                        });

                        //System.out.println(chatMessage.getSender().getName() + ": " + chatMessage.getMessage());
                    }
                }
                json = null;
            }
        }
        catch(Exception e){

        }
    }
}