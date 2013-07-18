package org.geodroid.app;

import java.io.File;

import org.jeo.android.GeoDataRegistry;
import org.jeo.data.Registry;

import com.google.common.base.Function;
import com.google.common.base.Optional;

import android.app.Activity;
import android.app.Application;

/**
 * Extension of android Application that manages global state for geo applications. 
 * <p>
 * A GeoApplication instance exposes a {@link Registry} object to the application
 * activities through {@link GeoApplication#getDataRegistry(Activity)}.
 * </p>
 * @author Justin Deoliveira, OpenGeo
 */
public class GeoApplication extends Application {

    protected Registry dataRegistry;

    public static Optional<GeoApplication> get(Activity activity) {
        Application app = activity.getApplication();
        if (app instanceof GeoApplication) {
            return Optional.of((GeoApplication) app);
        }
        return Optional.absent();
    }

    public static Registry getDataRegistry(Activity activity) {
        return get(activity).transform(new Function<GeoApplication, Registry>() {
            @Override
            public Registry apply(GeoApplication app) {
                return app.getDataRegistry();
            }
        }).or(new GeoDataRegistry());
    }

    public static void onStop(GeoFragment fragment) {
        Optional<GeoApplication> app = get(fragment.getActivity());
        if (app.isPresent()) {
            // means we should clean up the fragments registry
            fragment.getDataRegistry().close();
        }
    }

    public Registry getDataRegistry() {
        return dataRegistry;
    }

    @Override
    public void onCreate() {
        dataRegistry = createDataRegistry();
    }

    protected Registry createDataRegistry() {
        return new GeoDataRegistry();
    }
}
