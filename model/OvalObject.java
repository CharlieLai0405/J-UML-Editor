package editor.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class OvalObject extends GraphicalObject {
    public OvalObject(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawOval(x, y, width, height);
        if (selected) {
            g.setColor(Color.BLUE);
            for (Point p : getConnectionPorts()) {
                g.fillRect(p.x - 3, p.y - 3, 6, 6);
            }
        }
        drawLabel(g);
    }

    @Override
    public boolean contains(int px, int py) {
        double rx = width / 2.0;
        double ry = height / 2.0;
        double cx = x + rx;
        double cy = y + ry;
        return Math.pow((px - cx) / rx, 2) + Math.pow((py - cy) / ry, 2) <= 1;
    }

    @Override
    public List<Point> getConnectionPorts() {
        List<Point> ports = new ArrayList<>();
        int cx = x + width / 2;
        int cy = y + height / 2;
        ports.add(new Point(cx, y));              // 上
        ports.add(new Point(x + width, cy));      // 右
        ports.add(new Point(cx, y + height));     // 下
        ports.add(new Point(x, cy));              // 左
        return ports;
    }
}
