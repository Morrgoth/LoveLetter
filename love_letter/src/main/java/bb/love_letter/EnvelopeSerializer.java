package bb.love_letter;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * This is the custom JsonSerializer we use to convert Envelope objects to JSON.
 * The overriden serialize method must handle each Object type which might be the payload of an Envelope
 * individually.
 *
 * Currently it can handle: UserEvent, ChatMessage
 *
 * @author Bence Ament
 */
public class EnvelopeSerializer implements JsonSerializer<Envelope> {

    @Override
    public JsonElement serialize(Envelope envelope, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(envelope.getType().toString()));
        if (envelope.getType() == Envelope.TypeEnum.USEREVENT) {
            UserEvent userEvent = (UserEvent) envelope.getPayload();
            String payloadString = new Gson().toJson(userEvent);
            result.add("payload", new JsonPrimitive(payloadString));
        } else if (envelope.getType() == Envelope.TypeEnum.CHATMESSAGE) {
            ChatMessage chatMessage = (ChatMessage) envelope.getPayload();
            User user = chatMessage.getSender();
            String userString = new Gson().toJson(user);
            String payloadString = new Gson().toJson(chatMessage);
            result.add("payload", new JsonPrimitive(payloadString));
            result.add("user", new JsonPrimitive(userString));
        }
        return result;
    }
}
