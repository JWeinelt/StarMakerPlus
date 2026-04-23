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

package de.julianweinelt.starmakerplus.serialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.julianweinelt.starmakerplus.editor.model.*;
import de.julianweinelt.starmakerplus.editor.util.Block;
import de.julianweinelt.starmakerplus.editor.util.Entity;
import de.julianweinelt.starmakerplus.editor.util.Vector3D;
import de.julianweinelt.starmakerplus.editor.util.WorldCloudConfig;
import de.julianweinelt.starmakerplus.serialize.adapter.*;

import java.awt.*;

public class GSONCreator {
    public static Gson create() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .registerTypeAdapter(StarType.class, new StarTypeAdapter())
                .registerTypeAdapter(StarColor.class, new StarColorAdapter())
                .registerTypeAdapter(Temperature.class, new TemperatureAdapter())
                .registerTypeAdapter(HabitableZone.class, new HabitableZoneAdapter())
                .registerTypeAdapter(Color.class, new ColorAdapter())
                .registerTypeAdapter(WorldCloudConfig.class, new WorldCloudColorAdapter())
                .registerTypeAdapter(Block.class, new BlockAdapter())
                .registerTypeAdapter(Entity.class, new EntityAdapter())
                .registerTypeAdapter(Vector3D.class, new Vector3DAdapter())
                .create();
    }
}
