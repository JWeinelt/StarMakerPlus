package de.julianweinelt.starmakerplus.editor.ui;

import de.julianweinelt.starmakerplus.editor.render.BlockRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Consumer;

public class BlockSelectionDialog extends JDialog {

    private final JPanel gridPanel;

    public BlockSelectionDialog(Frame parent, List<String> blocks, BlockRenderer renderer, Consumer<String> onSelect) {
        super(parent, "Select a block", true);

        setSize(900, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        gridPanel = new JPanel(new GridLayout(0, 10));
        setResizable(false);

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        add(searchField, BorderLayout.NORTH);

        add(scrollPane, BorderLayout.CENTER);

        loadBlocks(blocks, renderer, onSelect);
    }

    private void loadBlocks(List<String> blocks, BlockRenderer renderer, Consumer<String> onSelect) {
        for (String block : blocks) {
            BufferedImage image = renderer.render(block);

            if (image == null) continue;

            Image scaled = image.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaled);

            JButton button = new JButton(icon);
            button.setPreferredSize(new Dimension(40, 40));
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder());

            button.setToolTipText(block);

            button.addActionListener(e -> {
                onSelect.accept(block);
                dispose();
            });

            gridPanel.add(button);
        }
    }
}