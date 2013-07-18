package org.geodroid.map;

import org.jeo.android.graphics.TransformPipeline;
import org.jeo.geom.Envelopes;
import org.jeo.map.Map;

import com.vividsolutions.jts.geom.Coordinate;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * Map control that zooms in and out in response to the pinch gesture.
 * 
 * @author Justin Deoliveira, OpenGeo
 */
public class PinchZoomControl extends ScaleGestureDetector.SimpleOnScaleGestureListener 
    implements MapControl {

    MapView mapView;
    ScaleGestureDetector scaleDetector;

    @Override
    public void init(MapView mapView) {
        this.mapView = mapView;
        scaleDetector = new ScaleGestureDetector(mapView.getContext(), this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt, MapView mapView) {
        return scaleDetector.onTouchEvent(evt);
    }

    @Override
    public boolean onScale(ScaleGestureDetector d) {
        // update the viewport
        double factor = 1 / d.getScaleFactor();
        mapView.getViewport().scale((float)factor, d.getFocusX(), d.getFocusY());
        mapView.invalidate();

        // update the map bounds
        TransformPipeline txp = mapView.getTransform(); 
        Coordinate focus = txp.getCanvasToWorld().map(new PointF(d.getFocusX(), d.getFocusY()));

        Map map = mapView.getMap();
        map.setBounds(Envelopes.scale(map.getBounds(), factor, focus));

        return true;
    }
}
