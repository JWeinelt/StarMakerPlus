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
