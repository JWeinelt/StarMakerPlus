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

package de.julianweinelt.starmakerplus.editor.ui.editor;

import de.julianweinelt.starmakerplus.StarMakerPlus;
import de.julianweinelt.starmakerplus.editor.model.Project;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public abstract class BaseJsonEditor extends JPanel {

    private final String fileName;


    protected final Map<String, JComponent> fields = new LinkedHashMap<>();


    private final JPanel formPanel = new JPanel(new GridBagLayout());
    private int currentRow = 0;

    private JTextArea jsonPreview;
    private boolean previewVisible = false;

    private final Project project;

    protected BaseJsonEditor(String fileName, Project project) {
        this.fileName = fileName;
        this.project = project;
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);

        formPanel.setBorder(new EmptyBorder(16, 24, 16, 24));


        GridBagConstraints filler = new GridBagConstraints();
        filler.gridy = 999;
        filler.weighty = 1;
        filler.fill = GridBagConstraints.VERTICAL;

        JScrollPane scroll = new JScrollPane(formPanel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        add(scroll, BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }


    protected abstract void buildForm();


    protected abstract String typeIcon();


    public abstract String toJson();


    protected void addSection(String title) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = currentRow++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(currentRow == 1 ? 0 : 18, 0, 6, 0);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel sectionLabel = new JLabel(title.toUpperCase());
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));

        formPanel.add(sectionLabel, gbc);
    }

    protected JTextField addTextField(String key, String label, String placeholder, String initial, Consumer<String> onTextChanged) {
        JTextField field = new JTextField(28);
        field.setToolTipText(placeholder);
        field.setText(initial);
        addFieldRow(key, label, field);

        field.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                onTextChanged.accept(field.getText());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {update();}

            @Override
            public void removeUpdate(DocumentEvent e) {update();}

            @Override
            public void changedUpdate(DocumentEvent e) {update();}
        });
        return field;
    }

    protected JPanel addBlockSelection(String key, String label, Consumer<String> onBlockSelected) {
        JPanel panel = new JPanel(new BorderLayout());
        JTextField field = new JTextField(28);
        field.setToolTipText("Block Name");
        panel.add(field, BorderLayout.CENTER);

        JButton btn = new JButton("...");
        btn.addActionListener(_ -> {
            StarMakerPlus.instance().openBlockDialog(bl -> {
                field.setText(bl);
                onBlockSelected.accept(bl);
            });
        });
        panel.add(btn, BorderLayout.EAST);

        return panel;
    }

    protected JSpinner addIntSpinner(String key, String label, int min, int max, int initial, Consumer<Integer> onValueChanged) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(initial, min, max, 1));
        spinner.setPreferredSize(new Dimension(120, 28));
        addFieldRow(key, label, spinner);
        spinner.addChangeListener(_ -> onValueChanged.accept((int) spinner.getValue()));
        return spinner;
    }

    protected JSpinner addDoubleSpinner(String key, String label, double min, double max, double initial, double step, Consumer<Double> onValueChanged) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(initial, min, max, step));
        spinner.setPreferredSize(new Dimension(120, 28));
        addFieldRow(key, label, spinner);
        spinner.addChangeListener(_ -> onValueChanged.accept((double) spinner.getValue()));
        return spinner;
    }

    protected JCheckBox addCheckBox(String key, String label, boolean initial, Consumer<Boolean> onValueChanged) {
        JCheckBox check = new JCheckBox();
        check.setSelected(initial);
        addFieldRow(key, label, check);
        check.addActionListener(_ -> onValueChanged.accept(check.isSelected()));
        return check;
    }

    protected <T extends Enum<T>> JComboBox<T> addComboBox(
            String key,
            String label,
            Class<T> enumClass,
            Consumer<T> onValueChanged
    ) {
        T[] values = enumClass.getEnumConstants();

        JComboBox<T> combo = new JComboBox<>(values);
        combo.setPreferredSize(new Dimension(200, 28));

        combo.addActionListener(e -> {
            T selected = (T) combo.getSelectedItem();
            if (selected != null) {
                onValueChanged.accept(selected);
            }
        });

        addFieldRow(key, label, combo);
        return combo;
    }

    protected ColorButton addColorPicker(String key, String label, Color initial, Consumer<Color> onColorChanged) {
        ColorButton btn = new ColorButton(initial);
        addFieldRow(key, label, btn);
        btn.addActionListener(e -> onColorChanged.accept(btn.getColor()));
        return btn;
    }


    protected void addCustomRow(String key, String label, JComponent component) {
        addFieldRow(key, label, component);
    }

    private void addFieldRow(String key, String label, JComponent component) {
        fields.put(key, component);

        GridBagConstraints lblGbc = new GridBagConstraints();
        lblGbc.gridx = 0;
        lblGbc.gridy = currentRow;
        lblGbc.anchor = GridBagConstraints.WEST;
        lblGbc.insets = new Insets(4, 0, 4, 16);
        lblGbc.weightx = 0;

        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(160, 26));
        formPanel.add(lbl, lblGbc);

        GridBagConstraints fieldGbc = new GridBagConstraints();
        fieldGbc.gridx = 1;
        fieldGbc.gridy = currentRow;
        fieldGbc.anchor = GridBagConstraints.WEST;
        fieldGbc.fill = GridBagConstraints.HORIZONTAL;
        fieldGbc.weightx = 1;
        fieldGbc.insets = new Insets(4, 0, 4, 0);
        formPanel.add(component, fieldGbc);

        currentRow++;
    }


    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());

        JLabel fileLabel = new JLabel(typeIcon() + "  " + fileName);

        JLabel typeLabel = new JLabel(getEditorTypeName());

        JPanel left = new JPanel(new BorderLayout());
        left.add(fileLabel, BorderLayout.NORTH);
        left.add(typeLabel, BorderLayout.SOUTH);

        header.add(left, BorderLayout.CENTER);
        return header;
    }

    protected String getEditorTypeName() {
        return "JSON Editor";
    }


    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));

        JButton saveBtn = accentButton("💾  Save");
        saveBtn.addActionListener(e -> onSave());

        JButton previewBtn = plainButton("{ }  Preview JSON");
        previewBtn.addActionListener(e -> toggleJsonPreview());

        footer.add(saveBtn);
        footer.add(previewBtn);

        return footer;
    }

    public void onSave() {
        String json = toJson();

        JOptionPane.showMessageDialog(this,
                "JSON saved:\n" + fileName,
                "Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    private void toggleJsonPreview() {
        if (!previewVisible) {
            String json = toJson();
            if (jsonPreview == null) {
                jsonPreview = new JTextArea(json);
                jsonPreview.setForeground(new Color(0xA8, 0xD8, 0x8F));
                jsonPreview.setEditable(false);
                jsonPreview.setBorder(new EmptyBorder(12, 16, 12, 16));
            } else {
                jsonPreview.setText(json);
            }

            JDialog dialog = new JDialog();
            dialog.setTitle("JSON Preview — " + fileName);
            dialog.setSize(500, 400);
            dialog.setLocationRelativeTo(this);

            JScrollPane sp = new JScrollPane(jsonPreview);
            sp.setBorder(BorderFactory.createEmptyBorder());
            dialog.add(sp);
            dialog.setVisible(true);
        }
    }


    protected String getFieldText(String key) {
        JComponent c = fields.get(key);
        if (c instanceof JTextField tf) return tf.getText().trim();
        if (c instanceof JComboBox<?> cb) return String.valueOf(cb.getSelectedItem());
        return "";
    }

    protected Object getFieldValue(String key) {
        JComponent c = fields.get(key);
        if (c instanceof JSpinner sp) return sp.getValue();
        if (c instanceof JCheckBox cb) return cb.isSelected();
        if (c instanceof ColorButton cb) return cb.getHex();
        return getFieldText(key);
    }

    private JButton accentButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton plainButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    protected Project project() {
        return project;
    }


    public static class ColorButton extends JButton {
        private Color color;

        public ColorButton(Color initial) {
            this.color = initial;
            setPreferredSize(new Dimension(60, 26));
            setBackground(color);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addActionListener(e -> {
                Color chosen = JColorChooser.showDialog(this, "Pick Color", color);
                if (chosen != null) {
                    color = chosen;
                    setBackground(color);
                }
            });
        }

        public Color getColor() {
            return color;
        }

        public String getHex() {
            return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
        }
    }
}
