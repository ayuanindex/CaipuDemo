package com.example.geq.caipudemo.vo;

public class Request_menu {
    private int typeid;
    private int startid;
    private int pagesize;

    public Request_menu() {
    }

    public Request_menu(int typeid, int startid, int pagesize) {
        this.typeid = typeid;
        this.startid = startid;
        this.pagesize = pagesize;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public int getStartid() {
        return startid;
    }

    public void setStartid(int startid) {
        this.startid = startid;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }
}