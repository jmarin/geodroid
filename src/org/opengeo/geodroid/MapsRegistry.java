package org.opengeo.geodroid;

import java.io.File;
import java.io.IOException;

import org.jeo.data.Registry;
import org.jeo.data.Workspace;
import org.jeo.geopkg.GeoPackage;

import android.os.Environment;

public class MapsRegistry implements Registry {

    File baseDir;

    public MapsRegistry() throws IOException {
        baseDir = new File(Environment.getExternalStorageDirectory(), "Maps");
    }

    @Override
    public Workspace get(String key) {
        File f = new File(baseDir, key + ".geopkg");
        if (f.exists()) {
            try {
                return new GeoPackage(f);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public void dispose() {
    }

}
