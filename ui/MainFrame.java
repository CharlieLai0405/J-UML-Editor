package editor.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import editor.canvas.CanvasPanel;
import editor.controller.EditorController;
import editor.model.GraphicalObject;

public class MainFrame extends JFrame {
    private final EditorController controller;
    private final CanvasPanel canvas;

    public MainFrame() {
        super("Diagram Editor");

        controller = new EditorController();
        canvas = new CanvasPanel(controller);

        setJMenuBar(createMenuBar()); // 加入上方功能表
        setLayout(new BorderLayout());
        add(createToolPanel(), BorderLayout.WEST);
        add(canvas, BorderLayout.CENTER);

        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel createToolPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        String[] modes = {
            "SELECT", "ASSOCIATION", "GENERALIZATION",
            "COMPOSITION", "RECT", "OVAL"
        };

        for (String mode : modes) {
            JButton button = new JButton(mode.toLowerCase());
            button.addActionListener(e -> controller.setMode(mode));
            panel.add(button);
        }

        return panel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu editMenu = new JMenu("Edit");

        JMenuItem groupItem = new JMenuItem("Group");
groupItem.addActionListener(e -> {
    controller.groupSelectedObjects();
    repaint();
});
editMenu.add(groupItem);

JMenuItem ungroupItem = new JMenuItem("Ungroup");
ungroupItem.addActionListener(e -> {
    controller.ungroupSelectedObject();
    repaint();
});
editMenu.add(ungroupItem);

        JMenuItem labelItem = new JMenuItem("Label");
        labelItem.addActionListener(e -> {
            GraphicalObject selected = controller.getSelectedObject();
            if (selected != null) {
                new LabelDialog(this, selected, canvas::repaint).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "請先選取一個物件", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        editMenu.add(labelItem);
        menuBar.add(editMenu);
        return menuBar;
    }
}
