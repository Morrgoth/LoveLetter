package bb.love_letter;

import java.io.DataInputStream;

class MyThreadRead extends Thread{
    DataInputStream is;
    public MyThreadRead(DataInputStream i){
        is=i;
    }
    public void run()
    {
        try{
            String json=null;
            while(true){
                // RECEIVE MESSAGE FROM SERVER
                json = is.readUTF();
                if(json != null) {
                    Envelope envelope = Util.deserializeJsontoEnvelope(json);
                    if (envelope.getType() == Envelope.TypeEnum.USEREVENT) {
                        System.out.println("UserEvent: TODO");
                    } else if (envelope.getType() == Envelope.TypeEnum.CHATMESSAGE) {
                        ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
                        System.out.println(chatMessage.getSender().getName() + ": " + chatMessage.getMessage());
                    }
                }
                json = null;
            }
        }
        catch(Exception e){

        }
    }
}