package de.julianweinelt.starmakerplus.editor.ui.editor;

import de.julianweinelt.starmakerplus.editor.model.*;
import de.julianweinelt.starmakerplus.serialize.GSONCreator;

import javax.swing.*;

public class StarEditor extends BaseJsonEditor {
    private final Star star;
    private final boolean isSubStar;
    private final SolarSystem solarSystem;

    public StarEditor(String fileName, Project project, Star star, boolean isSubStar, SolarSystem solarSystem) {
        super(fileName, project);
        this.star = star;
        this.isSubStar = isSubStar;
        this.solarSystem = solarSystem;

        buildForm();
    }

    @Override
    protected void buildForm() {
        addSection("Basic Info");
        addTextField("name", "Name", "The name of the star", star.getName(), star::setName);
        addDoubleSpinner("size", "Star Size", 1.0, 4.0, star.getStarSize(), 0.1,
                size -> star.setStarSize(size.floatValue()));
        addComboBox("star_type", "Star Type", StarType.class, star::setStarType);
        addComboBox("star_color", "Star Color", StarColor.class, star::setStarColor);

        if (isSubStar) {
            addSection("Sub Star Properties");
            addDoubleSpinner("star_phase", "Star Phase", 0.0, 2.0, star.getStarPhase(), 0.1,
                    phase -> star.setStarPhase(phase.floatValue()));
            addDoubleSpinner("distance_from_center", "Distance from Center", 0.0, 20.0, star.getDistanceFromCenter(), 0.5,
                    distance -> star.setDistanceFromCenter(distance.floatValue()));
        } else {
            addSection("Main Star Properties (Habitable Zone)");
            addDoubleSpinner("zone_distance", "Distance from Center", 0, 100, star.getHabitableZone().getDistanceFromCenter(), 0.1,
                    distance -> star.getHabitableZone().setDistanceFromCenter(distance.floatValue()));
            addDoubleSpinner("zone_size", "Size", 0, 100, star.getHabitableZone().getSize(), 0.1,
                    distance -> star.getHabitableZone().setSize(distance.floatValue()));
        }
    }

    @Override
    protected String typeIcon() {
        return "";
    }

    @Override
    public String toJson() {
        return GSONCreator.create().toJson(star);
    }

    @Override
    public void onSave() {
        boolean success = project().save(solarSystem);
        if (success) {
            JOptionPane.showMessageDialog(this, "Star saved");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save star");
        }
    }
}
