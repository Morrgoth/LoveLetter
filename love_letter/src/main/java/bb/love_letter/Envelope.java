package bb.love_letter;

import java.io.*;

public class Envelope implements Serializable {
    private static final long serialVersionUID = 1L;
    private  Object payload;
    private  TypeEnum type;

    //enum * user & chatMessage ) speichert in string type
    public enum TypeEnum{
        USEREVENT,
        CHATMESSAGE
    }

    public Envelope(Object payload, TypeEnum type){
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
