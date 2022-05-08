package bb.love_letter;

import com.google.gson.*;

import java.lang.reflect.Type;

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
            String payloadString = new Gson().toJson(chatMessage);
            result.add("payload", new JsonPrimitive(payloadString));
        }
        return result;
    }
}
