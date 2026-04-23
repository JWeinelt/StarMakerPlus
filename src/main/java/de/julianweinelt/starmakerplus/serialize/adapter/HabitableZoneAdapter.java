package de.julianweinelt.starmakerplus.serialize.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import de.julianweinelt.starmakerplus.editor.model.HabitableZone;

import java.io.IOException;

public class HabitableZoneAdapter extends TypeAdapter<HabitableZone> {

    @Override
    public void write(JsonWriter out, HabitableZone value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginArray();
        out.value(value.getDistanceFromCenter());
        out.value(value.getSize());
        out.endArray();
    }

    @Override
    public HabitableZone read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        in.beginArray();

        float distance = (float) in.nextDouble();
        float size = (float) in.nextDouble();

        in.endArray();

        HabitableZone temp = new HabitableZone();
        temp.setDistanceFromCenter(distance);
        temp.setSize(size);

        return temp;
    }
}