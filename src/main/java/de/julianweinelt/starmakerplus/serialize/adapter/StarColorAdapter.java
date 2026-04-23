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
