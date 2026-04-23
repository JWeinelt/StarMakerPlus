package de.julianweinelt.starmakerplus.editor.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class Moon {
    @SerializedName("parent_system")
    private String parentSystem;
    @SerializedName("parent_planet")
    private String parentPlanet;
}
