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
