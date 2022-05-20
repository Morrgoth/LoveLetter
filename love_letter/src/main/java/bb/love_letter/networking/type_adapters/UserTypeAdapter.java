package bb.love_letter.networking.type_adapters;

import bb.love_letter.game.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class UserTypeAdapter extends TypeAdapter<User> {


    @Override
    public void write(JsonWriter jsonWriter, User user) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("name").value(user.getName());
        jsonWriter.endObject();
    }

    @Override
    public User read(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        User user = new User();
        String fieldName = null;
        while (jsonReader.hasNext()) {
            fieldName = jsonReader.nextName();
            if ("name".equals(fieldName)) {
                user.setName(jsonReader.nextString());
            }
        }
        jsonReader.endObject();
        return user;
    }
}
