package net.dongliu.apk.parser.struct;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * String pool.
 *
 * @author dongliu
 */
public class StringPool {
    private String[] pool;
    private boolean  utf8;

    private HashMap<Integer, Long> poolOffsets;

    public StringPool(int poolSize) {
        pool = new String[poolSize];
    }

    public String get(int idx) {
        return pool[idx];
    }

    public void set(int idx, String value) {
        pool[idx] = value;
    }

    public String[] getPool() {
        return this.pool;
    }

    public HashMap<Integer, Long> getPoolOffsets() {
        return poolOffsets;
    }

    public void setPoolOffsets(HashMap<Integer, Long> poolOffsets) {
        this.poolOffsets = poolOffsets;
    }
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof StringPool) {
            StringPool oldStringPool = (StringPool) object;
            String[] oldPool = oldStringPool.getPool();
            //pool size is not equal, must be change!
            if (pool.length != oldPool.length) {
                return false;
            }
            HashSet<String> oldSet = new HashSet<>(Arrays.asList(oldPool));
            HashSet<String> newSet = new HashSet<>(Arrays.asList(pool));
            for (String value: oldSet) {
                if (!newSet.contains(value)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void setUtf8(boolean utf8) {
        this.utf8 = utf8;
    }

    public boolean isUtf8() {
        return utf8;
    }
}
