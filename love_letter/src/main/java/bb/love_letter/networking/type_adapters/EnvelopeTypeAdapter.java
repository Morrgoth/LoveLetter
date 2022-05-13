package bb.love_letter.networking.type_adapters;

import bb.love_letter.networking.data.ChatMessage;
import bb.love_letter.networking.data.Envelope;
import bb.love_letter.networking.data.LoginRequest;
import bb.love_letter.networking.data.ServerEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class EnvelopeTypeAdapter extends TypeAdapter<Envelope> {
    @Override
    public void write(JsonWriter jsonWriter, Envelope envelope) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LoginRequest.class, new LoginRequestTypeAdapter())
                .registerTypeAdapter(ServerEvent.class, new ServerEventTypeAdapter())
                .create(); // we have to register the other TypeAdapters here
        jsonWriter.beginObject();
        jsonWriter.name("type");
        jsonWriter.value(envelope.getType().toString());
        jsonWriter.name("payload");
        if (envelope.getType() == Envelope.EnvelopeType.LOGIN_REQUEST) {
            LoginRequest loginRequest = (LoginRequest) envelope.getPayload();
            String payload = gson.toJson(loginRequest);//, LoginRequest.class);
            jsonWriter.value(payload);
        } else if (envelope.getType() == Envelope.EnvelopeType.SERVER_EVENT) {
            ServerEvent serverEvent = (ServerEvent) envelope.getPayload();
            String payload = gson.toJson(serverEvent);//, ServerEvent.class);
            jsonWriter.value(payload);
        } else if (envelope.getType() == Envelope.EnvelopeType.CHAT_MESSAGE) {
            ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
            String payload = gson.toJson(chatMessage);//, ChatMessage.class);
            jsonWriter.value(payload);
        }
        jsonWriter.endObject();
    }

    @Override
    public Envelope read(JsonReader jsonReader) throws IOException {
        System.out.println(jsonReader)
        Gson gson = new GsonBuilder()
                //.registerTypeAdapter(LoginRequest.class, new LoginRequestTypeAdapter())
                //.registerTypeAdapter(ServerEvent.class, new ServerEventTypeAdapter())
                .create();
        Envelope envelope = new Envelope();
        jsonReader.beginObject();
        String fieldName = null;

        while (jsonReader.hasNext()) {
            JsonToken token = jsonReader.peek();

            if (token.equals(JsonToken.NAME)) {
                fieldName = jsonReader.nextName();
            }

            if (fieldName.equals("type")) {
                token = jsonReader.peek();
                String type = jsonReader.nextString();
                envelope.setType(envelopeTypeFromString(type));
            }

            if(fieldName.equals("payload")) {
                token = jsonReader.peek();
                String payload = jsonReader.nextString();
                if (envelope.getType() == Envelope.EnvelopeType.LOGIN_REQUEST) {
                    LoginRequest loginRequest = gson.fromJson(payload, LoginRequest.class);
                    envelope.setPayload(loginRequest);
                } else if (envelope.getType() == Envelope.EnvelopeType.SERVER_EVENT) {
                    ServerEvent serverEvent = gson.fromJson(payload, ServerEvent.class);
                    envelope.setPayload(serverEvent);
                } else if (envelope.getType() == Envelope.EnvelopeType.CHAT_MESSAGE) {
                    ChatMessage chatMessage = gson.fromJson(payload, ChatMessage.class);
                    envelope.setPayload(chatMessage);
                }
            }
        }
        jsonReader.endObject();
        return envelope;
    }

    private Envelope.EnvelopeType envelopeTypeFromString(String type) {
        if (type.equals("LOGIN_REQUEST")) {
            return Envelope.EnvelopeType.LOGIN_REQUEST;
        } else if (type.equals("SERVER_EVENT")) {
            return Envelope.EnvelopeType.SERVER_EVENT;
        } else if (type.equals("CHAT_MESSAGE")) {
            return Envelope.EnvelopeType.CHAT_MESSAGE;
        } else {
            return null;
        }
    }
}