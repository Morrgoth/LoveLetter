package bb.love_letter.networking.type_adapters;

import bb.love_letter.game.User;
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

/**
 *
 * @author Bence Ament
 * @author Zeynab Baiani
 * @author Tolga Engin
 */
public class EnvelopeTypeAdapter extends TypeAdapter<Envelope> {
    @Override
    public void write(JsonWriter jsonWriter, Envelope envelope) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("type").value(envelope.getType().toString());
        jsonWriter.name("payload").beginObject();
        if (envelope.getType() == Envelope.EnvelopeType.LOGIN_REQUEST) {
            LoginRequest loginRequest = (LoginRequest) envelope.getPayload();
            User user = loginRequest.getUser();
            jsonWriter.name("user").beginObject();
            jsonWriter.name("name").value(user.getName());
            jsonWriter.endObject();
        } else if (envelope.getType() == Envelope.EnvelopeType.SERVER_EVENT) {
            ServerEvent serverEvent = (ServerEvent) envelope.getPayload();
            jsonWriter.name("type").value(serverEvent.getServerEventType().toString());
            jsonWriter.name("message").value(serverEvent.getMessage());
        } else if (envelope.getType() == Envelope.EnvelopeType.CHAT_MESSAGE) {
            ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
            User sender = chatMessage.getSender();
            jsonWriter.name("message").value(chatMessage.getMessage());
            jsonWriter.name("isPrivate").value(chatMessage.isPrivate());
            jsonWriter.name("sender").beginObject();
            jsonWriter.name("name").value(sender.getName());
            jsonWriter.endObject();
        }
        jsonWriter.endObject();
        jsonWriter.endObject();
    }

    @Override
    public Envelope read(JsonReader jsonReader) throws IOException {
        final Envelope envelope = new Envelope();
        jsonReader.beginObject();
        String fieldName = null;
        while (jsonReader.hasNext()) {
            fieldName = jsonReader.nextName();
            if (fieldName.equals("type")) {
                envelope.setType(Envelope.EnvelopeType.valueOf(jsonReader.nextString()));
            } else if (fieldName.equals("payload")) {
                if (envelope.getType() == Envelope.EnvelopeType.LOGIN_REQUEST) {
                    LoginRequest loginRequest = new LoginRequest();
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        fieldName = jsonReader.nextName();
                        if (fieldName.equals("user")) {
                            jsonReader.beginObject();
                            while (jsonReader.hasNext()) {
                                fieldName = jsonReader.nextName();
                                if (fieldName.equals("name")) {
                                    User user = new User(jsonReader.nextString());
                                    loginRequest.setUser(user);
                                }
                            }
                            jsonReader.endObject();
                        }
                    }
                    jsonReader.endObject();
                    envelope.setPayload(loginRequest);
                } else if (envelope.getType() == Envelope.EnvelopeType.SERVER_EVENT) {
                    ServerEvent serverEvent = new ServerEvent();
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        fieldName = jsonReader.nextName();
                        if (fieldName.equals("type")) {
                            serverEvent.setServerEventType(ServerEvent.ServerEventType.valueOf(jsonReader.nextString()));
                        } else if (fieldName.equals("message")) {
                            serverEvent.setMessage(jsonReader.nextString());
                        }
                    }
                    envelope.setPayload(serverEvent);
                    jsonReader.endObject();
                } else if (envelope.getType() == Envelope.EnvelopeType.CHAT_MESSAGE) {
                    ChatMessage chatMessage = new ChatMessage();
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        fieldName = jsonReader.nextName();
                        if (fieldName.equals("message")) {
                            chatMessage.setMessage(jsonReader.nextString());
                        } else if(fieldName.equals("isPrivate")) {
                            chatMessage.setIsPrivate(jsonReader.nextBoolean());
                        } else if(fieldName.equals("sender")) {
                            jsonReader.beginObject();
                            while(jsonReader.hasNext()) {
                                fieldName = jsonReader.nextName();
                                if(fieldName.equals("name")) {
                                    User user = new User(jsonReader.nextString());
                                    chatMessage.setSender(user);
                                }
                            }
                            jsonReader.endObject();
                        }
                    }
                    envelope.setPayload(chatMessage);
                    jsonReader.endObject();
                }
            }
        }
        jsonReader.endObject();
        return envelope;
    }
}