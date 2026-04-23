package de.julianweinelt.starmakerplus.serialize.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import de.julianweinelt.starmakerplus.editor.util.Block;

import java.io.IOException;

public class BlockAdapter extends TypeAdapter<Block> {
    @Override
    public void write(JsonWriter out, Block value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.getNameSpace() + ":" + value.getName() + ((value.getMetaID() != -1) ? ":" + value.getMetaID() : ""));
    }

    @Override
    public Block read(JsonReader in) throws IOException {
        if (in.peek() == null) {
            in.nextNull();
            return null;
        }

        String[] parts = in.nextString().split(":");
        String nameSpace = parts[0];
        String name = parts[1];
        int metaID = -1;
        if (parts.length > 2) {
            metaID = Integer.parseInt(parts[2]);
        }
        return new Block(nameSpace, name, metaID);

    }
}
