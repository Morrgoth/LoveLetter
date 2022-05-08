package bb.love_letter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Util {
    public static Envelope jsonToEnvelope(String json) {
        Gson gson = new Gson();
        Envelope envelope = gson.fromJson(json, Envelope.class);
        return envelope;
    }

    public static Envelope deserializeJsontoEnvelope(String json) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        Envelope envelope = new Envelope();
        if (jsonObject.get("type").getAsString().equals("USEREVENT")) {
            UserEvent userEvent = gson.fromJson(jsonObject.get("payload").getAsString(), UserEvent.class);
            envelope.setType(Envelope.TypeEnum.USEREVENT);
            envelope.setPayload(userEvent);
        } else if (jsonObject.get("type").getAsString().equals("CHATMESSAGE")){
           ChatMessage chatMessage = gson.fromJson(jsonObject.get("payload").getAsString(), ChatMessage.class);
           envelope.setType(Envelope.TypeEnum.CHATMESSAGE);
           envelope.setPayload(chatMessage);
        }
        return envelope;
    }
}
