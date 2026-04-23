package de.julianweinelt.starmakerplus.editor.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class Star {
    private String name = "example_star";
    @SerializedName("star_size")
    private float starSize = 1.8F;
    @SerializedName("star_type")
    private StarType starType = StarType.DWARF;
    @SerializedName("star_color")
    private StarColor starColor = StarColor.WHITE;


    // Only used for sub-stars
    @SerializedName("star_phase")
    private float starPhase = 6.2F;
    @SerializedName("distance_from_center")
    private float distanceFromCenter = 1.75F;

    // Only used for main stars
    @SerializedName("habitable_zone")
    private HabitableZone habitableZone = new HabitableZone();
}