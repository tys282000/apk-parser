package net.dongliu.apk.parser.struct.resource;

import net.dongliu.apk.parser.struct.StringPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Resource packge.
 *
 * @author dongliu
 */
public class ResourcePackage {
    // the packageName
    private String name;
    private short id;
    // contains the names of the types of the Resources defined in the ResourcePackage
    private StringPool typeStringPool;
    //  contains the names (keys) of the Resources defined in the ResourcePackage.
    private StringPool keyStringPool;

    public ResourcePackage(PackageHeader header) {
        this.name = header.getName();
        this.id = (short) header.getId();
    }

    private Map<Short, TypeSpec> typeSpecMap = new HashMap<>();
    private Map<String, TypeSpec> typeSpecNameMap = new HashMap<>();


    private Map<Short, List<Type>> typesMap = new HashMap<>();
    private Map<String, List<Type>> typesNameMap = new HashMap<>();

    public void addTypeSpec(TypeSpec typeSpec) {
        this.typeSpecMap.put(typeSpec.getId(), typeSpec);
        this.typeSpecNameMap.put(typeSpec.getName(), typeSpec);
    }

    public Map<String, TypeSpec> getTypeSpecNameMap() {
        return this.typeSpecNameMap;
    }

    public Map<String, List<Type>> getTypesNameMap() {
        return this.typesNameMap;
    }

    public TypeSpec getTypeSpec(Short id) {
        return this.typeSpecMap.get(id);
    }

    public void addType(Type type) {
        List<Type> types = this.typesMap.get(type.getId());

        if (types == null) {
            types = new ArrayList<>();
            this.typesMap.put(type.getId(), types);
            this.typesNameMap.put(type.getName(), types);
        }
        types.add(type);
    }

    public List<Type> getTypes(Short id) {
        return this.typesMap.get(id);
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

    public StringPool getTypeStringPool() {
        return typeStringPool;
    }

    public void setTypeStringPool(StringPool typeStringPool) {
        this.typeStringPool = typeStringPool;
    }

    public StringPool getKeyStringPool() {
        return keyStringPool;
    }

    public void setKeyStringPool(StringPool keyStringPool) {
        this.keyStringPool = keyStringPool;
    }

    public Map<Short, TypeSpec> getTypeSpecMap() {
        return typeSpecMap;
    }

    public void setTypeSpecMap(Map<Short, TypeSpec> typeSpecMap) {
        this.typeSpecMap = typeSpecMap;
    }

    public Map<Short, List<Type>> getTypesMap() {
        return typesMap;
    }

    public void setTypesMap(Map<Short, List<Type>> typesMap) {
        this.typesMap = typesMap;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof ResourcePackage) {
            ResourcePackage oldResourcePackage = (ResourcePackage) object;
            if (!name.equals(oldResourcePackage.getName())) {
                return false;
            }
            if (!typeStringPool.equals(oldResourcePackage.getTypeStringPool())) {
                return false;
            }
            if (!keyStringPool.equals(oldResourcePackage.getKeyStringPool())) {
                return false;
            }
            Map<String, TypeSpec> oldTypeSpecNameMap = oldResourcePackage.getTypeSpecNameMap();

            if (typeSpecNameMap.size() != oldTypeSpecNameMap.size()) {
                return false;
            }
            for (String typeName : typeSpecNameMap.keySet()) {
                if (!oldTypeSpecNameMap.containsKey(typeName)) {
                    return false;
                }
                if (!typeSpecNameMap.get(typeName).equals(oldTypeSpecNameMap.get(typeName))) {
                    return false;
                }
            }
            Map<String, List<Type>> oldTypesNameMap = oldResourcePackage.getTypesNameMap();
            if (typesNameMap.size() != oldTypesNameMap.size()) {
                return false;
            }
            for (String typeName : typesNameMap.keySet()) {
                if (!oldTypesNameMap.containsKey(typeName)) {
                    return false;
                }
                List<Type> oldTypeList = oldTypesNameMap.get(typeName);
                List<Type> newTypeList = typesNameMap.get(typeName);
                if (oldTypeList.size() != newTypeList.size()) {
                    return false;
                }
                HashSet<Type> foundTypes = new HashSet<>();
                for (Type type : oldTypeList) {
                    boolean found = false;
                    for (Type newType : newTypeList) {
                        if (foundTypes.contains(newType)) {
                            continue;
                        }
                        if (type.equals(newType)) {
                            foundTypes.add(newType);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
//                        System.out.println("type not found:" + type.getName());
                        //type not found
                        return false;
                    }
                }
            }

            return true;
        }
        return false;
    }
}
