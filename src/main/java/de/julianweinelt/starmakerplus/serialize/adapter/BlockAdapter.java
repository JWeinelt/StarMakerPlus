/*
 * Copyright (C) 2026 Julian Weinelt
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/.
 */

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
