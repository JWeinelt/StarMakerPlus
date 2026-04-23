/*
 * Copyright (C) 2026 Julian Weinelt
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

package de.julianweinelt.starmakerplus.editor.ui;

import de.julianweinelt.starmakerplus.editor.model.Planet;
import de.julianweinelt.starmakerplus.editor.model.Project;
import de.julianweinelt.starmakerplus.editor.model.SolarSystem;
import de.julianweinelt.starmakerplus.editor.model.Star;
import de.julianweinelt.starmakerplus.editor.ui.editor.BaseJsonEditor;
import de.julianweinelt.starmakerplus.editor.ui.editor.PlanetEditor;
import de.julianweinelt.starmakerplus.editor.ui.editor.SolarSystemEditor;
import de.julianweinelt.starmakerplus.editor.ui.editor.StarEditor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class EditorScreen extends JFrame {

    private final Project project;
    private final JTabbedPane tabPane;
    private final ProjectTreePanel treePanel;
    private final Map<String, Integer> openTabs = new HashMap<>();

    public EditorScreen(Project project) {
        super("StarMaker Editor - " + project.getName());
        this.project = project;
        project.index();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1280, 800);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        tabPane = buildTabPane();
        treePanel = new ProjectTreePanel(project);
        treePanel.setOnNodeSelected(this::openFile);

        buildUI();
        setJMenuBar(buildMenuBar());
        setupWindowListener();
        openWelcomeTab();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());


        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setBorder(BorderFactory.createEmptyBorder());
        split.setDividerSize(4);
        split.setDividerLocation(240);
        split.setContinuousLayout(true);
        split.setLeftComponent(treePanel);
        split.setRightComponent(tabPane);

        root.add(split, BorderLayout.CENTER);
        root.add(buildStatusBar(), BorderLayout.SOUTH);

        setContentPane(root);
    }


    private JTabbedPane buildTabPane() {
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setBorder(BorderFactory.createEmptyBorder());


        UIManager.put("TabbedPane.tabInsets", new Insets(6, 12, 6, 12));
        return tabs;
    }

    private void openFile(ProjectTreePanel.TreeNodeData data) {
        String fileName = new File(data.filePath()).getName();
        log.info("Opening file: {}", fileName);
        if (openTabs.containsKey(fileName)) {
            tabPane.setSelectedIndex(openTabs.get(fileName));
            return;
        }

        BaseJsonEditor editor = createEditorFor(data);
        if (editor == null) {
            log.warn("Editor is null");
            return;
        }

        int idx = tabPane.getTabCount();
        tabPane.addTab(fileName, editor);
        tabPane.setTabComponentAt(idx, buildTabHeader(fileName, idx));
        tabPane.setSelectedIndex(idx);
        openTabs.put(fileName, idx);
    }

    private BaseJsonEditor createEditorFor(ProjectTreePanel.TreeNodeData data) {
        log.info(data.toJson());
        String fileName = data.filePath();
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".json")) {
            if (data.type().equals(ProjectTreePanel.NodeType.PLANET)) {
                return new PlanetEditor(fileName, project, new Planet()); //TODO: Parse Planet
            } else if (data.type().equals(ProjectTreePanel.NodeType.SOLAR_SYSTEM)) {
                return new SolarSystemEditor(fileName, project, project.parseSolarSystem(fileName));
            }
        }

        if (data.type().equals(ProjectTreePanel.NodeType.STAR)) {
            log.info("Creating star editor for {}", fileName);
            SolarSystem system = project.parseSolarSystem(data.filePath());
            Star star = system.getStar(data.name());
            return new StarEditor(star.getName(), project, star, false, system);
        }
        return null;
    }

    private JPanel buildTabHeader(String title, int tabIndex) {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        header.setOpaque(false);

        JLabel label = new JLabel(title);

        JButton close = new JButton("×");
        close.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        close.setBackground(new Color(0, 0, 0, 0));
        close.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        close.setFocusPainted(false);
        close.setContentAreaFilled(false);
        close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        close.addActionListener(e -> closeTab(title));

        header.add(label);
        header.add(close);
        return header;
    }

    private void closeTab(String fileName) {
        Integer idx = openTabs.remove(fileName);
        if (idx != null) {
            tabPane.removeTabAt(idx);

            openTabs.replaceAll((k, v) -> v > idx ? v - 1 : v);
        }
    }


    private void openWelcomeTab() {
        JPanel welcome = new JPanel(new GridBagLayout());

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel("Welcome to StarMakerPlus Editor");
        icon.setFont(new Font("Segoe UI", Font.BOLD, 64));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Open a file from the project tree");
        title.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hint = new JLabel("Double-click a .json file to start editing");
        hint.setForeground(new Color(0x55, 0x55, 0x70));
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(icon);
        inner.add(Box.createVerticalStrut(16));
        inner.add(title);
        inner.add(Box.createVerticalStrut(8));
        inner.add(hint);

        welcome.add(inner);

        tabPane.addTab("Welcome", welcome);
        tabPane.setTabComponentAt(0, buildTabHeader("Welcome", 0));
    }


    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(0x12, 0x12, 0x20));
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.WHITE),
                new EmptyBorder(3, 12, 3, 12)
        ));
        bar.setPreferredSize(new Dimension(0, 26));

        JLabel left = new JLabel("★  " + project.getName()
                + "   ·   " + project.getDirectory().getPath());

        JLabel right = new JLabel("StarMaker Editor  ·  Galacticraft Addon");
        right.setForeground(new Color(0x44, 0x44, 0x60));

        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }


    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));

        menuBar.add(fileMenu());
        menuBar.add(viewMenu());
        menuBar.add(helpMenu());

        return menuBar;
    }

    private JMenu fileMenu() {
        JMenu menu = menu("File");
        menu.add(menuItem("New File", KeyEvent.VK_N, ActionEvent.CTRL_MASK, e -> {
        }));
        menu.add(menuItem("Save", KeyEvent.VK_S, ActionEvent.CTRL_MASK, e -> {
        }));
        menu.add(menuItem("Save All", KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK, e -> {
        }));
        menu.addSeparator();
        menu.add(menuItem("Close Project", 0, 0, e -> closeProject()));
        menu.addSeparator();
        menu.add(menuItem("Exit", KeyEvent.VK_Q, ActionEvent.CTRL_MASK, e -> System.exit(0)));
        return menu;
    }

    private JMenu viewMenu() {
        JMenu menu = menu("View");
        menu.add(menuItem("Collapse Tree", 0, 0, e -> {
        }));
        return menu;
    }

    private JMenu helpMenu() {
        JMenu menu = menu("Help");
        menu.add(menuItem("About", 0, 0, e -> showAbout()));
        return menu;
    }

    private JMenu menu(String name) {
        JMenu m = new JMenu(name);
        return m;
    }

    private JMenuItem menuItem(String name, int keyCode, int modifiers, java.awt.event.ActionListener action) {
        JMenuItem item = new JMenuItem(name);
        if (keyCode != 0) item.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
        item.addActionListener(action);
        return item;
    }


    private void setupWindowListener() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                closeProject();
            }
        });
    }

    private void closeProject() {
        int result = JOptionPane.showConfirmDialog(this,
                "Close project and return to the project screen?",
                "Close Project", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            new ProjectSelectionScreen().setVisible(true);
            dispose();
        }
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
                "StarMaker Editor\nGalacticraft Addon JSON Generator\n\nVersion 1.0.0-dev",
                "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
