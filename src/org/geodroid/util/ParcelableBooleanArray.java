package org.geodroid.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseBooleanArray;

/**
 * Extension of {@link SparseBooleanArray} that implements {@link Parcelable}.
 * 
 * @author Justin Deoliveira, OpenGeo
 */
public class ParcelableBooleanArray extends SparseBooleanArray implements Parcelable {

    public static Parcelable.Creator<ParcelableBooleanArray> CREATOR = 
        new Parcelable.Creator<ParcelableBooleanArray>() {

            @Override
            public ParcelableBooleanArray createFromParcel(Parcel p) {
                int size = p.readInt();

                int[] keys = new int[size];
                p.readIntArray(keys);

                boolean[] vals = new boolean[size];
                p.readBooleanArray(vals);

                ParcelableBooleanArray array = new ParcelableBooleanArray(size);
                for (int i = 0; i < size; i++) {
                    array.put(keys[i], vals[i]);
                }

                return array;
            }

            @Override
            public ParcelableBooleanArray[] newArray(int size) {
                return new ParcelableBooleanArray[size];
            }
    };

    public ParcelableBooleanArray() {
        super();
    }

    public ParcelableBooleanArray(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel p, int flags) {
        int size = size();
        int[] keys = new int[size];
        boolean[] vals = new boolean[size];

        for (int i = 0; i < size; i++) {
            keys[i] = keyAt(i);
            vals[i] = valueAt(i);
        }
        
        p.writeInt(size);
        p.writeIntArray(keys);
        p.writeBooleanArray(vals);
    }
}
