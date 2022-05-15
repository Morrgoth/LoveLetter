package bb.love_letter.networking.type_adapters;

import bb.love_letter.game.User;
import bb.love_letter.networking.data.LoginRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class LoginRequestTypeAdapter extends TypeAdapter<LoginRequest>{
    @Override
    public void write(JsonWriter jsonWriter, LoginRequest loginRequest) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("user");
        new UserTypeAdapter().write(jsonWriter, loginRequest.getUser());
        jsonWriter.endObject();
    }

    @Override
    public LoginRequest read(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        LoginRequest loginRequest = new LoginRequest();
        String fieldName = null;
        while (jsonReader.hasNext()) {
            fieldName = jsonReader.nextName();
            if ("user".equals(fieldName)) {
                User user = new UserTypeAdapter().read(jsonReader);
                loginRequest.setUser(user);
            }
        }
        jsonReader.endObject();
        return loginRequest;
    }
}