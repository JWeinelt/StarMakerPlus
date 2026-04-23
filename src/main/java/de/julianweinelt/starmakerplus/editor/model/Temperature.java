package de.julianweinelt.starmakerplus.editor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Temperature {
    private float base = 1.0F;
    private float changeModificator = 1.0F;
}