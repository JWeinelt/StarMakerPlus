package de.julianweinelt.starmakerplus.serialize.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import de.julianweinelt.starmakerplus.editor.model.StarColor;

import java.io.IOException;

public class StarColorAdapter extends TypeAdapter<StarColor> {
    @Override
    public void write(JsonWriter out, StarColor value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.ordinal());
    }

    @Override
    public StarColor read(JsonReader in) throws IOException {
        if (in.peek() == null) {
            in.nextNull();
            return null;
        }

        int ordinal = in.nextInt();

        StarColor[] values = StarColor.values();
        if (ordinal < 0 || ordinal >= values.length) {
            throw new IOException("Invalid StarColor ordinal: " + ordinal);
        }
        return values[ordinal];
    }
}
