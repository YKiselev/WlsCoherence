package org.test.pof;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import java.io.IOException;
import java.util.*;

/**
 * Created by Uze on 08.02.14.
 */
public class SurrogateKey implements PortableObject {

    private static final Object[] EMPTY = new Object[0];

    private transient Object[] pairs;

    public SurrogateKey() {
    }

    public SurrogateKey(Object[] pairs) {
        this.pairs = pairs;
    }

    public SurrogateKey(Map<String, Object> source) {
        final int count = source.size();

        pairs = new Object[2 * count];

        final List<String> keys = new ArrayList<String>(source.keySet());

        Collections.sort(keys);

        for (int i = 0; i < count; i++) {
            final String key = keys.get(i);

            pairs[i * 2] = key;
            pairs[i * 2 + 1] = source.get(key);
        }
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> result = new HashMap<String, Object>();
        final int count = pairs.length / 2;

        for (int i = 0; i < count; i++) {
            result.put((String) pairs[i * 2], pairs[i * 2 + 1]);
        }

        return result;
    }

    @Override
    public void readExternal(PofReader pofReader) throws IOException {
        pairs = pofReader.readObjectArray(0, null);
        if (pairs == null) {
            pairs = EMPTY;
        }
    }

    @Override
    public void writeExternal(PofWriter pofWriter) throws IOException {
        pofWriter.writeObjectArray(0, pairs);
    }

    private static boolean equals(Object a, Object b) {
        if (a == null) {
            return b == null;
        }
        if (b == null) {
            return false;
        }
        if (a instanceof Object[]) {
            return b instanceof Object[] && Arrays.equals((Object[]) a, (Object[]) b);
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final SurrogateKey that = (SurrogateKey) o;
        final Object[] thatPairs = that.pairs;
        final int count = pairs.length;
        if (count != thatPairs.length) {
            return false;
        }

        for (int i = 0; i < count; i++) {
            if (!equals(pairs[i], thatPairs[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int code = 1;

        for (Object v : pairs) {
            if (v instanceof Object[]) {
                code = 31 * code + Arrays.hashCode((Object[]) v);
            } else if (v != null) {
                code = 31 * code + v.hashCode();
            }
        }

        return code;
    }
}
