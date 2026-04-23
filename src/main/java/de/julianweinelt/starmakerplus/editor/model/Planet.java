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
import de.julianweinelt.starmakerplus.editor.util.WorldCloudConfig;
import de.julianweinelt.starmakerplus.serialize.GSONCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Planet {
    @SerializedName("parent_system")
    private String parentSystem = "";
    @SerializedName("orbit_data")
    private OrbitData orbitData = new OrbitData();

    private float gravity = 0.058F;
    @SerializedName("atmosphere_pressure")
    private int atmospherePressure = 1;

    private Temperature temperature = new Temperature();

    @SerializedName("wind")
    private float windStrength = 0.0F;

    @SerializedName("day_lenght")
    private int dayLength = 24000;

    private boolean breathable = false;
    @SerializedName("solar_radiation")
    private boolean solarRadiation = false;
    @SerializedName("corrosive_atmo")
    private boolean corrosiveAtmosphere = false;

    @SerializedName("sun_brightness")
    private float sunBrightness = 0.5F;
    @SerializedName("star_brightness")
    private float starBrightness = 0.5F;

    @SerializedName("sky")
    private Color skyColor = new Color(127, 255, 212);
    @SerializedName("fog")
    private Color fogColor = new Color(127, 255, 212);
    @SerializedName("cloud")
    private WorldCloudConfig cloudColor = new WorldCloudConfig();
    @SerializedName("light")
    private Color lightColor = new Color(255, 50, 50);

    @SerializedName("world_data")
    private WorldData worldData = new WorldData();

    private List<String> biomes = new ArrayList<>();

    @SerializedName("sun_size")
    private float sunSize = 5.0F;
    private boolean precipitation = false;
    private boolean unreachable = false;
    @SerializedName("tidallyLocked")
    private boolean stopDayNightCycle = false;

    @Override
    public String toString() {
        return GSONCreator.create().toJson(this);
    }
}
