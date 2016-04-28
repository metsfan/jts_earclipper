package mxb.jts;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A simple widget to display triangulation results
 *
 * @author Michael Bedward
 */
public class ResultFrame extends JFrame {

    private static final int MARGIN = 10;
    private Canvas canvas;

    public ResultFrame(String title) throws HeadlessException {
        super(title);
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void addPolygon(Geometry p, Color c) {
        if (!(p instanceof Polygon)) {
            throw new IllegalArgumentException("Geometry must be a Polygon");
        }
        canvas.elements.add(new Element((Polygon)p, c));
    }

    private void initComponents() {
        canvas = new Canvas();
        getContentPane().add(canvas);
    }

    private static class Element {
        Polygon geom;
        Color color;

        public Element(Polygon p, Color c) {
            geom = p;
            color = c;
        }
    }

    private static class Canvas extends JPanel {

        AffineTransform tr;
        List<Element> elements = new ArrayList<Element>();

        @Override
        protected void paintComponent(Graphics g) {
            if (!elements.isEmpty()) {
                setTransform();
                Graphics2D g2 = (Graphics2D) g;
                Coordinate[] coords;
                for (Element e : elements) {
                    g2.setColor(e.color);
                        coords = e.geom.getExteriorRing().getCoordinates();
                        draw(g2, coords);
                        for (int i = 0; i < e.geom.getNumInteriorRing(); i++) {
                            coords = e.geom.getInteriorRingN(i).getCoordinates();
                            draw(g2, coords);
                        }
                }
            }
        }

        private void draw(Graphics2D g2, Coordinate[] coords) {
            for (int i = 1; i < coords.length; i++) {
                Point2D p0 = new Point2D.Double(coords[i - 1].x, coords[i - 1].y);
                tr.transform(p0, p0);
                Point2D p1 = new Point2D.Double(coords[i].x, coords[i].y);
                tr.transform(p1, p1);
                g2.drawLine((int) p0.getX(), (int) p0.getY(), (int) p1.getX(), (int) p1.getY());
            }
        }

        private void setTransform() {
            Envelope env = elements.get(0).geom.getEnvelopeInternal();
            for (int i = 1; i < elements.size(); i++) {
                env.expandToInclude(elements.get(i).geom.getEnvelopeInternal());
            }

            Rectangle visRect = getVisibleRect();
            Rectangle drawingRect = new Rectangle(
                    visRect.x + MARGIN, visRect.y + MARGIN, visRect.width - 2 * MARGIN, visRect.height - 2 * MARGIN);

            double scale = Math.min(drawingRect.getWidth() / env.getWidth(), drawingRect.getHeight() / env.getHeight());
            double xoff = MARGIN - scale * env.getMinX();
            double yoff = MARGIN + env.getMaxY() * scale;
            tr = new AffineTransform(scale, 0, 0, -scale, xoff, yoff);
        }
    }
}
