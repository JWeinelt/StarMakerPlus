package de.julianweinelt.starmakerplus.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LocalStorage {
    private final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    private static LocalStorage INSTANCE;

    private final File baseFolder = new File(System.getenv("APPDATA"), "StarMakerPlus");
    private final File configFile = new File(baseFolder, "config.json");
    @Getter
    private final File projectsFolder;
    private final File projectFile = new File(baseFolder, "projects.json");

    @Getter
    private Configuration configuration;

    @Getter
    private final List<ProjectLocation> projects = new ArrayList<>();

    public LocalStorage() {
        INSTANCE = this;
        String home = System.getenv("USERPROFILE");
        if (home == null) home = System.getenv("HOME");
        projectsFolder = new File(home, "SMPlusProjects");
        log.info("Base dir is at {}", projectsFolder.getAbsolutePath());
        if (baseFolder.mkdirs()) log.debug("Created base folder: {}", baseFolder.getAbsolutePath());
        if (projectsFolder.mkdirs()) log.debug("Created projects folder: {}", projectsFolder.getAbsolutePath());
    }

    public static LocalStorage instance() {
        return INSTANCE;
    }

    public void saveConfig() {
        try (FileWriter w = new FileWriter(configFile)) {
            w.write(GSON.toJson(configuration));
        } catch (IOException e) {
            log.error("Failed to save config", e);
        }
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            log.warn("Config file does not exist: {}", configFile.getAbsolutePath());
            saveConfig();
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
            configuration = GSON.fromJson(br, Configuration.class);
        } catch (IOException e) {
            log.error("Failed to load config", e);
        }
    }

    public void saveProjects() {
        try (FileWriter w = new FileWriter(projectFile)) {
            w.write(GSON.toJson(projects));
        } catch (IOException e) {
            log.error("Failed to save projects", e);
        }
    }

    public void loadProjects() {
        if (!projectFile.exists()) {
            log.warn("Projects file does not exist: {}", configFile.getAbsolutePath());
            saveProjects();
            return;
        }
        projects.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(projectFile))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) sb.append(line);
            Type type = new TypeToken<List<ProjectLocation>>(){}.getType();
            projects.addAll(GSON.fromJson(sb.toString(), type));
        } catch (IOException e) {
            log.error("Failed to load projects", e);
        }
    }

    public File projectFolder(String projectName) {
        return new File(projectsFolder, projectName);
    }

    public record ProjectLocation(String name, String path) {}
}
