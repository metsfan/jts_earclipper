package mxb.jts.triangulate;

import com.vividsolutions.jts.geom.Coordinate;
import static org.junit.Assert.*;
import org.junit.Test;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Michael Bedward
 */
public class EarClipperTest {

    private static final double EPS = 1.0E-4;

    @Test
    public void testPolygons() {
        System.out.println("Testing triangulation...");
        for (String name : TestPolygonProvider.getNames()) {
            System.out.println("   " + name + " polygon");
            doTest(name);
        }
    }

    private void doTest(String polyName) {
        Polygon poly = TestPolygonProvider.getPolygon(polyName);
        EarClipper clipper = new EarClipper(poly);
        Geometry result = clipper.getResult();

        assertNotNull(result);
        checkForOverlap(result);
        checkAllVerticesUsed(poly, result);
        checkCoverage(poly, result);
    }

    /**
     * Check that none of the polygons in the triangulation result
     * overlap each other
     */
    private void checkForOverlap(Geometry result) {
        final int N = result.getNumGeometries();

        for (int i = 1; i < N; i++) {
            Geometry ipoly = result.getGeometryN(i);
            for (int j = 0; j < i; j++) {
                assertFalse(ipoly.overlaps(result.getGeometryN(j)));
            }
        }
    }

    /**
     * Check that the triangulation result contains all of the
     * polygon vertices and vice versa
     */
    private void checkAllVerticesUsed(Polygon poly, Geometry result) {
        Set<Coordinate> polyCoords = new HashSet<Coordinate>();
        for (Coordinate c : poly.getCoordinates()) {
            polyCoords.add(c);
        }

        Set<Coordinate> resultCoords = new HashSet<Coordinate>();
        for (Coordinate c : result.getCoordinates()) {
            resultCoords.add(c);
        }

        assertTrue(resultCoords.containsAll(polyCoords));
        assertTrue(polyCoords.containsAll(resultCoords));
    }

    /**
     * Check that the triangulation completely covers the
     * original polygon
     */
    private void checkCoverage(Polygon poly, Geometry result) {
        Geometry union = result.union();
        assertTrue(Math.abs(poly.getArea() - union.getArea()) < EPS);
        assertTrue(poly.difference(union).isEmpty());
    }
}
