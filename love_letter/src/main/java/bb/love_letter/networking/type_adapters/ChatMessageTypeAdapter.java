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

public class ChatMessageTypeAdapter extends TypeAdapter<ChatMessage> {
    @Override
    public void write(JsonWriter jsonWriter, ChatMessage chatMessage) throws IOException {
        Gson gson = new GsonBuilder().create();
        jsonWriter.beginObject();
        jsonWriter.name("sender");
        jsonWriter.value(gson.toJson(chatMessage.getSender()));
        jsonWriter.name("message");
        jsonWriter.value(chatMessage.getMessage());
        jsonWriter.endObject();
    }

    @Override
    public ChatMessage read(JsonReader jsonReader) throws IOException {
        Gson gson = new GsonBuilder().create(); // we have to register the other TypeAdapters here
        ChatMessage chatMessage = new ChatMessage();
        jsonReader.beginObject();
        String fieldName = null;

        while (jsonReader.hasNext()) {
            JsonToken token = jsonReader.peek();

            if (token.equals(JsonToken.NAME)) {
                fieldName = jsonReader.nextName();
            }

            if (fieldName.equals("sender")) {
                token = jsonReader.peek();
                String json = jsonReader.nextString();
                User user = gson.fromJson(json, User.class);
                chatMessage.setSender (user);
            }

            if(fieldName.equals("message")) {
                token = jsonReader.peek();
                String message = jsonReader.nextString();
                chatMessage.setMessage(message);
            }
            if(fieldName.equals("isPrivate")) {
                token = jsonReader.peek();
                boolean isPrivate = jsonReader.nextBoolean();
                chatMessage.setIsPrivate(isPrivate);
            }
        }
        jsonReader.endObject();
        return chatMessage;
    }
}
