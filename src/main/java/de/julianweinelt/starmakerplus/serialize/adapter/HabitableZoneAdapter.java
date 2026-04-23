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