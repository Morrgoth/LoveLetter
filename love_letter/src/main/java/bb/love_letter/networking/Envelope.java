package bb.love_letter.networking;

import bb.love_letter.game.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;

/**
 * Envelope is used for Client-Server communication, by convention whenever we exchange data betweeen Client and Server
 * it must be wrapped by the sender in an Envelope and turned to JSON, and it must be deserialized from JSON by
 * the receiver and then unwrapped.
 *
 * @author Muqiu Wang
 */
public class Envelope implements Serializable {
    public Object payload;
    public EnvelopeType type;

    /**
     * TypeEnum is used to store the original type of the payload of the Envelope which is necessary to know in order
     * to unwrap the Envelope and get the payload back.
     */
    public enum EnvelopeType{
        SERVER_EVENT,
        CHAT_MESSAGE,
        LOGIN_REQUEST
    }

    /**
     * This constructor is used if you wish to create a new Envelope.
     * @param payload The Object to be exchanged between Client and Server.
     * @param type The original type of the payload.
     */
    public Envelope(Object payload, EnvelopeType type){
        this.payload = payload;
        this.type = type;
    }

    /**
     * This constructor is only used for the deserialization.
     */
    public Envelope() {
        this.payload = null;
        this.type = null;
    }

    public Object getPayload(){
        return payload;
    }
    public EnvelopeType getType(){
        return type;
    }

    /**
     * This method is only used for the deserialization.
     * @param payload The Object to be stored in the Envelope.
     */
    public void setPayload(Object payload) {
        this.payload = payload;
    }
    public void setType(EnvelopeType type) {
        this.type = type;
    }

    /**
     * @return JSON String to be sent through the network.
     */
    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Envelope.class, new EnvelopeSerializer()).create();
        return gson.toJson(this);
    }

    /**
     * This static method is used to recover an Envelope from its serialized JSON format.
     * @param json This is a JSON String received through the network.
     * @return An Envelope that has the same contents as the one which was originally sent through the Network.
     */
    public static Envelope deserializeEnvelopeFromJson(String json) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        Envelope envelope = new Envelope();
        if (jsonObject.get("type").getAsString().equals("SERVER_EVENT")) {
            ServerEvent serverEvent = gson.fromJson(jsonObject.get("payload").getAsString(), ServerEvent.class);
            envelope.setType(EnvelopeType.SERVER_EVENT);
            envelope.setPayload(serverEvent);
        } else if (jsonObject.get("type").getAsString().equals("CHAT_MESSAGE")){
            ChatMessage chatMessage = gson.fromJson(jsonObject.get("payload").getAsString(), ChatMessage.class);
            User user = gson.fromJson(jsonObject.get("user").getAsString(), User.class);
            chatMessage.setSender(user);
            envelope.setType(Envelope.EnvelopeType.CHAT_MESSAGE);
            envelope.setPayload(chatMessage);
        }else if (jsonObject.get("type").getAsString().equals("LOGIN_REQUEST")){
            LoginRequest loginRequest = gson.fromJson(jsonObject.get("payload").getAsString(), LoginRequest.class);
            User user = gson.fromJson(jsonObject.get("user").getAsString(), User.class);
            loginRequest.setUser(user);
            envelope.setType(EnvelopeType.LOGIN_REQUEST);
            envelope.setPayload(loginRequest);
        }
        return envelope;
    }

}