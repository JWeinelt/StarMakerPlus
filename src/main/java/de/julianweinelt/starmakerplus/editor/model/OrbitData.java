package de.julianweinelt.starmakerplus.editor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class OrbitData {
    private float phase = 1.14F;
    private float size = 1.2F;
    private float distanceFromCenter = 2.5F;
    private float relativeTime = 3.9F;
    private float eccentricityX = 0.0F;
    private float eccentricityY = 0.0F;
}
