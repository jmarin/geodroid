package org.geodroid.map;

import org.jeo.geom.Envelopes;
import org.jeo.map.Map;

import com.vividsolutions.jts.geom.Coordinate;

import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * Map control that pans in response to the sliding gesture.
 *
 * @author Justin Deoliveira, OpenGeo
 */
public class PanControl implements MapControl {

    PointF start = new PointF();

    @Override
    public void init(MapView mapView) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev, MapView mapView) {
        if (ev.getPointerCount() > 1) {
            return false;
        }

        switch(ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start.x = ev.getX();
                start.y = ev.getY();
                return true;

            case MotionEvent.ACTION_MOVE:
                PointF end = new PointF(ev.getX(), ev.getY());

                // use negative of end - start
                mapView.getViewport().translate(start.x - end.x, start.y - end.y);
                mapView.invalidate();

                Coordinate c1 = mapView.getTransform().getCanvasToWorld().map(start);
                Coordinate c2 = mapView.getTransform().getCanvasToWorld().map(end);

                start.x = end.x;
                start.y = end.y;

                double dx = c1.x - c2.x;
                double dy = c1.y - c2.y;

                Map map = mapView.getMap();
                map.setBounds(Envelopes.translate(map.getBounds(), dx, dy));
                return true;
        }

        return false;

    }
}
