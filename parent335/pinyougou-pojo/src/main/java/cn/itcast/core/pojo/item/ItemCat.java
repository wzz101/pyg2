package cn.itcast.core.pojo.item;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ItemCat implements Serializable {
    /**
     * 类目ID
     */
    private Long id;

    /**
     * 父类目ID=0时，代表的是一级的类目
     */
    private Long parentId;

    /**
     * 类目名称
     */
    private String name;

    /**
     * 类型id
     */
    private Long typeId;

    public ItemCat() {
    }

    public ItemCat(Long id, Long parentId, String name, Long typeId, List<ItemCat> itemCatList) {

        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.typeId = typeId;
        this.itemCatList = itemCatList;
    }

    public List<ItemCat> getItemCatList() {

        return itemCatList;
    }

    public void setItemCatList(List<ItemCat> itemCatList) {
        this.itemCatList = itemCatList;
    }

    private List<ItemCat> itemCatList;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return "ItemCat{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", typeId=" + typeId +
                ", itemCatList=" + itemCatList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemCat itemCat = (ItemCat) o;
        return Objects.equals(id, itemCat.id) &&
                Objects.equals(parentId, itemCat.parentId) &&
                Objects.equals(name, itemCat.name) &&
                Objects.equals(typeId, itemCat.typeId) &&
                Objects.equals(itemCatList, itemCat.itemCatList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, parentId, name, typeId, itemCatList);
    }
}