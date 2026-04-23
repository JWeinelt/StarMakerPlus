package de.julianweinelt.starmakerplus.editor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class HabitableZone {
    private float distanceFromCenter = 1.0F;
    private float size = 0.85F;
}
