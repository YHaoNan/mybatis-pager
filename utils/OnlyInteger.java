package top.yudoge.mybatis.interceptors.pager.utils;

public class OnlyInteger {
    private Integer integer;

    public OnlyInteger() {}

    public OnlyInteger(Integer integer) {
        this.integer = integer;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    @Override
    public String toString() {
        return "OnlyInteger{" +
                "integer=" + integer +
                '}';
    }
}
