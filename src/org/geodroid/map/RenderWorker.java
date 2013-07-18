package org.geodroid.map;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jeo.android.graphics.Renderer;
import org.jeo.map.Map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Debug;
import android.util.Log;

/**
 * Does the work of actually rendering the map, outside of the ui thread.
 *  
 * @author Justin Deoliveira, OpenGeo
 */
class RenderWorker {

    MapView mapView;
    ConcurrentLinkedQueue<Map> tasks;
    ScheduledExecutorService executor;

    RenderWorker(MapView mapView) {
        this.mapView = mapView;
        tasks = new ConcurrentLinkedQueue<Map>(); 
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void submit(Map map) {
        tasks.add(map);
        executor.schedule(new RenderJob(), 100, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        executor.shutdown();
    }

    class RenderJob implements Runnable {

        @Override
        public void run() {
            try {
                // grab the next map to render
                Map map = tasks.poll();
                if (map == null) {
                    return;
                }
    
                // if q is not empty forgot this map since another one pending
                if (!tasks.isEmpty()) {
                    return;
                }
    
                Log.d("renderWorker", "rendering");
                Debug.startMethodTracing("render");
                final Bitmap img = 
                    Bitmap.createBitmap(map.getWidth(), map.getHeight(), Bitmap.Config.ARGB_8888);
                Renderer r = new Renderer(new Canvas(img));
                r.init(map);
                r.render();
                
                Debug.stopMethodTracing();
                mapView.update(img);
            }
            catch(Exception e) {
                Log.w("renderWorker", "Exception during rendering", e);
            }
            
        }
        
    }
}
