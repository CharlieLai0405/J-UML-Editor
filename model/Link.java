package editor.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

public class Link {
    private int x1, y1, x2, y2;
    private final LinkType type;

    //  新增：動態連結的物件
    private GraphicalObject fromObject;
    private GraphicalObject toObject;

    //  原本 constructor：保留給滑鼠畫線（座標）
    public Link(int x1, int y1, int x2, int y2, LinkType type) {
        this.x1 = x1; this.y1 = y1;
        this.x2 = x2; this.y2 = y2;
        this.type = type;
    }

    //  新增 constructor：從物件建立連線（會自動追蹤）
    public Link(GraphicalObject from, GraphicalObject to, LinkType type) {
        this.fromObject = from;
        this.toObject = to;
        this.type = type;
        //updateFromObjects(); // 初始化時也先更新座標
        attachToNearestPorts();
    }

    public void draw(Graphics g) {
    //  嘗試從物件更新座標（不會中斷畫線）
    if (fromObject != null && toObject != null) {
        //updateFromObjects(); // 更新 x1, y1, x2, y2
        attachToNearestPorts();
    }

    g.setColor(Color.BLACK);
    g.drawLine(x1, y1, x2, y2);

    switch (type) {
        case ASSOCIATION -> drawLineArrow(g);
        case GENERALIZATION -> drawTriangleArrow(g);
        case COMPOSITION -> drawDiamondArrow(g);
    }
}

private void attachToNearestPorts() {
    if (fromObject == null || toObject == null) return;

    Point bestFrom = null, bestTo = null;
    double minDist = Double.MAX_VALUE;

    // Step 1️：先篩出「上下左右中點」的 port（假設中點會在 port index 4~7）
    var fromPorts = fromObject.getConnectionPorts();
    var toPorts = toObject.getConnectionPorts();

    var fromPreferred = getPreferredPorts(fromPorts);
    var toPreferred = getPreferredPorts(toPorts);

    // Step 2️：先試著從 preferred 中找到最短距離
    for (Point p1 : fromPreferred) {
        for (Point p2 : toPreferred) {
            double dist = p1.distance(p2);
            if (dist < minDist) {
                minDist = dist;
                bestFrom = p1;
                bestTo = p2;
            }
        }
    }

    // Step 3️：如果沒找到，再 fallback 用全部 port
    if (bestFrom == null || bestTo == null) {
        for (Point p1 : fromPorts) {
            for (Point p2 : toPorts) {
                double dist = p1.distance(p2);
                if (dist < minDist) {
                    minDist = dist;
                    bestFrom = p1;
                    bestTo = p2;
                }
            }
        }
    }

    // 設定最終連線點
    if (bestFrom != null && bestTo != null) {
        this.x1 = bestFrom.x;
        this.y1 = bestFrom.y;
        this.x2 = bestTo.x;
        this.y2 = bestTo.y;
    }
}

private java.util.List<Point> getPreferredPorts(java.util.List<Point> ports) {
    if (ports.size() >= 8) {
        // Rect：取上下左右中點（index 4~7）
        return ports.subList(4, 8);
    } else if (ports.size() == 4) {
        // Oval：全部就是上下左右中點
        return ports;
    }
    return new java.util.ArrayList<>(); // fallback 空集合
}


    //  若有指定物件，則更新座標
//    private void updateFromObjects() {
//    if (fromObject != null && toObject != null) {
//        Point p1 = getConnectionPoint(fromObject, toObject);
//        Point p2 = getConnectionPoint(toObject, fromObject);
//        this.x1 = p1.x;
//        this.y1 = p1.y;
//        this.x2 = p2.x;
//        this.y2 = p2.y;
//    }
//}

//    private Point getConnectionPoint(GraphicalObject from, GraphicalObject to) {
//    int fx = from.getX(), fy = from.getY(), fw = from.getWidth(), fh = from.getHeight();
//    int tx = to.getX(), ty = to.getY(), tw = to.getWidth(), th = to.getHeight();
//
//    int cx = fx + fw / 2;
//    int cy = fy + fh / 2;
//
//    int txCenter = tx + tw / 2;
//    int tyCenter = ty + th / 2;
//
//    int dx = txCenter - cx;
//    int dy = tyCenter - cy;
//
    // 判斷優先接近哪個方向（水平或垂直）
//    if (Math.abs(dx) > Math.abs(dy)) {
        // 向左或右連
//        if (dx > 0) {
//            return new Point(fx + fw, cy); // 右邊中心
//        } else {
//            return new Point(fx, cy); // 左邊中心
//        }
//    } else {
//        // 向上或下連
//        if (dy > 0) {
//            return new Point(cx, fy + fh); // 下邊中心
//        } else {
//            return new Point(cx, fy); // 上邊中心
//        }
//    }
//}


    private void drawLineArrow(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int len = 12;
        int arrowX1 = (int)(x2 - len * Math.cos(angle - Math.PI / 10));
        int arrowY1 = (int)(y2 - len * Math.sin(angle - Math.PI / 10));
        int arrowX2 = (int)(x2 - len * Math.cos(angle + Math.PI / 10));
        int arrowY2 = (int)(y2 - len * Math.sin(angle + Math.PI / 10));
        g2.drawLine(x2, y2, arrowX1, arrowY1);
        g2.drawLine(x2, y2, arrowX2, arrowY2);
    }

    private void drawTriangleArrow(Graphics g) {
        drawArrowHead(g, x2, y2, x1, y1, true, 18, 14);
    }

    private void drawDiamondArrow(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Polygon diamond = createDiamond(x2, y2, x1, y1, 14, 8); // length, width
        g2.setColor(Color.WHITE);
        g2.fillPolygon(diamond);
        g2.setColor(Color.BLACK);
        g2.drawPolygon(diamond);
    }

    private void drawArrowHead(Graphics g, int tipX, int tipY, int fromX, int fromY, boolean hollow, int length, int width) {
        Graphics2D g2 = (Graphics2D) g;
        Polygon arrow = createTriangle(tipX, tipY, fromX, fromY, length, width);
        if (hollow) {
            g2.setColor(Color.WHITE);
            g2.fillPolygon(arrow);
            g2.setColor(Color.BLACK);
            g2.drawPolygon(arrow);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillPolygon(arrow);
        }
    }

    private Polygon createTriangle(int tipX, int tipY, int fromX, int fromY, int length, int width) {
        double angle = Math.atan2(tipY - fromY, tipX - fromX);
        int[] xPoints = {
            tipX,
            (int)(tipX - length * Math.cos(angle - Math.toRadians(20))),
            (int)(tipX - length * Math.cos(angle + Math.toRadians(20)))
        };
        int[] yPoints = {
            tipY,
            (int)(tipY - length * Math.sin(angle - Math.toRadians(20))),
            (int)(tipY - length * Math.sin(angle + Math.toRadians(20)))
        };
        return new Polygon(xPoints, yPoints, 3);
    }

    private Polygon createDiamond(int tipX, int tipY, int fromX, int fromY, int length, int width) {
        double angle = Math.atan2(tipY - fromY, tipX - fromX);

        int frontX = tipX;
        int frontY = tipY;

        int backX = (int)(frontX - 2 * length * Math.cos(angle));
        int backY = (int)(frontY - 2 * length * Math.sin(angle));

        int leftX = (int)(frontX - length * Math.cos(angle) - width * Math.sin(angle));
        int leftY = (int)(frontY - length * Math.sin(angle) + width * Math.cos(angle));

        int rightX = (int)(frontX - length * Math.cos(angle) + width * Math.sin(angle));
        int rightY = (int)(frontY - length * Math.sin(angle) - width * Math.cos(angle));

        return new Polygon(
            new int[]{frontX, leftX, backX, rightX},
            new int[]{frontY, leftY, backY, rightY},
            4
        );
    }
}
