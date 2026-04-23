/*
 * Copyright (C) 2026  Julian Weinelt
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
