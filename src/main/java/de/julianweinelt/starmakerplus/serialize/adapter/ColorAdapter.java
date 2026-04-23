package de.julianweinelt.starmakerplus.serialize.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import de.julianweinelt.starmakerplus.editor.model.Temperature;

import java.awt.*;
import java.io.IOException;

public class ColorAdapter extends TypeAdapter<Color> {

    @Override
    public void write(JsonWriter out, Color value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginArray();
        out.value(value.getRed());
        out.value(value.getGreen());
        out.value(value.getBlue());
        out.endArray();
    }

    @Override
    public Color read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        in.beginArray();

        int red = in.nextInt();
        int green = in.nextInt();
        int blue = in.nextInt();

        in.endArray();

        return new Color(red, green, blue);
    }
}