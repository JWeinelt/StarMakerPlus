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

package de.julianweinelt.starmakerplus;

import com.formdev.flatlaf.FlatDarkLaf;
import de.julianweinelt.starmakerplus.editor.project.ProjectService;
import de.julianweinelt.starmakerplus.editor.render.minecraft.BlockRenderer;
import de.julianweinelt.starmakerplus.editor.ui.BlockSelectionDialog;
import de.julianweinelt.starmakerplus.editor.ui.ProjectSelectionScreen;
import de.julianweinelt.starmakerplus.minecraft.DataExtractor;
import de.julianweinelt.starmakerplus.storage.LocalStorage;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class StarMakerPlus {
    private static File cacheFolder = new File(System.getenv("APPDATA"), "StarMakerPlus/.cache");
    private BlockRenderer blockRenderer;

    private JFrame parent;

    private DataExtractor dataExtractor;

    private static StarMakerPlus instance;

    public static StarMakerPlus instance() {
        return instance;
    }

    static void main(String[] args) {
        instance = new StarMakerPlus();
        instance.start();
    }

    private void start() {
        dataExtractor = new DataExtractor();
        blockRenderer = new BlockRenderer(cacheFolder);
        prepare();
        SwingUtilities.invokeLater(() -> {
            FlatDarkLaf.setup();
            ProjectSelectionScreen screen = new ProjectSelectionScreen();
            parent = screen;
            screen.setVisible(true);
        });
    }

    private void prepare() {
        log.info("StarMakerPlus is starting...");
        log.info("Loading config...");
        new LocalStorage().loadConfig();
        log.info("Loading projects...");
        LocalStorage.instance().loadProjects();
        log.info("Done!");
        log.info("Loading data of Minecraft installation...");
        try {
            DataExtractor.instance().ensureCache();
        } catch (IOException e) {
            log.error("Failed to load cache", e);
        }
        new ProjectService();
    }

    public void openBlockDialog(Consumer<String> block) {
        BlockSelectionDialog dialog = new BlockSelectionDialog(
                parent,
                dataExtractor.getBlocks(),
                blockRenderer::render,
                block
        );

        dialog.setVisible(true);
    }
}
