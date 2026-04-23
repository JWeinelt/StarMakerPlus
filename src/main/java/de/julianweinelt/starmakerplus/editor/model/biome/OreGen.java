package de.julianweinelt.starmakerplus.editor.model.biome;

import com.google.gson.annotations.SerializedName;
import de.julianweinelt.starmakerplus.editor.util.Block;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class OreGen {
    @SerializedName("ore_block")
    private Block oreBlock;
    @SerializedName("replaced_block")
    private Block replacedBlock;

    @SerializedName("block_count")
    private int blockCountPerVein = 5;

    private int minY = 5;
    private int maxY = 70;

    @SerializedName("amount_per_chunk")
    private int amountPerChunk = 20;
}
