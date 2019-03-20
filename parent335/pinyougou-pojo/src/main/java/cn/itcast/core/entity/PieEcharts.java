package cn.itcast.core.entity;

import java.io.Serializable;
import java.util.List;

public class PieEcharts implements Serializable {
    private String name;
    private String type;
    private String radius;
    private List<PieDataEcharts> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public List<PieDataEcharts> getData() {
        return data;
    }

    public void setData(List<PieDataEcharts> date) {
        this.data = date;
    }
}
