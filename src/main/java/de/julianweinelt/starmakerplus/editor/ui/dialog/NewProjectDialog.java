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

package de.julianweinelt.starmakerplus.editor.ui.dialog;

import de.julianweinelt.starmakerplus.editor.project.ProjectService;
import de.julianweinelt.starmakerplus.storage.LocalStorage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;

public class NewProjectDialog extends JDialog {

    private final JTextField nameField = new JTextField(24);
    private final JTextField descField = new JTextField(24);
    private final JTextField pathField = new JTextField(24);

    private File baseCreationFolder;

    public NewProjectDialog(Frame parent) {
        super(parent, "New Project", true);
        setSize(480, 300);
        setLocationRelativeTo(parent);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBorder(new EmptyBorder(24, 28, 20, 28));

        JLabel title = new JLabel("Create New Project");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setBorder(new EmptyBorder(0, 0, 18, 0));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 4, 8);

        addRow(form, gbc, 0, "Project Name:", nameField);
        baseCreationFolder = LocalStorage.instance().getProjectsFolder();
        addRow(form, gbc, 1, "Description:", descField);

        JPanel pathRow = new JPanel(new BorderLayout(6, 0));
        pathField.setText(baseCreationFolder.getAbsolutePath());
        pathRow.add(pathField, BorderLayout.CENTER);

        nameField.getDocument().addDocumentListener(new DocumentListener() {
            private void updatePath() {
                pathField.setText(new File(baseCreationFolder, nameField.getText()).getAbsolutePath());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updatePath();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updatePath();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updatePath();
            }
        });
        nameField.setText("untitled");

        JLabel pathLabel = new JLabel("Location:");

        JButton browse = new JButton("Browse");
        browse.setFocusPainted(false);
        browse.addActionListener(_ -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("" +
                    ((pathLabel.getText().isEmpty()) ? LocalStorage.instance().getProjectsFolder() : pathLabel.getText())
            ));
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                baseCreationFolder = chooser.getSelectedFile();
            }
        });
        pathRow.add(browse, BorderLayout.EAST);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0;
        form.add(pathLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        form.add(pathRow, gbc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setBorder(new EmptyBorder(16, 0, 0, 0));

        JButton cancel = new JButton("Cancel");
        stylePlainButton(cancel);
        cancel.addActionListener(e -> dispose());

        JButton create = new JButton("Create Project");
        styleAccentButton(create);
        create.addActionListener(_ -> createProject());

        buttons.add(cancel);
        buttons.add(create);

        root.add(title, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void addRow(JPanel form, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        JLabel lbl = new JLabel(labelText);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        form.add(lbl, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(field, gbc);
    }

    private void createProject() {
        String name = nameField.getText().trim();
        String desc = descField.getText().trim();
        String path = pathField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Project name is required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        File dir = new File(path);
        ProjectService.instance().createProject(name, desc, dir);
        dispose();
    }

    private void stylePlainButton(JButton btn) {
        btn.setFocusPainted(false);
    }

    private void styleAccentButton(JButton btn) {
        btn.setFocusPainted(false);
    }
}
