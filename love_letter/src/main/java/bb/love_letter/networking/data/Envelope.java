package bb.love_letter.networking.data;

import bb.love_letter.networking.type_adapters.EnvelopeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Envelope is used for Client-Server communication, by convention whenever we exchange data betweeen Client and Server
 * it must be wrapped by the sender in an Envelope and turned to JSON, and it must be deserialized from JSON by
 * the receiver and then unwrapped.
 *
 * @author Muqiu Wang
 */
public class Envelope {
    private EnvelopeSerializable payload;
    private EnvelopeType type;

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
    public Envelope(EnvelopeSerializable payload, EnvelopeType type){
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
    public void setPayload(EnvelopeSerializable payload) {
        this.payload = payload;
    }
    public void setType(EnvelopeType type) {
        this.type = type;
    }

    /**
     * @return JSON String to be sent through the network.
     */
    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Envelope.class, new EnvelopeTypeAdapter()).create();
        return gson.toJson(this);
    }

    /**
     * This static method is used to recover an Envelope from its serialized JSON format.
     * @param json This is a JSON String received through the network.
     * @return An Envelope that has the same contents as the one which was originally sent through the Network.
     */
    public static Envelope fromJson(String json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Envelope.class, new EnvelopeTypeAdapter()).create();
        Envelope envelope = gson.fromJson(json, Envelope.class);
        return envelope;
    }

}