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
 * @author Zeynab Baiani
 */
public class ChatMessageTypeAdapter extends TypeAdapter<ChatMessage> {
    @Override
    public void write(JsonWriter jsonWriter, ChatMessage chatMessage) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("sender");
        new UserTypeAdapter().write(jsonWriter, chatMessage.getSender());
        jsonWriter.name("message").value(chatMessage.getMessage());
        jsonWriter.name("isPrivate").value(chatMessage.isPrivate());
        jsonWriter.endObject();
    }

    @Override
    public ChatMessage read(JsonReader jsonReader) throws IOException {
        ChatMessage chatMessage = new ChatMessage();
        String fieldName = null;
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            fieldName = jsonReader.nextName();
            if (fieldName.equals("sender")) {
                User user = new UserTypeAdapter().read(jsonReader);
                chatMessage.setSender(user);
            }
            if(fieldName.equals("message")) {
                String message = jsonReader.nextString();
                chatMessage.setMessage(message);
            }
            if(fieldName.equals("isPrivate")) {
                boolean isPrivate = jsonReader.nextBoolean();
                chatMessage.setIsPrivate(isPrivate);
            }
        }
        jsonReader.endObject();
        return chatMessage;
    }
}
