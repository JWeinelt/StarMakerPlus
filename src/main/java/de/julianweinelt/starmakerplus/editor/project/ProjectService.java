/*
 * Copyright (C) 2026  Julian Weinelt
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package de.julianweinelt.starmakerplus.editor.project;


import de.julianweinelt.starmakerplus.editor.model.Project;
import de.julianweinelt.starmakerplus.storage.LocalStorage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class ProjectService {

    private static ProjectService INSTANCE;
    private final List<Project> projects = new ArrayList<>();
    private final List<ProjectListener> listeners = new ArrayList<>();

    public ProjectService() {
        INSTANCE = this;
        //loadDemoProjects();
        loadProjects();
    }

    public static ProjectService instance() {
        return INSTANCE;
    }

    public interface ProjectListener {
        void onProjectsChanged();
    }

    public void addListener(ProjectListener l) { listeners.add(l); }
    public void removeListener(ProjectListener l) { listeners.remove(l); }
    private void fireChanged() { listeners.forEach(ProjectListener::onProjectsChanged); }

    public List<Project> getProjects() {
        return List.copyOf(projects);
    }

    public Project createProject(String name, String description, File directory) {
        Project p = new Project(name, description, directory);
        if (!p.getDirectory().exists()) {
            createBaseStructure(p.getDirectory());
            LocalStorage.instance().getProjects().add(new LocalStorage.ProjectLocation(name, directory.getAbsolutePath()));
            LocalStorage.instance().saveProjects();
        }
        projects.add(p);
        fireChanged();
        return p;
    }

    public void removeProject(Project project) {
        projects.remove(project);
        fireChanged();
    }

    private void loadProjects() {
        LocalStorage.instance().getProjects().forEach(projectLoc -> {
            projects.add(new Project(projectLoc.name(), "", new File(projectLoc.path())));
        });
    }

    public void createBaseStructure(File origin) {
        String[] folders = {
                "assets/starsources/biomes",
                "assets/starsources/bodies",
                "assets/starsources/bodies/asteroids",
                "assets/starsources/bodies/moons",
                "assets/starsources/bodies/planets",
                "assets/starsources/bodies/satellites",
                "assets/starsources/bodies/systems",
                "assets/starsources/lang",
                "assets/starsources/structures",
                "assets/starsources/textures/gui/celestialbodies",
        };

        log.info("Creating base structure in {}", origin);
        for (String folder : folders) {
            File f = new File(origin, folder);
            log.debug("Creating {} in {}", f.getName(), folder);
            if (f.mkdirs()) log.debug("Created {}", folder);
        }
        log.info("Base structure created.");
    }
}