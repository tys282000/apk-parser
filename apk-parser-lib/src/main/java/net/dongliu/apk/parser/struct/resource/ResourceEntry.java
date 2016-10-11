package net.dongliu.apk.parser.struct.resource;

import net.dongliu.apk.parser.struct.ResValue;
import net.dongliu.apk.parser.struct.ResourceValue;
import net.dongliu.apk.parser.utils.ParseUtils;

import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.Locale;

/**
 * A Resource entry specifies the key (name) of the Resource.
 * It is immediately followed by the value of that Resource.
 *
 * @author dongliu
 */
public class ResourceEntry {
    // Number of bytes in this structure. uint16_t
    private int size;

    // If set, this is a complex entry, holding a set of name/value
    // mappings.  It is followed by an array of ResTable_map structures.
    public static final int FLAG_COMPLEX = 0x0001;
    // If set, this resource has been declared public, so libraries
    // are allowed to reference it.
    public static final int FLAG_PUBLIC = 0x0002;
    // uint16_t
    private int flags;

    // Reference into ResTable_package::keyStrings identifying this entry.
    //public long keyRef;

    private String key;

    // the resvalue following this resource entry.
    private ResourceValue value;

    private Type type;

    /**
     * get value as string
     *
     * @return value
     */
    public String toStringValue() {
        if (value != null) {
            return value.toStringValue();
        } else {
            return "null";
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Nullable
    public ResourceValue getValue() {
        return value;
    }

    @Nullable
    public void setValue(@Nullable ResourceValue value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ResourceEntry{" +
                "size=" + size +
                ", flags=" + flags +
                ", key='" + key + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof ResourceEntry) {
            boolean ifOldMapEntry = object instanceof ResourceMapEntry;
            boolean ifMapEntry = this instanceof ResourceMapEntry;
            if (ifMapEntry != ifOldMapEntry) {
                return false;
            }
            ResourceEntry oldResEntry = (ResourceEntry) object;
            if (!key.equals(oldResEntry.getKey())) {
                return false;
            }
            if (!flagIgnorePublic(flags, oldResEntry.getFlags())) {
                return false;
            }
            if (!valueIgnorePublic(value, type, oldResEntry.getValue(), oldResEntry.getType())) {
                return false;
            }
            if (ifOldMapEntry) {
                 ResourceMapEntry oldMapEntry = (ResourceMapEntry) object;
                 ResourceMapEntry mapEntry = (ResourceMapEntry) this;
                if (mapEntry.getCount() != oldMapEntry.getCount()) {
                    return false;
                }
                String parentSpecName = ParseUtils.getResourceNameById(mapEntry.getParent(), type.getResourceTable());
                String oldParentSpecName = ParseUtils.getResourceNameById(oldMapEntry.getParent(), oldMapEntry.getType().getResourceTable());

                if (!parentSpecName.equals(oldParentSpecName)) {
                    return false;
                }
                ResourceTableMap[] resourceTableMaps = mapEntry.getResourceTableMaps();
                ResourceTableMap[] oldResourceTableMaps = oldMapEntry.getResourceTableMaps();
                if (resourceTableMaps.length != oldResourceTableMaps.length) {
                    return false;
                }
                HashMap<String, ResourceValue> resKeyValue = new HashMap<>();
                HashMap<String, ResourceValue> oldResKeyValue = new HashMap<>();
                int length = resourceTableMaps.length;
                for (int i = 0; i < length; i++) {
                    ResourceTableMap resourceTable = resourceTableMaps[i];
                    String resSpecName = ParseUtils.getResourceNameById(resourceTable.getNameRef(), type.getResourceTable());
                    resKeyValue.put(resSpecName, resourceTable.getResValue());

                    ResourceTableMap oldResourceTable = oldResourceTableMaps[i];
                    String oldResSpecName = ParseUtils.getResourceNameById(oldResourceTable.getNameRef(), oldMapEntry.getType().getResourceTable());
                    oldResKeyValue.put(oldResSpecName, oldResourceTable.getResValue());
                }

                for (String name : resKeyValue.keySet()) {
                    if (!oldResKeyValue.containsKey(name)) {
                        return false;
                    }
                    if (!valueIgnorePublic(resKeyValue.get(name), type, oldResKeyValue.get(name), oldResEntry.getType())) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
    //for public.xml
    public boolean flagIgnorePublic(int flag1, int flag2) {
        return (flag1 & ~ResourceEntry.FLAG_PUBLIC) == (flag2 & ~ResourceEntry.FLAG_PUBLIC);
    }

    public boolean valueIgnorePublic(ResourceValue value1, Type type1, ResourceValue value2, Type type2) {
        if (value1 == null && value2 == null) {
            return true;
        }
        if ((value1 == null && value2 != null) || (value1 != null && value2 == null)) {
            return false;
        }
        //for idx.xml
        if (type.getName().equals("id")) {
            if ((value1.getDataType() == ResValue.ResType.STRING
                && value2.getDataType() == ResValue.ResType.INT_BOOLEAN)
                || (value1.getDataType() == ResValue.ResType.INT_BOOLEAN
                && value1.getDataType() == ResValue.ResType.STRING)) {
                return true;
            }
        }

        if (value1.getSize() != value2.getSize()) {
            return false;
        }
        if (value1.getDataType() != value2.getDataType()) {
            return false;
        }
        //if it is reference value
        if (value1 instanceof ResourceValue.ReferenceResourceValue && value2 instanceof ResourceValue.ReferenceResourceValue) {
            String name1 = ParseUtils.getResourceNameById(((ResourceValue.ReferenceResourceValue) value1).getReferenceResourceId(), type1.getResourceTable());
            String name2 = ParseUtils.getResourceNameById(((ResourceValue.ReferenceResourceValue) value2).getReferenceResourceId(), type2.getResourceTable());
            return name1.equals(name2);

        }
        return (value1.toStringValue().equals(value2.toStringValue()));
    }


}
