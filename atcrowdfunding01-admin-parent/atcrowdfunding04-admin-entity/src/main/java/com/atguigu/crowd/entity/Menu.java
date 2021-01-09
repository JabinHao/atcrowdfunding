package com.atguigu.crowd.entity;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    // 主键
    private Integer id;

    // 父节点id
    private Integer pid;

    // 节点名称
    private String name;

    // 节点附带url地址，点击菜单项时要跳转的地址
    private String url;

    // 节点图标样式
    private String icon;

    // 存储子节点的集合，初始化以避免空指针异常
    private List<Menu> children = new ArrayList<>();

    // 控制节点是否默认展开，设置为true表示默认打开
    private Boolean open = true;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public List<Menu> getChildren() {
        return children;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Menu() {
    }

    public Menu(Integer id, Integer pid, String name, String url, String icon, List<Menu> children, Boolean open) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.url = url;
        this.icon = icon;
        this.children = children;
        this.open = open;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", icon='" + icon + '\'' +
                ", children=" + children +
                ", open=" + open +
                '}';
    }
}