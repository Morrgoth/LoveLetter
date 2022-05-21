package bb.love_letter.networking.type_adapters;

import bb.love_letter.networking.data.Envelope;
import bb.love_letter.networking.data.ServerEvent;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 *
 * @author Tolga Engin
 */
public class ServerEventTypeAdapter extends TypeAdapter<ServerEvent> {
    @Override
    public void write(JsonWriter jsonWriter, ServerEvent serverEvent) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("type").value(serverEvent.getServerEventType().toString());
        jsonWriter.name("message").value(serverEvent.getMessage());
        jsonWriter.endObject();
    }

    @Override
    public ServerEvent read(JsonReader jsonReader) throws IOException {
        ServerEvent serverEvent = new ServerEvent();
        String fieldName = null;
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            fieldName = jsonReader.nextName();
            if ("type".equals(fieldName)) {
                String type = jsonReader.nextString();
                serverEvent.setServerEventType(ServerEvent.ServerEventType.valueOf(type));
            }
            if ("message".equals(fieldName)) {
                serverEvent.setMessage(jsonReader.nextString());
            }
        }
        jsonReader.endObject();
        return serverEvent;
    }
}
