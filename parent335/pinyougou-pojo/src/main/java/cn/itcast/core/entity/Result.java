package cn.itcast.core.entity;

import java.io.Serializable;

public class Result implements Serializable {
    private boolean flag;//操作是否成功
    private String message;//操作信息

    public Result(boolean flag, String message) {
        this.flag = flag;
        this.message = message;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
