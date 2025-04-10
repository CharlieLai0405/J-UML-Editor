package editor.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class CompositeObject extends GraphicalObject {
    private final List<GraphicalObject> children = new ArrayList<>();

    public CompositeObject(List<GraphicalObject> selectedObjects) {
        super(0, 0, 0, 0); // 初始化位置稍後會重算
        for (GraphicalObject obj : selectedObjects) {
            children.add(obj);
        }
        recalculateBounds();
    }

    public List<GraphicalObject> getChildren() {
        return children;
    }

    private void recalculateBounds() {
        if (children.isEmpty()) return;
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (GraphicalObject obj : children) {
            minX = Math.min(minX, obj.getX());
            minY = Math.min(minY, obj.getY());
            maxX = Math.max(maxX, obj.getX() + obj.getWidth());
            maxY = Math.max(maxY, obj.getY() + obj.getHeight());
        }

        this.x = minX;
        this.y = minY;
        this.width = maxX - minX;
        this.height = maxY - minY;
    }

    @Override
    public void draw(Graphics g) {
        for (GraphicalObject obj : children) {
            obj.draw(g);
        }
        if (selected) {
            g.setColor(Color.BLUE);
            g.drawRect(x, y, width, height);
        }
        drawLabel(g);
    }

    @Override
    public boolean contains(int px, int py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    @Override
    public void moveTo(int newX, int newY) {
        int dx = newX - x;
        int dy = newY - y;
        for (GraphicalObject obj : children) {
            obj.moveTo(obj.getX() + dx, obj.getY() + dy);
        }
        this.x = newX;
        this.y = newY;
    }

    @Override
    public List<Point> getConnectionPorts() {
        return List.of(
            new Point(x + width / 2, y),              // 上
            new Point(x + width, y + height / 2),     // 右
            new Point(x + width / 2, y + height),     // 下
            new Point(x, y + height / 2)              // 左
        );
    }
}
