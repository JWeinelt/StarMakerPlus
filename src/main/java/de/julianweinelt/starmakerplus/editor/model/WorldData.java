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
