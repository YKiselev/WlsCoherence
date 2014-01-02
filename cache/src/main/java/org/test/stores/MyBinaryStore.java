package org.test.stores;

import com.tangosol.net.cache.BinaryEntryStore;
import com.tangosol.util.BinaryEntry;

import java.util.Set;

/**
 * Created by Uze on 02.01.14.
 */
public class MyBinaryStore implements BinaryEntryStore {

    public MyBinaryStore(String cacheName) {
        int g = 0;
    }

    @Override
    public void load(BinaryEntry binaryEntry) {
        int g = 0;
    }

    @Override
    public void loadAll(Set set) {
        int g = 0;
    }

    @Override
    public void store(BinaryEntry binaryEntry) {
        int g = 0;
    }

    @Override
    public void storeAll(Set set) {
        int g = 0;
    }

    @Override
    public void erase(BinaryEntry binaryEntry) {
        int g = 0;
    }

    @Override
    public void eraseAll(Set set) {
        int g = 0;
    }
}
