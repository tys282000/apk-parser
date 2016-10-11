package net.dongliu.apk.parser.struct.resource;

/**
 * @author dongliu
 */
public class TypeSpec {

    private long[] entryFlags;
    private String name;
    private short id;

    public TypeSpec(TypeSpecHeader header) {
        this.id = header.getId();
    }

    public boolean exists(int id) {
        return id < entryFlags.length;
    }

    public long[] getEntryFlags() {
        return entryFlags;
    }

    public void setEntryFlags(long[] entryFlags) {
        this.entryFlags = entryFlags;
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

    @Override
    public String toString() {
        return "TypeSpec{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof TypeSpec) {
            TypeSpec oldTypeSpec = (TypeSpec) object;
            if (!name.equals(oldTypeSpec.getName())) {
                return false;
            }
            //ignore entry flag here
            return true;
        }
        return false;
    }
}
