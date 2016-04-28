/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mxb.jts.triangulate;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * @author Michael Bedward
 */
public class TestPolygonProvider {

    public static final Map<String, Polygon> polygons = new LinkedHashMap<String, Polygon>();

    static {
        Polygon poly = null;
        WKTReader reader = new WKTReader();

        try {
            /*
             * Triangle (to check that the algorithm doesn't fail
             * when there is nothing to do
             */
            poly = (Polygon) reader.read(
                    "POLYGON((0 0, 50 100, 100 0, 0 0))");

            polygons.put("nothing to do", poly);

            /*
             * Two isoceles triangles forming a single polygon with no holes
             */
            poly = (Polygon) reader.read(
                    "POLYGON( (50 100, 100 200, 200 0, 100 0, 100 100, 50 100) )" );

            polygons.put("isoceles", poly);

            /*
             * Square with extra vertices to test that all
             * vertices get used
             */
            poly = (Polygon) reader.read(
                    "POLYGON((0 0, 0 20, 0 40, 0 60, 0 80, 0 100, 100 100, " +
                    "100 80, 100 60, 100 40, 100 20, 100 0, 0 0))");

            polygons.put("square with extra vertices", poly);

            /*
             * House shape with diamond hole
             */
            poly = (Polygon) reader.read(
                    "POLYGON((0 0, 10 0, 10 5, 5 10, 0 5, 0 0), "
                    + "(5 3, 7 5, 5 7, 3 5, 5 3))");

            polygons.put("house with diamond hole", poly);

            /*
             * Martin's evil polygon
             */
            poly = (Polygon) reader.read(
                    "POLYGON ((50 440, 50 50, 510 50, 510 440, 280 240, 50 440), "
                    + "(105 230, 443 228, 106 208, 105 230), "
                    + "(280 210, 260 190, 310 190, 280 210))");

            polygons.put("evil holes", poly);

            /*
             * Another evil polygon. Hole touching outer shell at a single point
             * as permitted by the OGC specs.
             */
            poly = (Polygon) reader.read(
                    "POLYGON ((30 40, 30 180, 210 180, 210 40, 30 40), " +
                    "(80 110, 160 110, 120 180, 80 110))" );

            polygons.put("evil OGC touching hole", poly);

            /*
             * 5 square holes in a square polygon
             */
            poly = (Polygon) reader.read(
                    "POLYGON ((0 0, 0 200, 200 200, 200 0, 0 0), "
                    + "(20 20, 20 40, 40 40, 40 20, 20 20), "
                    + "(20 160, 20 180, 40 180, 40 160, 20 160), "
                    + "(160 160, 160 180, 180 180, 180 160, 160 160), "
                    + "(160 20, 160 40, 180 40, 180 20, 160 20), "
                    + "(90 90, 90 110, 110 110, 110 90, 90 90) )");

            polygons.put("5 square holes", poly);

            /*
             * Interlocking U-shaped holes
             */
            poly = (Polygon) reader.read(
                    "POLYGON ((0 0, 0 200, 250 200, 250 0, 0 0), " +
                    "(40 40, 40 170, 60 170, 60 60, 140 60, 140 120, 160 120, 160 40, 40 40), " +
                    "(90 80, 90 170, 210 170, 210 40, 190 40, 190 150, 110 150, 110 80, 90 80))" );

            polygons.put("u-shaped holes", poly);

        } catch (ParseException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static Polygon getPolygon(String name) {
        return polygons.get(name);
    }

    public static Collection<String> getNames() {
        return polygons.keySet();
    }
}
