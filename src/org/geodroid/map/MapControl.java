package org.geodroid.map;

import android.view.MotionEvent;
import android.view.View;

/**
 * Interface for touch controls used by {@link MapView}.
 *
 * @author Justin Deoliveira, OpenGeo
 */
public interface MapControl {

    /**
     * Initializer called on construction of the map view.
     */
    void init(MapView mapView);

    /**
     * Touch event callback.
     * 
     * @param evt The event.
     * @param mapView the map {@link View}
     * 
     * @return True if the control has handled the event, otherwise false.
     */
    boolean onTouchEvent(MotionEvent evt, MapView mapView);
}
