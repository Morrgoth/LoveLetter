package bb.love_letter.networking;

import bb.love_letter.game.User;
import bb.love_letter.networking.data.ChatMessage;
import bb.love_letter.networking.data.Envelope;
import bb.love_letter.networking.data.LoginRequest;
import bb.love_letter.networking.data.ServerEvent;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * This is the custom JsonSerializer we use to convert Envelope objects to JSON.
 * The override serialize method must handle each Object type which might be the payload of an Envelope
 * individually.
 *
 * Currently, it can handle: UserEvent, ChatMessage
 *
 * @author Bence Ament
 */
public class EnvelopeSerializer implements JsonSerializer<Envelope> {

    @Override
    public JsonElement serialize(Envelope envelope, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(envelope.getType().toString()));
        if (envelope.getType() == Envelope.EnvelopeType.SERVER_EVENT) {
            ServerEvent serverEvent = (ServerEvent) envelope.getPayload();
            String payloadString = new Gson().toJson(serverEvent);
            result.add("payload", new JsonPrimitive(payloadString));
        } else if (envelope.getType() == Envelope.EnvelopeType.CHAT_MESSAGE) {
            ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
            User user = chatMessage.getSender();
            String userString = new Gson().toJson(user);
            String payloadString = new Gson().toJson(chatMessage);
            result.add("payload", new JsonPrimitive(payloadString));
            result.add("user", new JsonPrimitive(userString));
        } else if (envelope.getType() == Envelope.EnvelopeType.LOGIN_REQUEST) {
            LoginRequest loginRequest = (LoginRequest) envelope.getPayload();
            User user = loginRequest.getUser();
            String userString = new Gson().toJson(user);
            String payloadString = new Gson().toJson(loginRequest);
            result.add("payload", new JsonPrimitive(payloadString));
            result.add("user", new JsonPrimitive(userString));
        }
        return result;
    }
}
