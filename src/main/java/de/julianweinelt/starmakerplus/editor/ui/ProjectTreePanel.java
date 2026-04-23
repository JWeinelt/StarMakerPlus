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

import de.julianweinelt.starmakerplus.editor.model.Project;
import de.julianweinelt.starmakerplus.serialize.GSONCreator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;


@Slf4j
public class ProjectTreePanel extends JPanel {

    private final Project project;
    private final JTree tree;
    @Setter
    private Consumer<TreeNodeData> onNodeSelected;

    public ProjectTreePanel(Project project) {
        this.project = project;
        setLayout(new BorderLayout());


        JPanel header = new JPanel(new BorderLayout());

        JLabel title = new JLabel(project.getName().toUpperCase());
        title.setFont(new Font("Segoe UI", Font.BOLD, 11));

        JButton collapseBtn = smallIconButton();
        collapseBtn.addActionListener(e -> collapseAll());

        header.add(title, BorderLayout.CENTER);
        header.add(collapseBtn, BorderLayout.EAST);


        tree = new JTree(buildTreeModel());
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        tree.setRowHeight(24);
        tree.setCellRenderer(new StarMakerTreeRenderer());

        tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                    if (path == null) {
                        log.warn("Tree path is null");
                        return;
                    }

                    tree.setSelectionPath(path);
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();

                    TreeNodeData data = (TreeNodeData) node.getUserObject();
                    openMenu(tree, data, e.getX(), e.getY());
                }

                if (e.getClickCount() == 2) {
                    TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                    if (path != null) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        if (node.isLeaf() && onNodeSelected != null) {
                            TreeNodeData data = (TreeNodeData) node.getUserObject();
                            onNodeSelected.accept(data);
                        } else if (!node.isLeaf()) {
                            TreeNodeData data = (TreeNodeData) node.getUserObject();
                            if (data.type.equals(NodeType.SOLAR_SYSTEM)) {
                                onNodeSelected.accept(data);
                            }
                        }
                    }
                }
            }
        });


        tree.expandRow(0);

        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPopupMenu openMenu(JTree tree, TreeNodeData data, int x, int y) {
        JPopupMenu menu = new JPopupMenu();

        log.debug("Opening menu for {} ({})", data.filePath, data.type);
        switch (data.type) {
            case PROJECT -> {
                JMenu addMenu = new JMenu("Add");
                addMenu.add("Solar System");
                menu.add(addMenu);
            } case FOLDER -> {
                JMenu addMenu = new JMenu("Add");
                addMenu.add("Solar System");
                addMenu.add("Star");
                addMenu.add("Planet");
                addMenu.add("Moon");
                addMenu.add("Satellite");
                menu.add(addMenu);
            }
        }

        menu.show(tree, x, y);
        return menu;
    }

    private TreeModel buildTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
                new TreeNodeData(project.getName(), NodeType.PROJECT, ""));

        DefaultMutableTreeNode galaxies = folder("Galaxies");
        project.getGalaxies().forEach(galaxy -> {
            DefaultMutableTreeNode g = leaf(galaxy, NodeType.GALAXY, "");
            galaxies.add(g);
            DefaultMutableTreeNode systems = folder("Solar Systems");
            g.add(systems);

            project.getSolarSystems().forEach(system -> {
                DefaultMutableTreeNode s = leaf(system.getName(), NodeType.SOLAR_SYSTEM, system.getName() + ".json");
                DefaultMutableTreeNode stars = folder("Stars");
                s.add(stars);
                s.add(folder("Planets"));
                s.add(folder("Asteroids"));
                systems.add(s);

                system.getStars().forEach(star -> {
                    stars.add(leaf(star.getName(), NodeType.STAR, system.getName()));
                });
            });
        });



        root.add(galaxies);

        return new DefaultTreeModel(root);
    }

    private DefaultMutableTreeNode folder(String name) {
        return new DefaultMutableTreeNode(new TreeNodeData(name, NodeType.FOLDER, ""));
    }

    private DefaultMutableTreeNode leaf(String name, NodeType type, String filePath) {
        return new DefaultMutableTreeNode(new TreeNodeData(name, type, filePath));
    }

    private void collapseAll() {
        for (int i = tree.getRowCount() - 1; i > 0; i--) {
            tree.collapseRow(i);
        }
    }

    private JButton smallIconButton() {
        JButton btn = new JButton("Collapse All");
        btn.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }


    public enum NodeType {
        PROJECT, FOLDER, PLANET, STAR, MOON, SOLAR_SYSTEM, GALAXY
    }

    public record TreeNodeData(String name, NodeType type, String filePath) {
        @Override
        public String toString() {
            return name;
        }

        public String toJson() {
            return GSONCreator.create().toJson(this);
        }
    }


    private static class StarMakerTreeRenderer extends DefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

            if (value instanceof DefaultMutableTreeNode node &&
                    node.getUserObject() instanceof TreeNodeData data) {
                setText(data.name());
            }
            return this;
        }
    }
}
