package cn.itcast.core.pojo.good;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.io.Serializable;
@ExcelTarget("brand")
public class Brand implements Serializable {
    private Long id;

    @Excel(name = "名字_brand")
    private String name;

    /**
     * 品牌首字母
     */
    @Excel(name = "首字母_brand")
    private String firstChar;


    private String status;

    public Brand() {
    }







    public static long getSerialVersionUID() {
        return serialVersionUID;
    }



    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar == null ? null : firstChar.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", status=").append(status);
        sb.append(", firstChar=").append(firstChar);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    public String getStatus() {
        return status;
    }

    public Brand(Long id, String name, String status, String firstChar) {

        this.id = id;
        this.name = name;
        this.status = status;
        this.firstChar = firstChar;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Brand other = (Brand) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getFirstChar() == null ? other.getFirstChar() == null : this.getFirstChar().equals(other.getFirstChar()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getFirstChar() == null) ? 0 : getFirstChar().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }
}