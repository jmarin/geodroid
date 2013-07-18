package org.geodroid.map;

import android.graphics.Rect;

/**
 * The window (in pixel space) to render to the canvas;
 *  
 * @author Justin Deoliveira, OpenGeo
 */
class Viewport {

    /** 
     * canvas dimensions
     */
    Rect canvas;

    /**
     * current subset of canvas currently in view
     */
    Rect window;

    public Rect getCanvas() {
        return canvas;
    }

    public Rect getWindow() {
        return window;
    }

    public void resize(int width, int height) {
        canvas = new Rect(0, 0, width, height);
    }

    public void reinit() {
        window = new Rect(canvas);
    }

    public void scale(float factor, float x, float y) {
        x = window.left + x * window.width() / canvas.width();
        y = window.top + y * window.height() / canvas.height(); 

        window.left = (int) Math.floor(x - (x - window.left) * factor);
        window.right = (int) Math.ceil(x + (window.right - x) * factor);
        window.top = (int) Math.floor(y - (y - window.top) * factor);
        window.bottom = (int) Math.ceil(y + (window.bottom - y) * factor);
   }

    public void translate(float dx, float dy) {
        window.left += dx;
        window.right += dx;
        window.top += dy;
        window.bottom += dy;
    }
}
