package editor.ui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import editor.model.GraphicalObject;

public class LabelDialog extends JDialog {
    private final JTextField nameField = new JTextField();
    private final JComboBox<String> shapeBox = new JComboBox<>(new String[]{"RECT", "OVAL"});
    private final JTextField colorField = new JTextField();
    private final JTextField fontSizeField = new JTextField();

    private final GraphicalObject object;
    private final Runnable repaintCallback;

    public LabelDialog(JFrame parent, GraphicalObject object, Runnable repaintCallback) {
        super(parent, "Custom label Style", true);
        this.object = object;
        this.repaintCallback = repaintCallback;

        setLayout(new GridLayout(6, 2, 10, 10));
        setSize(300, 250);
        setLocationRelativeTo(parent);

        add(new JLabel("Name"));
        nameField.setText(object.getLabelText());
        add(nameField);

        add(new JLabel("Shape"));
        shapeBox.setSelectedItem(object.getLabelShape());
        add(shapeBox);

        add(new JLabel("Color"));
        colorField.setText(colorToString(object.getLabelColor()));
        add(colorField);

        add(new JLabel("FontSize"));
        fontSizeField.setText(String.valueOf(object.getLabelFontSize()));
        add(fontSizeField);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        add(cancelBtn);

        JButton okBtn = new JButton("OK");
        okBtn.addActionListener(e -> {
            applyChanges();
            dispose();
            repaintCallback.run();
        });
        add(okBtn);
    }

    private void applyChanges() {
        object.setLabelText(nameField.getText().trim());
        object.setLabelShape((String) shapeBox.getSelectedItem());

        try {
            object.setLabelColor(Color.decode(colorField.getText().trim()));
        } catch (NumberFormatException e) {
            try {
                object.setLabelColor((Color) Color.class.getField(colorField.getText().trim().toUpperCase()).get(null));
            } catch (Exception ex) {
                object.setLabelColor(Color.LIGHT_GRAY); // fallback
            }
        }

        try {
            int size = Integer.parseInt(fontSizeField.getText().trim());
            object.setLabelFontSize(size);
        } catch (NumberFormatException e) {
            object.setLabelFontSize(12); // default
        }
    }

    private String colorToString(Color color) {
        if (color == null) return "";
        if (color.equals(Color.YELLOW)) return "yellow";
        if (color.equals(Color.RED)) return "red";
        if (color.equals(Color.BLUE)) return "blue";
        if (color.equals(Color.GREEN)) return "green";
        if (color.equals(Color.GRAY)) return "gray";
        if (color.equals(Color.LIGHT_GRAY)) return "lightGray";
        return "#"+Integer.toHexString(color.getRGB()).substring(2);
    }
}
