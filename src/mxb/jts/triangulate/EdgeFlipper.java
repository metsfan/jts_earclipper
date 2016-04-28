/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mxb.jts.triangulate;

import com.vividsolutions.jts.algorithm.CGAlgorithms;
import com.vividsolutions.jts.geom.Coordinate;
import java.util.Collections;
import java.util.List;

/**
 * @author Michael Bedward
 */
public class EdgeFlipper {

    private final List<Coordinate> shellCoords;

    EdgeFlipper(List<Coordinate> shellCoords) {
        this.shellCoords = Collections.unmodifiableList(shellCoords);
    }

    public boolean flip(Triangle t0, Triangle t1) {
        return flip(t0, t1, t0.getSharedVertices(t1));
    }

    public boolean flip(Triangle ear0, Triangle ear1, int[] sharedVertices) {
        if (sharedVertices == null || sharedVertices.length != 2) {
            return false;
        }
        
        Coordinate shared0 = shellCoords.get(sharedVertices[0]);
        Coordinate shared1 = shellCoords.get(sharedVertices[1]);

        /*
         * Find the unshared vertex of each ear
         */
        int[] vertices = ear0.getVertices();
        int i = 0;
        while (vertices[i] == sharedVertices[0] || vertices[i] == sharedVertices[1]) {
            i++ ;
        }
        int v0 = vertices[i];
        Coordinate c0 = shellCoords.get(v0);

        i = 0;
        vertices = ear1.getVertices();
        while (vertices[i] == sharedVertices[0] || vertices[i] == sharedVertices[1]) {
            i++ ;
        }
        int v1 = vertices[i];
        Coordinate c1 = shellCoords.get(v1);

        /*
         * The candidate new edge is from v0 to v1. First check if this
         * is inside the quadrilateral
         */
        final Coordinate[] quadRing = { c0, shared0, c1, shared1 };

        int dir0 = CGAlgorithms.orientationIndex(c0, c1, shared0);
        int dir1 = CGAlgorithms.orientationIndex(c0, c1, shared1);
        if (dir0 == -dir1) {
            // The candidate edge is inside. Compare its length to
            // the current shared edge and swap them if the candidate
            // is shorter.
            if (c0.distance(c1) < shared0.distance(shared1)) {
                ear0.setVertices(sharedVertices[0], v0, v1);
                ear1.setVertices(v1, v0, sharedVertices[1]);
                return true;
            }
        }

        return false;
    }

}
