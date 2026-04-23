package de.julianweinelt.starmakerplus.editor.render;

import java.awt.image.BufferedImage;

public interface BlockRenderer {
    BufferedImage render(String blockName);
}