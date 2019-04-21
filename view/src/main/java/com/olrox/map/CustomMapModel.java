package com.olrox.map;

import org.primefaces.model.map.Circle;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Overlay;
import org.primefaces.model.map.Polygon;
import org.primefaces.model.map.Polyline;
import org.primefaces.model.map.Rectangle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CustomMapModel implements MapModel, Serializable {
    private static final long serialVersionUID = 1L;
    private List<Marker> markers = new ArrayList();

    public CustomMapModel() {
    }

    public List<Marker> getMarkers() {
        return this.markers;
    }

    @Override
    public List<Polyline> getPolylines() {
        return Collections.emptyList();
    }

    @Override
    public List<Polygon> getPolygons() {
        return Collections.emptyList();
    }

    @Override
    public List<Circle> getCircles() {
        return Collections.emptyList();
    }

    @Override
    public List<Rectangle> getRectangles() {
        return Collections.emptyList();
    }

    public void addOverlay(Overlay overlay) {
        if (overlay instanceof Marker) {
            overlay.setId("marker" + overlay.getData().toString());
            this.markers.add((Marker)overlay);
        }
    }

    public Overlay findOverlay(String id) {
        List list = markers;

        Iterator iterator = list.iterator();

        Overlay overlay;
        do {
            if (!iterator.hasNext()) {
                return null;
            }

            overlay = (Overlay)iterator.next();
        } while(!overlay.getId().equals(id));

        return overlay;
    }
}
