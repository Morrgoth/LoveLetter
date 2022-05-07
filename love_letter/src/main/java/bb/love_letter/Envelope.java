package bb.love_letter;

import java.io.*;

public class Envelope implements Serializable {
    private  Object payload;
    private  TypeEnum type;

    //enum * user & chatMessage ) speichert in string type
    public enum TypeEnum{
        USER,
        CHATMESSAGE
    }

    public Envelope(final Object payload, TypeEnum type){
        this.payload = payload;
        this.type = type;
    }

    public Object getPayload(){
        return payload;
    }

    public TypeEnum getType(){

        return type;
    }

}
