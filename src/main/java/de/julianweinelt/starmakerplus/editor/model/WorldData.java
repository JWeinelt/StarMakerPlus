package de.julianweinelt.starmakerplus.editor.model;

import com.google.gson.annotations.SerializedName;
import de.julianweinelt.starmakerplus.editor.util.Block;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class WorldData {
    private int tier = 6;
    private boolean generateCaves = true;
    private boolean generateRavine = false;
    private int craterGenerationFrequency = 0;

    @SerializedName("stone_block")
    private Block stoneBlock = new Block("stone");
    private float mapSize = 1000.0F;
    @SerializedName("water_block")
    private Block waterBlock = new Block("water");

    @SerializedName("waterY")
    private int waterLevel = 124;
    @SerializedName("lander_type")
    private int landerType = -1;

    private float meteorFrequency = 0.5F;
    private float fallDamageModifier = 0.9F;
    private float fuelUsageModifier = 0.5F;

    //TODO: Ring textures

}
