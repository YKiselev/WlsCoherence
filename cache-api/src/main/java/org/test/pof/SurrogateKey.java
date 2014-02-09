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
    public static final int POF_OPERATION = 0;
    public static final int POF_PAIRS = 1;

    private String operation;
    private Object[] pairs;

    public <E extends Enum<E>> E getOperation(Class<E> enumClass) {
        return Enum.valueOf(enumClass, operation);
    }

    public String getOperation() {
        return operation;
    }

    public SurrogateKey() {
    }

    public SurrogateKey(String operation, Object[] pairs) {
        this.operation = operation;
        this.pairs = pairs;
    }

    public SurrogateKey(String operation, Map<String, Object> source) {
        this.operation = operation;

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
        operation = pofReader.readString(POF_OPERATION);
        pairs = pofReader.readObjectArray(POF_PAIRS, null);
        if (pairs == null) {
            pairs = EMPTY;
        }
    }

    @Override
    public void writeExternal(PofWriter pofWriter) throws IOException {
        pofWriter.writeString(POF_OPERATION, operation);
        pofWriter.writeObjectArray(POF_PAIRS, pairs);
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
        return a.equals(b);
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

        if (!equals(operation, that.operation)) {
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

        if (operation != null) {
            code = 31 * code + operation.hashCode();
        }

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
