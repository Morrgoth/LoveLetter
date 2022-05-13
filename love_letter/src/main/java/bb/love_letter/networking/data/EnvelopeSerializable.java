package bb.love_letter.networking.data;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public interface EnvelopeSerializable {
    public Envelope toEnvelope();
    }