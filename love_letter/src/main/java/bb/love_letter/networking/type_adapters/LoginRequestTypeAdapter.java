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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new UserTypeAdapter())
                .create();
        jsonWriter.name("user");
        String userJson = gson.toJson(loginRequest.getUser());
        jsonWriter.value(userJson);
        jsonWriter.endObject();
    }

    @Override
    public LoginRequest read(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new UserTypeAdapter())
                .create();
        LoginRequest loginRequest = new LoginRequest();
        String fieldName = null;
        while (jsonReader.hasNext()) {
            JsonToken token = jsonReader.peek();
            if(token.equals(JsonToken.NAME)) {
                fieldName =jsonReader.nextString();
            }
            if ("user".equals(fieldName)) {
                token = jsonReader.peek();
                String json = jsonReader.nextString();
                loginRequest.setUser(gson.fromJson(json, User.class));
            }
        }
        jsonReader.endObject();
        return null;
    }
}