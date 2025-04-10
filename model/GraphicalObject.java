package editor.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

public abstract class GraphicalObject {
    protected int x, y, width, height;
    protected boolean selected = false;

    protected String label = "";
    protected String labelShape = "RECT";
    protected Color labelColor = Color.WHITE;
    protected int labelFontSize = 12;

    public GraphicalObject(int x, int y, int width, int height) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
    }

    public abstract void draw(Graphics g);
    public abstract boolean contains(int px, int py);

    //  新增：每個物件需提供自己的連線 port 清單
    public abstract List<Point> getConnectionPorts();

    public void moveTo(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setLabelStyle(String shape, Color color, int fontSize) {
        this.labelShape = shape;
        this.labelColor = color;
        this.labelFontSize = fontSize;
    }

    protected void drawLabel(Graphics g) {
        if (label == null || label.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        Font font = new Font("SansSerif", Font.PLAIN, labelFontSize);
        g2.setFont(font);
        FontMetrics metrics = g2.getFontMetrics(font);
        int textWidth = metrics.stringWidth(label);
        int textHeight = metrics.getHeight();

        int centerX = x + width / 2;
        int centerY = y + height / 2;
        int rectX = centerX - textWidth / 2 - 5;
        int rectY = centerY - textHeight / 2 - 2;
        int rectW = textWidth + 10;
        int rectH = textHeight;

        g2.setColor(labelColor);

        if (labelShape.equalsIgnoreCase("ORAL") || labelShape.equalsIgnoreCase("OVAL")) {
            g2.fillOval(rectX, rectY, rectW, rectH);
            g2.setColor(Color.BLACK);
            g2.drawOval(rectX, rectY, rectW, rectH);
        } else {
            g2.fillRect(rectX, rectY, rectW, rectH);
            g2.setColor(Color.BLACK);
            g2.drawRect(rectX, rectY, rectW, rectH);
        }

        g2.setColor(Color.BLACK);
        g2.drawString(label, centerX - textWidth / 2, centerY + textHeight / 4);
    }

    public String getLabelText() {
    return label;
}

public String getLabelShape() {
    return labelShape;
}

public Color getLabelColor() {
    return labelColor;
}

public int getLabelFontSize() {
    return labelFontSize;
}

public void setLabelText(String text) {
    this.label = text;
}

public void setLabelShape(String shape) {
    this.labelShape = shape;
}

public void setLabelColor(Color color) {
    this.labelColor = color;
}

public void setLabelFontSize(int size) {
    this.labelFontSize = size;
}


}
