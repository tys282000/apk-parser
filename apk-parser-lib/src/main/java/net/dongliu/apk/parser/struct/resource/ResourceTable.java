package net.dongliu.apk.parser.struct.resource;

import net.dongliu.apk.parser.struct.StringPool;
import net.dongliu.apk.parser.utils.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dongliu
 */
public class ResourceTable {
    private Map<Short, ResourcePackage> packageMap = new HashMap<>();
    private Map<String, ResourcePackage> packageNameMap = new HashMap<>();

    private StringPool stringPool;
    private long       fileSize;

    public static Map<Integer, String> sysStyle = ResourceLoader.loadSystemStyles();

    public void addPackage(ResourcePackage resourcePackage) {
        this.packageMap.put(resourcePackage.getId(), resourcePackage);
        this.packageNameMap.put(resourcePackage.getName(), resourcePackage);
    }

    public Map<String, ResourcePackage> getPackageNameMap() {
        return packageNameMap;
    }

    public ResourcePackage getPackage(short id) {
        return this.packageMap.get(id);
    }

    public StringPool getStringPool() {
        return stringPool;
    }

    public void setStringPool(StringPool stringPool) {
        this.stringPool = stringPool;
    }

    public void setFileSize(long size) {
        this.fileSize = size;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof ResourceTable) {
            ResourceTable oldResourceTable = (ResourceTable) object;

            //file size is not equal, must be change!
            if (fileSize != oldResourceTable.getFileSize()) {
                System.out.println("resources.arsc is not equal, reason: file size is changed");
                return false;
            }
            //diff string pool
            if (!stringPool.equals(oldResourceTable.getStringPool())) {
                System.out.println("resources.arsc is not equal, reason: string pool is changed");
                return false;
            }
            Map<String, ResourcePackage> oldPackageNameMap = oldResourceTable.getPackageNameMap();
            if (packageNameMap.size() != oldPackageNameMap.size()) {
                System.out.println("resources.arsc is not equal, reason: package size is changed");
                return false;
            }
            for (String packageName : packageNameMap.keySet()) {
                if (!oldPackageNameMap.containsKey(packageName)) {
                    System.out.println("resources.arsc is not equal, reason: package name is not the same");
                    return false;
                }
                if (!packageNameMap.get(packageName).equals(oldPackageNameMap.get(packageName))) {
                    System.out.println("resources.arsc is not equal, reason: package is not equal");
                    return false;
                }
            }

            return true;
        }
        return false;
    }
}
