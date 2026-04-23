package de.julianweinelt.starmakerplus.serialize.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import de.julianweinelt.starmakerplus.editor.util.WorldCloudConfig;

import java.awt.*;
import java.io.IOException;

public class WorldCloudColorAdapter extends TypeAdapter<WorldCloudConfig> {

    @Override
    public void write(JsonWriter out, WorldCloudConfig value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginArray();
        out.value(value.getColor().getRed());
        out.value(value.getColor().getGreen());
        out.value(value.getColor().getBlue());
        out.value(value.getY());
        out.endArray();
    }

    @Override
    public WorldCloudConfig read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        in.beginArray();

        int red = in.nextInt();
        int green = in.nextInt();
        int blue = in.nextInt();
        int y = in.nextInt();

        in.endArray();

        return new WorldCloudConfig(new Color(red, green, blue), y);
    }
}