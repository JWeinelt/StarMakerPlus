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
