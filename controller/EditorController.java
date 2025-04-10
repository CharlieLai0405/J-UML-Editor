package editor.controller;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import editor.model.CompositeObject;
import editor.model.GraphicalObject;
import editor.model.Link;
import editor.model.LinkType;
import editor.model.OvalObject;
import editor.model.RectObject;

public class EditorController {
    private final List<GraphicalObject> objects = new ArrayList<>();
    private final List<Link> links = new ArrayList<>();
    private String currentMode = "SELECT";

    private int dragStartX, dragStartY, dragEndX, dragEndY;
    private int lastDragX, lastDragY;
    private boolean isDragging = false;
    private boolean isDraggingObjects = false;

    public void setMode(String mode) {
        currentMode = mode;
        System.out.println("Switched to mode: " + mode);
        clearSelection();
    }

    public void handleMousePressed(int x, int y) {
        if (currentMode.equals("RECT")) {
            objects.add(new RectObject(x, y, 100, 60));
        } else if (currentMode.equals("OVAL")) {
            objects.add(new OvalObject(x, y, 100, 60));
        } else if (currentMode.equals("SELECT")) {
            boolean found = false;
            for (int i = objects.size() - 1; i >= 0; i--) {
                GraphicalObject obj = objects.get(i);
                if (obj.contains(x, y)) {
                    if (!obj.isSelected()) {
                        clearSelection();
                        obj.setSelected(true);
                    }
                    isDraggingObjects = true;
                    lastDragX = x;
                    lastDragY = y;
                    found = true;
                    break;
                }
            }

            if (!found) {
                clearSelection();
                isDragging = true;
                dragStartX = dragEndX = x;
                dragStartY = dragEndY = y;
            }
        } else if (isLinkMode()) {
            dragStartX = dragEndX = x;
            dragStartY = dragEndY = y;
            isDragging = true;
        }
    }

    public void handleMouseDragged(int x, int y) {
        if (isDraggingObjects) {
            int dx = x - lastDragX;
            int dy = y - lastDragY;
            for (GraphicalObject obj : objects) {
                if (obj.isSelected()) {
                    moveObject(obj, dx, dy);
                }
            }
            lastDragX = x;
            lastDragY = y;
        } else if (isDragging) {
            dragEndX = x;
            dragEndY = y;
        }
    }

    public void handleMouseReleased(int x, int y) {
        dragEndX = x;
        dragEndY = y;

        if (currentMode.equals("SELECT")) {
            if (isDragging) {
                selectByBox();
            }
        } else if (isLinkMode() && isDragging) {
    GraphicalObject from = findObjectAt(dragStartX, dragStartY);
    GraphicalObject to = findObjectAt(dragEndX, dragEndY);

    if (from != null && to != null && from != to) {
        try {
            LinkType type = LinkType.valueOf(currentMode);
            links.add(new Link(from, to, type));
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid link type: " + currentMode);
        }
    } else {
        System.out.println("❌ 沒有選中兩個物件，取消加線");
    }
}


        isDragging = false;
        isDraggingObjects = false;
    }

    private void moveObject(GraphicalObject obj, int dx, int dy) {
        obj.moveTo(obj.getX() + dx, obj.getY() + dy);
    }

    private void selectByBox() {
        int x1 = Math.min(dragStartX, dragEndX);
        int y1 = Math.min(dragStartY, dragEndY);
        int x2 = Math.max(dragStartX, dragEndX);
        int y2 = Math.max(dragStartY, dragEndY);
        Rectangle box = new Rectangle(x1, y1, x2 - x1, y2 - y1);

        boolean anySelected = false;
        for (GraphicalObject obj : objects) {
            Rectangle r = new Rectangle(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
            if (box.contains(r)) {
                obj.setSelected(true);
                anySelected = true;
            } else {
                obj.setSelected(false);
            }
        }

        if (!anySelected) clearSelection();
    }

    private boolean isLinkMode() {
        return currentMode.equals("ASSOCIATION")
            || currentMode.equals("GENERALIZATION")
            || currentMode.equals("COMPOSITION");
    }

    public Rectangle getSelectionBox() {
        if (isDragging && currentMode.equals("SELECT")) {
            int x = Math.min(dragStartX, dragEndX);
            int y = Math.min(dragStartY, dragEndY);
            int w = Math.abs(dragEndX - dragStartX);
            int h = Math.abs(dragEndY - dragStartY);
            return new Rectangle(x, y, w, h);
        }
        return null;
    }

    public List<GraphicalObject> getObjects() {
        return objects;
    }

    public List<Link> getLinks() {
        return links;
    }

    public GraphicalObject getSelectedObject() {
        for (GraphicalObject obj : objects) {
            if (obj.isSelected()) return obj;
        }
        return null;
    }

    private void clearSelection() {
        for (GraphicalObject obj : objects) {
            obj.setSelected(false);
        }
    }

    //  新增：抓取指定座標上的物件
    private GraphicalObject findObjectAt(int x, int y) {
        for (int i = objects.size() - 1; i >= 0; i--) {
            if (objects.get(i).contains(x, y)) {
                return objects.get(i);
            }
        }
        return null;
    }

    public void groupSelectedObjects() {
    List<GraphicalObject> selected = new ArrayList<>();
    for (GraphicalObject obj : objects) {
        if (obj.isSelected()) {
            selected.add(obj);
        }
    }

    if (selected.size() <= 1) return;

    objects.removeAll(selected);
    CompositeObject group = new CompositeObject(selected);
    group.setSelected(true);
    objects.add(group);
}

public void ungroupSelectedObject() {
    GraphicalObject selected = getSelectedObject();
    if (!(selected instanceof CompositeObject)) return;

    CompositeObject group = (CompositeObject) selected;
    objects.remove(group);
    for (GraphicalObject child : group.getChildren()) {
        child.setSelected(true);
        objects.add(child);
    }
}


}
