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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Biome {
    @SerializedName("persistance")
    private float persistence = 1.2F;
    private int octaves = 4;
    @SerializedName("height")
    private int baseHeight = 67;
    private int intquility = 9;

    private float biomeSize = 1.2F;

    @SerializedName("water_color")
    private Color waterColor = new Color(127, 255, 212);
    @SerializedName("foliage_color")
    private Color foliageColor = new Color(40, 92, 153);
    @SerializedName("grass_color")
    private Color grassColor = new Color(57, 101, 160);

    @SerializedName("surface_block")
    private Block surfaceBlock = new Block("grass");

    @SerializedName("subsurface_block")
    private Block subsurfaceBlock = new Block("dirt");

    @SerializedName("oregen")
    private List<OreGen> oreGen = new ArrayList<>();
    @SerializedName("grassgen")
    private List<GrassGen> grassGen = new ArrayList<>();

    @SerializedName("creature_spawnlist")
    private List<EntitySpawn> creatureSpawnList = new ArrayList<>();

    @SerializedName("monster_spawnlist")
    private List<EntitySpawn> monsterSpawnList = new ArrayList<>();

    @SerializedName("water_creature_spawnlist")
    private List<EntitySpawn> waterCreatureSpawnList = new ArrayList<>();

    @SerializedName("nbt_structures")
    private List<NBTStructure> structures = new ArrayList<>();
}
