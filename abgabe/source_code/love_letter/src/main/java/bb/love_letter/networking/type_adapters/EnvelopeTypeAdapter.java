package bb.love_letter.networking.type_adapters;

import bb.love_letter.networking.data.ChatMessage;
import bb.love_letter.networking.data.Envelope;
import bb.love_letter.networking.data.LoginRequest;
import bb.love_letter.networking.data.ServerEvent;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 *
 * @author Bence Ament
 */
public class EnvelopeTypeAdapter extends TypeAdapter<Envelope> {
    @Override
    public void write(JsonWriter jsonWriter, Envelope envelope) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("type").value(envelope.getType().toString());
        jsonWriter.name("payload");
        if (envelope.getType() == Envelope.EnvelopeType.LOGIN_REQUEST) {
            LoginRequest loginRequest = (LoginRequest) envelope.getPayload();
            new LoginRequestTypeAdapter().write(jsonWriter, loginRequest);
        } else if (envelope.getType() == Envelope.EnvelopeType.SERVER_EVENT) {
            ServerEvent serverEvent = (ServerEvent) envelope.getPayload();
            new ServerEventTypeAdapter().write(jsonWriter, serverEvent);
        } else if (envelope.getType() == Envelope.EnvelopeType.CHAT_MESSAGE) {
            ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
            new ChatMessageTypeAdapter().write(jsonWriter, chatMessage);
        }
        jsonWriter.endObject();
    }

    @Override
    public Envelope read(JsonReader jsonReader) throws IOException {
        final Envelope envelope = new Envelope();
        jsonReader.beginObject();
        String fieldName;
        while (jsonReader.hasNext()) {
            fieldName = jsonReader.nextName();
            if (fieldName.equals("type")) {
                envelope.setType(Envelope.EnvelopeType.valueOf(jsonReader.nextString()));
            } else if (fieldName.equals("payload")) {
                if (envelope.getType() == Envelope.EnvelopeType.LOGIN_REQUEST) {
                    LoginRequest loginRequest = new LoginRequestTypeAdapter().read(jsonReader);
                    envelope.setPayload(loginRequest);
                } else if (envelope.getType() == Envelope.EnvelopeType.SERVER_EVENT) {
                    ServerEvent serverEvent = new ServerEventTypeAdapter().read(jsonReader);
                    envelope.setPayload(serverEvent);
                } else if (envelope.getType() == Envelope.EnvelopeType.CHAT_MESSAGE) {
                    ChatMessage chatMessage = new ChatMessageTypeAdapter().read(jsonReader);
                    envelope.setPayload(chatMessage);
                }
            }
        }
        jsonReader.endObject();
        return envelope;
    }
}