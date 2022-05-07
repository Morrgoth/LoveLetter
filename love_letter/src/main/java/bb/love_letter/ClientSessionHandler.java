package bb.love_letter;

import java.io.*;
import java.net.Socket;

public class ClientSessionHandler implements Runnable {
    private ObjectInputStream serverInput;
    private ChatController chatController;

    /*
    im Envelope wird type user || chatMessage Type durch getType() an Switch gegeben;
    danach switch case von Type - wenn user mach USER || wenn chatMessage mach CHATMESSAGE
    Weiterleitung an chatController
     */
    public ClientSessionHandler(ChatController chatController) {
        this.chatController = chatController;
    }

    @Override
    public void run() {
        System.out.println("Thread started running");

        while (true) {
            try {
                //Receive message from server
                Envelope envelope = (Envelope) NetworkConnection.getInstance().getObjectInputStream().readObject();
                switch (envelope.getType()) {
                    case USER:
                        User user = (User) envelope.getPayload();
                        chatController.addUser(user);
                        break;
                    case CHATMESSAGE:
                        ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
                        chatController.addChatMessage(chatMessage);
                        break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
