package bb.love_letter;

import java.io.*;

public class Envelope implements Serializable {
    private  Object payload;
    private  String type;

    public Envelope(final Object payload,String type){
        this.payload = payload;
        this.type = type;
    }

    public Object getPayload(){
        return this.payload;
    }

    public String getType(){
        return type;
    }

}
