package mxb.jts.triangulate;

import com.vividsolutions.jts.geom.Coordinate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michael Bedward
 */
public class EdgeFlipperTest {

    @Test
    public void flipKiteShape() {
        System.out.println("   kite shape");

        List<Coordinate> clist = new ArrayList<Coordinate>();
        clist.add(new Coordinate(0, 0));
        clist.add(new Coordinate(-5, 10));
        clist.add(new Coordinate(0, 15));
        clist.add(new Coordinate(5, 10));

        Triangle t0 = new Triangle(0, 1, 2);
        Triangle t1 = new Triangle(0, 3, 2);
        EdgeFlipper instance = new EdgeFlipper(clist);

        assertTrue( instance.flip(t0, t1) );

        int[] vertices = t0.getVertices();
        Arrays.sort(vertices);
        assertArrayEquals(new int[]{0, 1, 3}, vertices);

        vertices = t1.getVertices();
        Arrays.sort(vertices);
        assertArrayEquals(new int[]{1, 2, 3}, vertices);
    }

    @Test
    public void concavePolyShouldNotFlip() {
        System.out.println("   onncave polygon");

        List<Coordinate> clist = new ArrayList<Coordinate>();
        clist.add(new Coordinate(0, 0));
        clist.add(new Coordinate(-5, 10));
        clist.add(new Coordinate(0, 5));
        clist.add(new Coordinate(5, 10));

        Triangle t0 = new Triangle(0, 1, 2);
        Triangle t1 = new Triangle(0, 3, 2);
        EdgeFlipper instance = new EdgeFlipper(clist);

        assertFalse( instance.flip(t0, t1) );
    }


    @Test
    public void equilateralTriangleShouldNotFlip() {
        System.out.println("   equilateral triangle polygon");

        List<Coordinate> clist = new ArrayList<Coordinate>();
        clist.add(new Coordinate(0, 0));
        clist.add(new Coordinate(1, Math.sqrt(3.0)));
        clist.add(new Coordinate(2, 0));
        clist.add(new Coordinate(1, 0));

        Triangle t0 = new Triangle(0, 1, 3);
        Triangle t1 = new Triangle(3, 1, 2);
        EdgeFlipper instance = new EdgeFlipper(clist);

        assertFalse( instance.flip(t0, t1) );
    }

    @Test
    public void sliverOnHypoteneuse() {
        System.out.println("   sliver on hypoteneuse of broad triangle");

        List<Coordinate> clist = new ArrayList<Coordinate>();
        clist.add(new Coordinate(0, 0));
        clist.add(new Coordinate(100, 100));
        clist.add(new Coordinate(100, 0));
        clist.add(new Coordinate(50, 60));

        Triangle broad = new Triangle(0, 1, 2);
        Triangle sliver = new Triangle(0, 3, 1);

        EdgeFlipper instance = new EdgeFlipper(clist);
        assertTrue(instance.flip(broad, sliver));

        int[] shared = broad.getSharedVertices(sliver);
        Arrays.sort(shared);
        assertArrayEquals(new int[]{2, 3}, shared);
    }
}