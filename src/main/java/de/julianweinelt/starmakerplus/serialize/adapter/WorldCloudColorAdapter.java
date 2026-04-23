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