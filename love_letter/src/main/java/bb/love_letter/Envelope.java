package bb.love_letter;

import java.io.*;

public class Envelope implements Serializable {
    public Object payload;
    public TypeEnum type;

    //enum * user & chatMessage ) speichert in string type
    public enum TypeEnum{
        USEREVENT,
        CHATMESSAGE
    }

    public Envelope(Object payload, TypeEnum type){
        this.payload = payload;
        this.type = type;
    }

    public Envelope() {
        this.payload = null;
        this.type = null;
    }

    public Object getPayload(){
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public TypeEnum getType(){

        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

}
