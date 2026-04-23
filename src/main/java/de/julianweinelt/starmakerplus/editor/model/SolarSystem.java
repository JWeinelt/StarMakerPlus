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

package de.julianweinelt.starmakerplus.editor.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SolarSystem {
    private transient String name;
    private String galaxy;
    private float posX = 3.5F;
    private float posY = 1.5F;

    private List<Star> stars = new ArrayList<>();

    public SolarSystem(String name, String galaxy) {
        this.name = name;
        this.galaxy = galaxy;
    }

    public Star getStar(String name) {
        return stars.stream().filter(s -> s.getName().equals(name)).findFirst().orElse(null);
    }
}
