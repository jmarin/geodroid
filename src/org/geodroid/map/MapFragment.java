package org.geodroid.map;

import org.geodroid.R;
import org.geodroid.app.GeoFragment;
import org.jeo.map.Layer;
import org.jeo.map.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

/**
 * Fragment containing a {@link MapView}. 
 *  
 * @author Justin Deoliveira, OpenGeo
 */
public class MapFragment extends GeoFragment {

    /**
     * Callback to implemented by activities using the fragment.
     */
    public interface Callback {
        /**
         * Return the map object for the fragment.
         */
        Map getMap();
    }

    Map map;
    MapView mapView;

    public Map getMap() {
        return map;
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof Callback) {
            map = ((Callback) activity).getMap();
        }
        else {
            map = new Map();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        return mapView = new MapView(getActivity(), map);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_map_layers) {
            showLayersPopup(item);
            return true;
        }
        else if (item.getItemId() == R.id.menu_map_refresh) {
            mapView.redraw();
            onRefresh();
            return true;
        }

        return false;
    }

    void showLayersPopup(MenuItem item) {
        Activity act = getActivity();

        View v = act.findViewById(R.id.menu_map_layers);
        PopupMenu popup = new PopupMenu(act, v);

        MenuItem.OnMenuItemClickListener ll = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Layer l = mapView.getMap().getLayers().get(item.getItemId());
                l.setVisible(!l.isVisible());
                mapView.redraw();
                return true;
            }
        };
    
        int id = 0;
        for (Layer l : mapView.getMap().getLayers()) {
            MenuItem it = popup.getMenu().add(Menu.NONE, id++, Menu.NONE, l.getTitle());
            it.setCheckable(true);
            it.setChecked(l.isVisible());
            it.setOnMenuItemClickListener(ll);
        }

        popup.show();
    }

    protected void onRefresh() {
    }
}
