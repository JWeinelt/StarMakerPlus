package de.julianweinelt.starmakerplus.editor.model.biome;

import com.google.gson.annotations.SerializedName;
import de.julianweinelt.starmakerplus.editor.util.Block;
import de.julianweinelt.starmakerplus.editor.util.Vector3D;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class NBTStructure {
    @SerializedName("nbt_file")
    private String nbtFileName;
    @SerializedName("amount_per_chunk")
    private int amountPerChunk = 1;
    @SerializedName("gen_chance")
    private int generationChance = 93;
    @SerializedName("offsetPos")
    private Vector3D offset = new Vector3D(0,0,0);
    @SerializedName("ignore_air")
    private boolean ignoreAir = false;
    @SerializedName("on_block")
    private Block generateOnBlock = new Block("grass");
}
