package mxb.jts.triangulate;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michael Bedward
 */
public class TriangleTest {

    @Test
    public void testVertices() {
        System.out.println("   set and get vertices");
        int[] vertices = {1, 2, 3};
        Triangle instance = new Triangle(vertices[0], vertices[1], vertices[2]);

        assertArrayEquals(vertices, instance.getVertices());

        vertices = new int[]{4, 5, 6};
        instance.setVertices(vertices[0], vertices[1], vertices[2]);
        assertArrayEquals(vertices, instance.getVertices());
    }

    @Test
    public void testNoSharedVertices() {
        System.out.println("   no shared vertices");

        Triangle t = new Triangle(1, 2, 3);

        assertNull(t.getSharedVertices(new Triangle(4, 5, 6)));
    }

    @Test
    public void testSingleSharedVertex() {
        System.out.println("   single shared vertex");

        Triangle t = new Triangle(1, 2, 3);
        int[] shared = t.getSharedVertices(new Triangle(3, 5, 6));
        assertNotNull(shared);
        assertEquals(3, shared[0]);
    }

    @Test
    public void testSharedEdge() {
        System.out.println("   shared edge");

        Triangle t = new Triangle(1, 2, 3);
        int[] shared = t.getSharedVertices(new Triangle(1, 2, 4));
        assertNotNull(shared);
        assertEquals(2, shared.length);
        Arrays.sort(shared);
        assertArrayEquals(new int[]{1, 2}, shared);
    }

    @Test
    public void testSharedSelf() {
        System.out.println("   shared self");

        Triangle t = new Triangle(1, 2, 3);
        int[] shared = t.getSharedVertices(t);
        assertNotNull(shared);
        assertEquals(3, shared.length);
        Arrays.sort(shared);
        assertArrayEquals(new int[]{1, 2, 3}, shared);
    }

}