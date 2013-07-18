package org.geodroid.app;

import org.jeo.data.Registry;

import android.app.Activity;
import android.app.Fragment;

public class GeoFragment extends Fragment {

    Registry reg;

    public Registry getDataRegistry() {
        return reg;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        reg = GeoApplication.getDataRegistry(activity);
    }

    @Override
    public void onStop() {
        super.onStop();
        GeoApplication.onStop(this);
    }
}
