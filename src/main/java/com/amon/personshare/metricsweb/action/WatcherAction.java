package com.amon.personshare.metricsweb.action;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Created by Administrator on 2015/10/17.
 */
public class WatcherAction  extends ActionSupport {

    private static final long serialVersionUID = -8950477819884632649L;
    private int id;
    private String name;

    public String findData(){

        name = "你好用户"+id;

        return SUCCESS;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
