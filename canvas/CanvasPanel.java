package editor.canvas;

import editor.controller.EditorController;
import editor.model.GraphicalObject;
import editor.model.Link;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CanvasPanel extends JPanel implements MouseListener, MouseMotionListener {
    private final EditorController controller;

    public CanvasPanel(EditorController controller) {
        this.controller = controller;
        setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (GraphicalObject obj : controller.getObjects()) {
            obj.draw(g);
        }

        for (Link link : controller.getLinks()) {
            link.draw(g);
        }

        Rectangle box = controller.getSelectionBox();
        if (box != null) {
            g.setColor(Color.GRAY);
            ((Graphics2D) g).setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
            g.drawRect(box.x, box.y, box.width, box.height);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        controller.handleMousePressed(e.getX(), e.getY());
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        controller.handleMouseReleased(e.getX(), e.getY());
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        controller.handleMouseDragged(e.getX(), e.getY());
        repaint();
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}
