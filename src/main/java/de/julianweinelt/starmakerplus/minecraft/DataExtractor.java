package de.julianweinelt.starmakerplus.minecraft;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
public class DataExtractor {
    private File minecraftFile = new File(System.getenv("APPDATA"), ".minecraft/versions/1.12.2/1.12.2.jar");

    private File cacheFolder = new File(System.getenv("APPDATA"), "StarMakerPlus/.cache");

    @Getter
    private List<String> blocks = new ArrayList<>();

    private static DataExtractor instance;

    public static DataExtractor instance() {
        return instance;
    }
    public DataExtractor() {
        instance = this;
    }


    public void ensureCache() throws IOException {
        if (!cacheFolder.exists() || Objects.requireNonNull(cacheFolder.listFiles()).length == 0) {
            extractAssets();
        }

        blocks = findAllBlocks();
        log.info("Found {} blocks", blocks.size());
    }

    private void extractAssets() throws IOException {
        try (JarFile jar = new JarFile(minecraftFile)) {
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                String name = entry.getName();

                if (!name.startsWith("assets/minecraft/")) continue;

                if (!(name.contains("blockstates") || name.contains("models") || name.contains("textures"))) continue;

                File outFile = new File(cacheFolder, name.replace("assets/minecraft/", ""));
                outFile.getParentFile().mkdirs();

                try (InputStream is = jar.getInputStream(entry);
                     FileOutputStream os = new FileOutputStream(outFile)) {

                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = is.read(buffer)) > 0) {
                        os.write(buffer, 0, len);
                    }
                }
            }
        }
    }

    private List<String> findAllBlocks() {
        File blockStates = new File(cacheFolder, "blockstates");
        File[] files = blockStates.listFiles();
        if (files == null) return List.of();
        List<String> blocks = new ArrayList<>();

        Arrays.stream(files).toList().forEach(file -> {
            if (file.getName().contains("double_slab")) return;
            if (file.getName().contains("glass_pane")) return;
            if (file.getName().contains("fence")) return;
            if (file.getName().contains("wall")) return;
            if (file.getName().contains("redstone_wire")) return;
            if (file.getName().contains("string")) return;
            if (file.getName().contains("chorus_plant")) return;
            blocks.add("minecraft:" + file.getName().replace(".json", ""));
        });
        return blocks;
    }
}
