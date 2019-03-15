package cn.itcast.core.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页的结果集信息
 * pojo:实现序列化接口
 *  1.网络传输
 *  2.持久化
 */
public class PageResult implements Serializable{
    private long total;//总条数
    private List rows;//结果集

    public PageResult(long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
