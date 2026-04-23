package de.julianweinelt.starmakerplus.editor.ui.editor;

import de.julianweinelt.starmakerplus.editor.model.Project;
import de.julianweinelt.starmakerplus.editor.model.SolarSystem;
import de.julianweinelt.starmakerplus.serialize.GSONCreator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SolarSystemEditor extends BaseJsonEditor {
    private final SolarSystem solarSystem;

    public SolarSystemEditor(String fileName, Project project, SolarSystem solarSystem) {
        super(fileName, project);
        this.solarSystem = solarSystem;

        buildForm();
    }

    @Override
    protected void buildForm() {
        if (solarSystem == null) {
            log.warn("SolarSystem is null");
            log.warn("Normally, that should not happen.");
            return;
        }

        addSection("Basic Info");
        addTextField("galaxy", "Galaxy", "The galaxy this system belongs to", solarSystem.getGalaxy(), solarSystem::setGalaxy);
        addDoubleSpinner("pos_x", "X-Position on Map", -100.0F, 100.0F, solarSystem.getPosX(), 1,
                x -> solarSystem.setPosX(x.floatValue()));
        addDoubleSpinner("pos_y", "Y-Position on Map", -100.0F, 100.0F, solarSystem.getPosY(), 1,
                y -> solarSystem.setPosY(y.floatValue()));
    }

    @Override
    protected String typeIcon() {
        return "";
    }

    @Override
    public String toJson() {
        return GSONCreator.create().toJson(solarSystem);
    }

    @Override
    public void onSave() {
        project().save(solarSystem);
    }
}