package de.julianweinelt.starmakerplus.serialize.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import de.julianweinelt.starmakerplus.editor.model.Temperature;

import java.io.IOException;

public class TemperatureAdapter extends TypeAdapter<Temperature> {

    @Override
    public void write(JsonWriter out, Temperature value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        out.beginArray();
        out.value(value.getBase());
        out.value(value.getChangeModificator());
        out.endArray();
    }

    @Override
    public Temperature read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        in.beginArray();

        float base = (float) in.nextDouble();
        float changeMod = (float) in.nextDouble();

        in.endArray();

        Temperature temp = new Temperature();
        temp.setBase(base);
        temp.setChangeModificator(changeMod);

        return temp;
    }
}