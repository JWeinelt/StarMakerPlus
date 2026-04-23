package de.julianweinelt.starmakerplus.editor.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.julianweinelt.starmakerplus.editor.model.biome.Biome;
import de.julianweinelt.starmakerplus.serialize.GSONCreator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Setter
@Getter
public class Project {
    private final File projectFolderInternal;

    private String name;
    private String description;
    private File directory;
    private LocalDateTime lastModified;

    private List<SolarSystem> solarSystems = new ArrayList<>();
    private List<Biome> biomes = new ArrayList<>();
    private List<Planet> planets = new ArrayList<>();
    //TODO: add asteroids, moons, satellites

    private final File bodies;
    private final File rootFile;
    private final File fSolarSystems;
    private final File fPlanets;
    private final File fBiomes;

    public Project(String name, String description, File directory) {
        this.name = name;
        this.description = description;
        this.directory = directory;
        this.lastModified = LocalDateTime.now();

        projectFolderInternal = new File(directory, ".starmakerplus");

        rootFile = new File(directory, "assets/starsources");
        if (!rootFile.exists()) log.warn("Root folder does not exist: {}", rootFile.getAbsolutePath());

        bodies = new File(rootFile, "bodies");
        if (!bodies.exists()) log.warn("Bodies folder does not exist: {}", bodies.getAbsolutePath());
        fSolarSystems = new File(bodies, "systems");
        if (!fSolarSystems.exists()) log.warn("Solar systems folder does not exist: {}", fSolarSystems.getAbsolutePath());
        fPlanets = new File(bodies, "planets");
        if (!fPlanets.exists()) log.warn("Planets folder does not exist: {}", fPlanets.getAbsolutePath());
        fBiomes = new File(rootFile, "biomes");
        if (!fBiomes.exists()) log.warn("Biomes folder does not exist: {}", fBiomes.getAbsolutePath());
    }

    public String getLastModifiedFormatted() {
        return lastModified.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    public void index() {
        log.info("Starting indexing project: {}", name);
        indexSolarSystems();
    }

    private void indexSolarSystems() {
        log.info("Indexing solar systems");
        File[] files = fSolarSystems.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) continue;
            if (!f.getName().endsWith(".json")) continue;

            JsonObject data = loadJson(f);
            if (data == null) continue;
            List<Star> stars = new ArrayList<>();
            data.getAsJsonArray("stars").forEach(e -> stars.add(GSONCreator.create().fromJson(e, Star.class)));

            SolarSystem solarSystem = new SolarSystem(f.getName().replace(".json", ""),
                    data.get("galaxy").getAsString());
            solarSystem.setStars(stars);
            solarSystem.setPosX(data.get("posX").getAsFloat());
            solarSystem.setPosY(data.get("posY").getAsFloat());
            solarSystems.add(solarSystem);
        }
        log.info("Finished indexing solar systems");
        log.info("Found {} solar systems", solarSystems.size());
    }

    private JsonObject loadJson(File f) {
        try {
            return JsonParser.parseString(Files.readString(f.toPath()).trim()).getAsJsonObject();
        } catch (IOException e) {
            log.error("Failed to load json", e);
            return null;
        }
    }

    public List<String> getGalaxies() {
        List<String> galaxies = new ArrayList<>();
        solarSystems.forEach(s -> {
            if (!galaxies.contains(s.getGalaxy())) galaxies.add(s.getGalaxy());
        });
        return galaxies;
    }

    public SolarSystem parseSolarSystem(String name) {
        return solarSystems.stream().filter(s -> s.getName().equals(name.replace(".json", ""))).findFirst().orElse(null);
    }



    // Save methods
    public boolean save(SolarSystem solarSystem) {
        File f = new File(fSolarSystems, solarSystem.getName() + ".json");
        try (FileWriter w = new FileWriter(f)) {
            w.write(GSONCreator.create().toJson(solarSystem));
        } catch (IOException e) {
            log.error("Failed to save solar system", e);
            return false;
        }
        return true;
    }

    @Override
    public String toString() { return name; }
}