package com.example.geq.caipudemo.vo;

public class vegetableinfo {
    private String typepic;
    private String description;
    private String typeid;
    private String typename;

    public vegetableinfo() {
    }

    public vegetableinfo(String typepic, String description, String typeid, String typename) {
        this.typepic = typepic;
        this.description = description;
        this.typeid = typeid;
        this.typename = typename;
    }

    public String getTypepic() {
        return typepic;
    }

    public void setTypepic(String typepic) {
        this.typepic = typepic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }
}
