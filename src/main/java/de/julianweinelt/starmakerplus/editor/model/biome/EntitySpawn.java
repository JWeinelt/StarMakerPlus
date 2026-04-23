package de.julianweinelt.starmakerplus.editor.model.biome;

import com.google.gson.annotations.SerializedName;
import de.julianweinelt.starmakerplus.editor.util.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class EntitySpawn {
    private Entity entity;
    private int weight = 15;
    @SerializedName( "min_count")
    private int minCount = 1;
    @SerializedName( "max_count")
    private int maxCount = 3;
}
