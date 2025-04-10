package editor.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class RectObject extends GraphicalObject {
    public RectObject(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        if (selected) {
            g.setColor(Color.BLUE);
            for (Point p : getConnectionPorts()) {
                g.fillRect(p.x - 3, p.y - 3, 6, 6); // 藍色 port
            }
        }
        drawLabel(g);
    }

    @Override
    public boolean contains(int px, int py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    @Override
    public List<Point> getConnectionPorts() {
        List<Point> ports = new ArrayList<>();
        int cx = x + width / 2;
        int cy = y + height / 2;
        ports.add(new Point(x, y));                         // 左上
        ports.add(new Point(x + width, y));                 // 右上
        ports.add(new Point(x, y + height));                // 左下
        ports.add(new Point(x + width, y + height));        // 右下
        ports.add(new Point(cx, y));                        // 上中
        ports.add(new Point(x + width, cy));                // 右中
        ports.add(new Point(cx, y + height));               // 下中
        ports.add(new Point(x, cy));                        // 左中
        return ports;
    }
}
