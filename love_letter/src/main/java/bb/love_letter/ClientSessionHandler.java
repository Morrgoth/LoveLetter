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
        System.out.println("ClientSessionHandler Thread started running");

        while (true) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(NetworkConnection.getInstance().getSocket().getInputStream()));
                String json = null;
                while (json == null) {
                    json = bufferedReader.readLine();
                }
                System.out.println(json);
                Envelope envelope = Util.deserializeJsontoEnvelope(json);
                switch (envelope.getType()) {
                    case USEREVENT:
                        UserEvent userEvent = (UserEvent) envelope.getPayload();
                        chatController.addUser(userEvent.getUser());
                        break;
                    case CHATMESSAGE:
                        ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
                        chatController.addChatMessage(chatMessage);
                        break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
