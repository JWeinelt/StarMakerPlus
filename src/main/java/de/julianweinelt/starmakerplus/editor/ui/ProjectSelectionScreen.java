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

package de.julianweinelt.starmakerplus.editor.ui;

import de.julianweinelt.starmakerplus.editor.model.Project;
import de.julianweinelt.starmakerplus.editor.project.ProjectService;
import de.julianweinelt.starmakerplus.editor.ui.dialog.NewProjectDialog;
import de.julianweinelt.starmakerplus.editor.ui.theme.DarkTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class ProjectSelectionScreen extends JFrame implements ProjectService.ProjectListener {

    private final ProjectService projectService = ProjectService.instance();

    private JPanel projectListPanel;

    public ProjectSelectionScreen() {
        super("StarMaker Editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setMinimumSize(new Dimension(700, 450));
        setLocationRelativeTo(null);

        projectService.addListener(this);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());


        JPanel sidebar = buildSidebar();
        root.add(sidebar, BorderLayout.WEST);


        JPanel content = buildContent();
        root.add(content, BorderLayout.CENTER);

        setContentPane(root);
    }



    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setLayout(new BorderLayout());


        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBorder(new EmptyBorder(32, 20, 24, 20));

        JLabel starIcon = new JLabel("★");
        starIcon.setFont(new Font("Segoe UI", Font.BOLD, 36));
        starIcon.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel("StarMaker");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Galacticraft Addon Editor");
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        logoPanel.add(starIcon);
        logoPanel.add(Box.createVerticalStrut(8));
        logoPanel.add(titleLabel);
        logoPanel.add(Box.createVerticalStrut(2));
        logoPanel.add(subtitleLabel);

        sidebar.add(logoPanel, BorderLayout.NORTH);


        JPanel actions = buildSidebarActions();
        sidebar.add(actions, BorderLayout.CENTER);

        return sidebar;
    }

    private JPanel buildSidebarActions() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(0, 12, 12, 12));

        panel.add(sidebarButton("＋  New Project", () -> showNewProjectDialog()));
        panel.add(Box.createVerticalStrut(4));
        panel.add(sidebarButton("📂  Open Folder", () -> openFolder()));
        panel.add(Box.createVerticalStrut(16));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(sep);

        panel.add(Box.createVerticalStrut(16));
        panel.add(sidebarButton("⚙  Settings", () -> openSettings()));
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JButton sidebarButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addActionListener(e -> action.run());
        return btn;
    }



    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout());


        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(24, 28, 12, 28));

        JLabel heading = new JLabel("Projects");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(220, 30));
        searchField.putClientProperty("placeholder", "Search projects...");

        header.add(heading, BorderLayout.WEST);
        header.add(searchField, BorderLayout.EAST);

        content.add(header, BorderLayout.NORTH);


        projectListPanel = new JPanel();
        projectListPanel.setLayout(new BoxLayout(projectListPanel, BoxLayout.Y_AXIS));
        projectListPanel.setBorder(new EmptyBorder(0, 20, 20, 20));

        refreshProjectList();

        JScrollPane scroll = new JScrollPane(projectListPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        content.add(scroll, BorderLayout.CENTER);
        return content;
    }



    private void refreshProjectList() {
        projectListPanel.removeAll();
        List<Project> projects = projectService.getProjects();

        if (projects.isEmpty()) {
            JLabel empty = new JLabel("No projects yet. Create one to get started.");
            empty.setBorder(new EmptyBorder(40, 8, 0, 0));
            projectListPanel.add(empty);
        } else {
            for (Project project : projects) {
                projectListPanel.add(buildProjectCard(project));
                projectListPanel.add(Box.createVerticalStrut(8));
            }
        }

        projectListPanel.revalidate();
        projectListPanel.repaint();
    }

    private JPanel buildProjectCard(Project project) {
        JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(project.getName());

        JLabel descLabel = new JLabel(project.getDescription());

        JLabel pathLabel = new JLabel(project.getDirectory().getPath());
        pathLabel.setForeground(new Color(89, 89, 89));

        info.add(nameLabel);
        info.add(Box.createVerticalStrut(3));
        info.add(descLabel);
        info.add(Box.createVerticalStrut(2));
        info.add(pathLabel);


        JPanel right = new JPanel(new BorderLayout());

        JLabel dateLabel = new JLabel(project.getLastModifiedFormatted());
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        right.add(dateLabel, BorderLayout.NORTH);

        card.add(info, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);


        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(DarkTheme.ACCENT),
                        new EmptyBorder(14, 18, 14, 18)
                ));
            }

            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(DarkTheme.BORDER),
                        new EmptyBorder(14, 18, 14, 18)
                ));
            }

            public void mouseClicked(MouseEvent e) {
                openProject(project);
            }
        });

        return card;
    }



    private void openProject(Project project) {
        EditorScreen editor = new EditorScreen(project);
        editor.setVisible(true);
        dispose();
    }

    private void showNewProjectDialog() {
        NewProjectDialog dialog = new NewProjectDialog(this);
        dialog.setVisible(true);
    }

    private void openFolder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Open Project Folder");
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File dir = chooser.getSelectedFile();
            Project p = projectService.createProject(dir.getName(), "", dir);
            openProject(p);
        }
    }

    private void openSettings() {
        JOptionPane.showMessageDialog(this,
                "Settings coming soon.", "Settings",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onProjectsChanged() {
        SwingUtilities.invokeLater(this::refreshProjectList);
    }
}
