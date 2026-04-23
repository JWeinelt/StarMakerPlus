package de.julianweinelt.starmakerplus.serialize.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import de.julianweinelt.starmakerplus.editor.util.Vector3D;

import java.awt.*;
import java.io.IOException;

public class Vector3DAdapter extends TypeAdapter<Vector3D> {
    @Override
    public void write(JsonWriter out, Vector3D value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginArray();
        out.value(value.x());
        out.value(value.y());
        out.value(value.z());
        out.endArray();
    }

    @Override
    public Vector3D read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        in.beginArray();

        int red = in.nextInt();
        int green = in.nextInt();
        int blue = in.nextInt();

        in.endArray();

        return new Vector3D(red, green, blue);
    }
}
