package bb.love_letter;

import jdk.internal.org.objectweb.asm.ClassReader;

import java.io.*;
//import java.net.*;

public class ClientSessionHandler implements Runnable{
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
                Envelope envelope =(Envelope) NetworkConnection.getInstance().getObjectInputStream().readObject();

                switch (Envelope.getType()) {
                    case Envelope.TypeEnum.USER:
                        //fill in action for ChatHandler
                        break;
                    case Envelope.TypeEnum.CHATMESSAGE:
                        //fill in action for ChatHandler
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