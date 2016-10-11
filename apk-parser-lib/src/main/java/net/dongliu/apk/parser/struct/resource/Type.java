package net.dongliu.apk.parser.struct.resource;

import net.dongliu.apk.parser.struct.ResourceValue;
import net.dongliu.apk.parser.struct.StringPool;
import net.dongliu.apk.parser.utils.Buffers;
import net.dongliu.apk.parser.utils.ParseUtils;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author dongliu
 */
public class Type {

    private String name;
    private short id;
    private TypeHeader typeHeader;

    private Locale         locale;
    private ResTableConfig config;
    private StringPool     keyStringPool;
    private ByteBuffer     buffer;
    private long[]         offsets;
    private StringPool     stringPool;
    private ResourceTable resourceTable;

    private HashMap<Integer, ResourceEntry> resourceEntryHashMap     = new HashMap<>();
    private HashMap<String, ResourceEntry>  resourceEntryNameHashMap = new HashMap<>();

    public Type(TypeHeader header) {
        this.id = header.getId();
        this.config = header.getConfig();
        this.typeHeader = header;
        this.locale = new Locale(header.getConfig().getLanguage(), header.getConfig().getCountry());
    }

    public ResourceEntry getResourceEntry(int id) {
        if (id >= offsets.length) {
            return null;
        }

        if (offsets[id] == TypeHeader.NO_ENTRY) {
            return null;
        }

        return resourceEntryHashMap.get(id);
    }

    public void parseAllResourceEntry() {
        int length = offsets.length;
        for (int i = 0; i < length; i++) {
            readResourceEntry(i);
        }
    }
    private ResourceEntry readResourceEntry(int id) {
        if (id >= offsets.length) {
            return null;
        }
        if (offsets[id] == TypeHeader.NO_ENTRY) {
            return null;
        }
        // read Resource Entries
        buffer.position((int) offsets[id]);

        long beginPos = buffer.position();
        ResourceEntry resourceEntry = new ResourceEntry();
        // size is always 8(simple), or 16(complex)
        resourceEntry.setSize(Buffers.readUShort(buffer));
        resourceEntry.setFlags(Buffers.readUShort(buffer));
        long keyRef = buffer.getInt();
        String key = keyStringPool.get((int) keyRef);
        resourceEntry.setKey(key);

        ResourceEntry resultEntry;

        if ((resourceEntry.getFlags() & ResourceEntry.FLAG_COMPLEX) != 0) {
            ResourceMapEntry resourceMapEntry = new ResourceMapEntry(resourceEntry);

            // Resource identifier of the parent mapping, or 0 if there is none.
            long parent =  Buffers.readUInt(buffer);
            long count = Buffers.readUInt(buffer);
            resourceMapEntry.setParent(parent);
            resourceMapEntry.setCount(count);

            buffer.position((int) (beginPos + resourceEntry.getSize()));

            //An individual complex Resource entry comprises an entry immediately followed by one or more fields.
            ResourceTableMap[] resourceTableMaps = new ResourceTableMap[(int) resourceMapEntry.getCount()];
            for (int i = 0; i < resourceMapEntry.getCount(); i++) {
                resourceTableMaps[i] = readResourceTableMap();
            }

            resourceMapEntry.setResourceTableMaps(resourceTableMaps);
            resultEntry = resourceMapEntry;
        } else {
            buffer.position((int) (beginPos + resourceEntry.getSize()));
            resourceEntry.setValue(ParseUtils.readResValue(buffer, stringPool, resourceTable, locale));
            resultEntry = resourceEntry;
        }
        //set type also
        resultEntry.setType(this);
        resourceEntryHashMap.put(id, resultEntry);
        resourceEntryNameHashMap.put(key, resultEntry);
        return resultEntry;
    }

    private ResourceTableMap readResourceTableMap() {
        ResourceTableMap resourceTableMap = new ResourceTableMap();
        long resId = Buffers.readUInt(buffer);
        resourceTableMap.setNameRef(resId);
        ResourceValue resourceValue = ParseUtils.readResValue(buffer, stringPool, resourceTable, locale);
        resourceTableMap.setResValue(resourceValue);


        if ((resourceTableMap.getNameRef() & 0x02000000) != 0) {
            //read arrays
        } else if ((resourceTableMap.getNameRef() & 0x01000000) != 0) {
            // read attrs
        } else {
        }

        return resourceTableMap;
    }

    public ResTableConfig getConfig() {
        return this.config;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public StringPool getKeyStringPool() {
        return keyStringPool;
    }

    public void setKeyStringPool(StringPool keyStringPool) {
        this.keyStringPool = keyStringPool;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public long[] getOffsets() {
        return offsets;
    }

    public void setOffsets(long[] offsets) {
        this.offsets = offsets;
    }

    public StringPool getStringPool() {
        return stringPool;
    }

    public void setStringPool(StringPool stringPool) {
        this.stringPool = stringPool;
    }

    public TypeHeader getTypeHeader() {
        return typeHeader;
    }

    public void setResourceTable(ResourceTable resourceTable) {
        this.resourceTable = resourceTable;
    }

    public HashMap<String, ResourceEntry> getResourceEntryNameHashMap() {
        return resourceEntryNameHashMap;
    }

    public ResourceTable getResourceTable() {
        return resourceTable;
    }

    @Override
    public String toString() {
        return "Type{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", locale=" + locale +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof Type) {
            Type oldType = (Type) object;

            if (!name.equals(oldType.getName())) {
                return false;
            }
            TypeHeader oldTypeHeader = oldType.getTypeHeader();
//            System.out.println("type name:" + name);

            long typeSize = typeHeader.getChunkSize() - typeHeader.getEntriesStart();
            long oldTypeSize = oldTypeHeader.getChunkSize() - oldTypeHeader.getEntriesStart();

            if (typeSize != oldTypeSize) {
//                System.out.println("type typeSize:" + typeSize + " old typeSize:" + oldTypeSize);
                return false;
            }
            if (typeHeader.getEntryCount() != oldTypeHeader.getEntryCount()) {
//                System.out.println("type getEntryCount:" + typeSize + " old getEntryCount:" + oldTypeSize);
                return false;
            }
            if (!config.equals(oldType.getConfig())) {
//                System.out.println("config is not equal");
                return false;
            }

           HashMap<String, ResourceEntry> oldEntryNameMap = oldType.getResourceEntryNameHashMap();

            if (resourceEntryNameHashMap.size() != oldEntryNameMap.size()) {
                return false;
            }
            for (String specName : resourceEntryNameHashMap.keySet()) {
                if (!oldEntryNameMap.containsKey(specName)) {
                    return false;
                }
                if (!resourceEntryNameHashMap.get(specName).equals(oldEntryNameMap.get(specName))) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

}


