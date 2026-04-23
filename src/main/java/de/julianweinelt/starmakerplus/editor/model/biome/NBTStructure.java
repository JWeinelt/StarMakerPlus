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
