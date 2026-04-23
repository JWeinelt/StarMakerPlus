package de.julianweinelt.starmakerplus.serialize.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import de.julianweinelt.starmakerplus.editor.util.Entity;

import java.io.IOException;

public class EntityAdapter extends TypeAdapter<Entity> {
    @Override
    public void write(JsonWriter out, Entity value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.nameSpace() + ":" + value.name());
    }

    @Override
    public Entity read(JsonReader in) throws IOException {
        if (in.peek() == null) {
            in.nextNull();
            return null;
        }

        String[] parts = in.nextString().split(":");
        String nameSpace = parts[0];
        String name = parts[1];
        return new Entity(nameSpace, name);
    }
}
