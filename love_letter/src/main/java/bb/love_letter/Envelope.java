package bb.love_letter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

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

    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Envelope.class, new EnvelopeSerializer()).create();
        return gson.toJson(this);
    }

    public static Envelope deserializeEnvelopeFromJson(String json) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        Envelope envelope = new Envelope();
        if (jsonObject.get("type").getAsString().equals("USEREVENT")) {
            UserEvent userEvent = gson.fromJson(jsonObject.get("payload").getAsString(), UserEvent.class);
            envelope.setType(Envelope.TypeEnum.USEREVENT);
            envelope.setPayload(userEvent);
        } else if (jsonObject.get("type").getAsString().equals("CHATMESSAGE")){
            ChatMessage chatMessage = gson.fromJson(jsonObject.get("payload").getAsString(), ChatMessage.class);
            User user = gson.fromJson(jsonObject.get("user").getAsString(), User.class);
            chatMessage.setSender(user);
            envelope.setType(Envelope.TypeEnum.CHATMESSAGE);
            envelope.setPayload(chatMessage);
        }
        return envelope;
    }

}
