package de.julianweinelt.starmakerplus.serialize.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import de.julianweinelt.starmakerplus.editor.model.StarType;

import java.io.IOException;

public class StarTypeAdapter extends TypeAdapter<StarType> {
    @Override
    public void write(JsonWriter out, StarType value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.ordinal());
    }

    @Override
    public StarType read(JsonReader in) throws IOException {
        if (in.peek() == null) {
            in.nextNull();
            return null;
        }

        int ordinal = in.nextInt();

        StarType[] values = StarType.values();
        if (ordinal < 0 || ordinal >= values.length) {
            throw new IOException("Invalid StarType ordinal: " + ordinal);
        }
        return values[ordinal];
    }
}
