package de.julianweinelt.starmakerplus.editor.model.biome;

import com.google.gson.annotations.SerializedName;
import de.julianweinelt.starmakerplus.editor.util.Block;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class GrassGen {
    @SerializedName("grass_block")
    private Block grassBlock;
    @SerializedName("ground_block")
    private Block groundBlock;

    @SerializedName("grass_count")
    private int grassCount = 10;
    @SerializedName("onWater")
    private boolean generatorOnWater = false;
}