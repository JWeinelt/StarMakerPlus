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

package de.julianweinelt.starmakerplus.editor.ui.editor;

import de.julianweinelt.starmakerplus.editor.model.Planet;
import de.julianweinelt.starmakerplus.editor.model.Project;
import de.julianweinelt.starmakerplus.editor.model.WorldData;
import de.julianweinelt.starmakerplus.editor.util.Block;
import de.julianweinelt.starmakerplus.serialize.GSONCreator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlanetEditor extends BaseJsonEditor {
    private final Planet planet;

    public PlanetEditor(String fileName, Project project, Planet planet) {
        super(fileName, project);
        this.planet = planet;

        buildForm();
    }

    @Override
    protected void buildForm() {
        if (planet == null) {
            log.warn("Planet is null");
            log.warn("Normally, that should not happen.");
            return;
        }

        addSection("Basic Info");
        addTextField("parent_system", "Parent System", "The parent system",
                planet.getParentSystem(), planet::setParentSystem);
        addDoubleSpinner("gravity", "Gravity", 0.058F, 0.08, planet.getGravity(), 0.001,
                gravity -> planet.setGravity(gravity.floatValue()));
        addCheckBox("unreachable", "Can be visited", planet.isUnreachable(),
                visitable -> planet.setUnreachable(!visitable));
        addCheckBox("tidallyLocked", "Tidally Locked", planet.isStopDayNightCycle(),
                planet::setStopDayNightCycle);
        addCheckBox("precipitation", "Precipitation", planet.isPrecipitation(),
                planet::setPrecipitation);
        addDoubleSpinner("sun_size", "Sun Size", 1.0, 10.0, 5.0, 0.5,
                sunSize -> planet.setSunSize(sunSize.floatValue()));

        addSection("Atmosphere & Appearance");

        addIntSpinner("atmosphere_pressure", "Atmosphere Pressure", 1, 10, planet.getAtmospherePressure(),
                planet::setAtmospherePressure);
        addDoubleSpinner("wind_strength", "Wind Strength", 0.0, 100.0, planet.getWindStrength(), 0.01,
                wind -> planet.setWindStrength(wind.floatValue()));
        addIntSpinner("day_length", "Day Length (h)", 1, 48, planet.getDayLength() / 1000,
                length -> planet.setDayLength(length * 1000));

        addCheckBox("breathable", "Breathable Air", planet.isBreathable(),
                planet::setBreathable);
        addCheckBox("solar_radiation", "Solar Radiation", planet.isSolarRadiation(),
                planet::setSolarRadiation);
        addCheckBox("corrosive_atmosphere", "Corrosive Atmosphere", planet.isCorrosiveAtmosphere(),
                planet::setCorrosiveAtmosphere);
        addDoubleSpinner("sun_brightness", "Sun Brightness", 0.1, 2.0, planet.getSunBrightness(), 0.1,
                brightness -> planet.setSunBrightness(brightness.floatValue()));
        addDoubleSpinner("star_brightness", "Star Brightness", 0.1, 2.0, planet.getStarBrightness(), 0.1,
                brightness -> planet.setStarBrightness(brightness.floatValue()));


        addSection("Temperature");
        addDoubleSpinner("temp_temperature", "Base Temperature", -100.0, 100.0, planet.getTemperature().getBase(), 1.0,
                temp -> planet.getTemperature().setBase(temp.floatValue()));
        addDoubleSpinner("temp_modifier", "Temperature Modifier", -10.0, 10.0, planet.getTemperature().getChangeModificator(), 1.0,
                temp -> planet.getTemperature().setChangeModificator(temp.floatValue()));

        addSection("Orbit");
        addDoubleSpinner("phase", "Position around the main star", 0.0, 2.0, planet.getOrbitData().getPhase(), 0.1,
                phase -> planet.getOrbitData().setPhase(phase.floatValue()));
        addDoubleSpinner("size", "Size of planet", 0.0, 2.0, planet.getOrbitData().getSize(), 0.1,
                size -> planet.getOrbitData().setSize(size.floatValue()));
        addDoubleSpinner("distance_from_center", "Distance from Center", 0.0, 20.0, planet.getOrbitData().getDistanceFromCenter(), 0.5,
                size -> planet.getOrbitData().setDistanceFromCenter(size.floatValue()));
        addDoubleSpinner("relative_time", "Relative Time", 0.0, 20.0, planet.getOrbitData().getRelativeTime(), 0.3,
                size -> planet.getOrbitData().setRelativeTime(size.floatValue()));
        addDoubleSpinner("eccentricity_x", "Eccentricity X", -20.0, 20.0, planet.getOrbitData().getEccentricityX(), 0.5,
                size -> planet.getOrbitData().setEccentricityX(size.floatValue()));
        addDoubleSpinner("eccentricity_y", "Eccentricity Y", -20.0, 20.0, planet.getOrbitData().getEccentricityY(), 0.5,
                size -> planet.getOrbitData().setEccentricityY(size.floatValue()));

        addSection("World Data");
        WorldData w = planet.getWorldData();
        addIntSpinner("tier", "Rocket Tier", 1, 10, w.getTier(), w::setTier);
        addCheckBox("gen_caves", "Generate Caves", w.isGenerateCaves(), w::setGenerateCaves);
        addCheckBox("gen_ravine", "Generate Ravine", w.isGenerateRavine(), w::setGenerateRavine);
        addIntSpinner("crater_prob", "Crater Generation Frequency", 0, 10, w.getCraterGenerationFrequency(), w::setCraterGenerationFrequency);

        addBlockSelection("stone_block", "\"Stone\" Block", block -> w.setStoneBlock(new Block(block)));
        addBlockSelection("water_block", "Water Block", block -> w.setWaterBlock(new Block(block)));

        addDoubleSpinner("map_size", "Map Size (biomes)", 500, 2000, w.getMapSize(), 100, size -> w.setMapSize(size.floatValue()));
        addIntSpinner("water_level", "Water Level (Y)", 0, 256, w.getWaterLevel(), w::setWaterLevel);
        //TODO: add lander type
        addDoubleSpinner("meteor_frequency", "Meteor Frequency", 0.1, 2.0, w.getMeteorFrequency(), 0.1,
                size -> w.setMeteorFrequency(size.floatValue()));
        addDoubleSpinner("fall_damage_modifier", "Fall Damage Modifier", 0.1, w.getFallDamageModifier(), 0.5, 0.1,
                size -> w.setFallDamageModifier(size.floatValue()));
        addDoubleSpinner("fuel_usage_modifier", "Fuel Usage Modifier", 0.1, 2.0, w.getFuelUsageModifier(), 0.1,
                size -> w.setFuelUsageModifier(size.floatValue()));

        //TODO: add list of biomes

    }

    @Override
    protected String typeIcon() {
        return "🪐";
    }

    @Override
    protected String getEditorTypeName() {
        return "Planet Definition";
    }

    @Override
    public String toJson() {
        return GSONCreator.create().toJson(planet);
    }
}
