package org.geodroid.map;

import org.jeo.geom.Envelopes;
import org.jeo.map.Map;

import com.vividsolutions.jts.geom.Coordinate;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Map control that zooms on double tap. 
 * 
 * @author Justin Deoliveira, OpenGeo
 */
public class DoubleTapZoomControl implements MapControl {

    int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();

    int doubleTapSlop;
    Tap first;

    @Override
    public void init(MapView mapView) {
        ViewConfiguration viewConfig = ViewConfiguration.get(mapView.getContext());

        doubleTapSlop = viewConfig.getScaledDoubleTapSlop();
        doubleTapSlop *= doubleTapSlop;
    }

    public boolean onTouchEvent(MotionEvent ev, MapView mapView) {
        if (ev.getPointerCount() != 1) {
            return false;
        }
    
        switch(ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            if (first != null && isDoubleTap(first, ev)) {
                mapView.getViewport().scale(0.5f, ev.getX(), ev.getY());
                mapView.invalidate();
    
                Coordinate focus = 
                    mapView.getTransform().getCanvasToWorld().map(new PointF(ev.getX(), ev.getY()));

                Map map = mapView.getMap();
                map.setBounds(Envelopes.scale(map.getBounds(), 0.5, focus));

                first = null;
            }
            else {
                first = new Tap(ev);
            }
            
            return true;
        }
    
        return false;
    }
    
    boolean isDoubleTap(Tap first, MotionEvent second) {
        if (second.getEventTime() - first.time > DOUBLE_TAP_TIMEOUT) {
            return false;
        }
    
        float dx = second.getX() - first.x;
        float dy = second.getY() - first.y;
    
        return dx*dx + dy*dy < doubleTapSlop;
    }

    static class Tap {
        long time;
        float x, y;

        Tap(MotionEvent event) {
            time = event.getEventTime();
            x = event.getX();
            y = event.getY();
        }
    }
}
