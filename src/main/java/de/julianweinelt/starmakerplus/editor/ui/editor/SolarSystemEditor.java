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

import de.julianweinelt.starmakerplus.editor.model.Project;
import de.julianweinelt.starmakerplus.editor.model.SolarSystem;
import de.julianweinelt.starmakerplus.serialize.GSONCreator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SolarSystemEditor extends BaseJsonEditor {
    private final SolarSystem solarSystem;

    public SolarSystemEditor(String fileName, Project project, SolarSystem solarSystem) {
        super(fileName, project);
        this.solarSystem = solarSystem;

        buildForm();
    }

    @Override
    protected void buildForm() {
        if (solarSystem == null) {
            log.warn("SolarSystem is null");
            log.warn("Normally, that should not happen.");
            return;
        }

        addSection("Basic Info");
        addTextField("galaxy", "Galaxy", "The galaxy this system belongs to", solarSystem.getGalaxy(), solarSystem::setGalaxy);
        addDoubleSpinner("pos_x", "X-Position on Map", -100.0F, 100.0F, solarSystem.getPosX(), 1,
                x -> solarSystem.setPosX(x.floatValue()));
        addDoubleSpinner("pos_y", "Y-Position on Map", -100.0F, 100.0F, solarSystem.getPosY(), 1,
                y -> solarSystem.setPosY(y.floatValue()));
    }

    @Override
    protected String typeIcon() {
        return "";
    }

    @Override
    public String toJson() {
        return GSONCreator.create().toJson(solarSystem);
    }

    @Override
    public void onSave() {
        project().save(solarSystem);
    }
}