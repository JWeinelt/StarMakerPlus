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

package de.julianweinelt.starmakerplus.editor.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Block {
    private String nameSpace;
    private String name;
    private int metaID;

    public Block(String nameSpace, String name) {
        this.nameSpace = nameSpace;
        this.name = name;
        this.metaID = -1;
    }
    public Block(String name) {
        String[] dat = name.split(":");
        if (dat.length == 1) {
            this.nameSpace = "minecraft";
            this.name = dat[0];
            this.metaID = -1;
        } else if (dat.length == 2) {
            this.nameSpace = dat[0];
            this.name = dat[1];
            this.metaID = -1;
        } else if (dat.length == 3) {
            this.nameSpace = dat[0];
            this.name = dat[1];
            this.metaID = Integer.parseInt(dat[2]);
        } else {
            throw new IllegalArgumentException("Invalid block name: " + name);
        }
    }
}
