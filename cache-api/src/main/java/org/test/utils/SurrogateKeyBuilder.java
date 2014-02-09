package org.test.utils;

import org.test.pof.SurrogateKey;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Uze on 09.02.14.
 */
public final class SurrogateKeyBuilder {

    private final String operation;
    private final Map<String, Object> map = new HashMap<String, Object>();

    private SurrogateKeyBuilder(String operation) {
        this.operation = operation;
    }

    public static SurrogateKeyBuilder create(Enum operation) {
        return new SurrogateKeyBuilder(operation.toString());
    }

    public static SurrogateKeyBuilder create(String operation) {
        return new SurrogateKeyBuilder(operation);
    }

    public SurrogateKeyBuilder add(String name, Object value) {
        map.put(name, value);
        return this;
    }

    public SurrogateKey build() {
        return new SurrogateKey(operation, map);
    }
}
