package org.geodroid.map;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.jeo.android.graphics.TransformPipeline;
import org.jeo.data.Dataset;
import org.jeo.map.Layer;
import org.jeo.map.Map;
import org.jeo.map.Style;
import org.osgeo.proj4j.CoordinateReferenceSystem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AndroidRuntimeException;
import android.view.MotionEvent;
import android.view.View;

import com.vividsolutions.jts.geom.Envelope;

/**
 * View that renders the contents of a {@link Map} object and provides controls for 
 * navigating the map. 
 *  
 * @author Justin Deoliveira, OpenGeo
 */
public class MapView extends View implements Map.Listener {

    Map map;
    TransformPipeline tx;

    Bitmap image;
    Viewport viewport;

    List<MapControl> controls;
    RenderWorker renderWorker;

    public MapView(Context context) {
        this(context, new Map());
    }

    public MapView(Context context, Map map) {
        super(context);

        this.map = map;
        this.map.bind(this);

        tx = new TransformPipeline(map);
        viewport = new Viewport();

        controls = 
            Arrays.asList(new PanControl(), new DoubleTapZoomControl(), new PinchZoomControl());
        for (MapControl ctrl : controls) {
            ctrl.init(this);
        }

        renderWorker = new RenderWorker(this);
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public TransformPipeline getTransform() {
        return tx;
    }

    public void redraw() {
        renderWorker.submit(new Map(map));
    }

    public void addLayers(List<Dataset> datasets) {
        if (datasets.isEmpty()) {
            return;
        }

        try {
            if (map.getLayers().isEmpty()) {
                Dataset first = datasets.iterator().next();

                map.setBounds(first.bounds());
                map.setCRS(first.getCRS());
            }
    
            for (Dataset data : datasets) {
                map.getLayers().add(new Layer(data));
            }

            redraw();
        }
        catch(IOException e) {
            throw new AndroidRuntimeException(e);
        }
    }

    void update(final Bitmap img) {
        post(new Runnable() {
            @Override
            public void run() {
                MapView.this.image = img;
                viewport.reinit();
                invalidate();
            }
        });
    }

    public void destroy() {
        map.close();
        renderWorker.shutdown();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean handled = false;
        for (MapControl ctrl : controls) {
            handled = ctrl.onTouchEvent(event, this) || handled;
        }
        return handled;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        image = Bitmap.createBitmap(w, h, Bitmap.Config.ALPHA_8);
        viewport.resize(w,  h);
        map.setSize(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(image, viewport.getWindow(), viewport.getCanvas(), new Paint());
    }

    @Override
    public void onBoundsChanged(Map map, Envelope bounds, Envelope old) {
        redraw();
    }

    @Override
    public void onSizeChanged(Map map, int width, int height, int oldWidth, int oldHeight) {
        redraw();
    }

    @Override
    public void onStyleChanged(Map map, Style style, Style old) {
        redraw();
    }

    @Override
    public void onCRSChanged(Map map, CoordinateReferenceSystem crs, CoordinateReferenceSystem old) {
        redraw();
    }

//    class State {
//        /**
//         * world to screen transform pipeline
//         */
//        TransformPipeline tx;
//    
//        /**
//         * rendered image
//         */
//        Bitmap img;
//    
//        public void init(Canvas canvas) {
//            int w = canvas.getWidth();
//            int h = canvas.getHeight();
//    
//            map.setWidth(w);
//            map.setHeight(h);
//    
//            tx = new TransformPipeline(map, canvas);
//    
//            update(Bitmap.createBitmap(w, h, Bitmap.Config.ALPHA_8));
//        }
//    
//        public Map getMap() {
//            return map;
//        }
//    
//        public Bitmap getImage() {
//            return img;
//        }
//    
//        public TransformPipeline getTransform() {
//            return tx;
//        }
//    
//        public void update(Envelope bounds) {
//            map.setBounds(bounds);
//            tx.update(map);
//        }
//    
//        public void update(Bitmap img) {
//            this.img = img;
//        }
//    }
}
