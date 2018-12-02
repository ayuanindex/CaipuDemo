package com.example.geq.caipudemo.vo;

public class Menuinfo {
    private String spic;
    private String assistmaterial;
    private String notlikes;
    private String menuname;
    private String abstracts;
    private String mainmaterial;
    private String menuid;
    private String typeid;
    private String likes;

    public Menuinfo() {
    }

    public Menuinfo(String spic, String assistmaterial, String notlikes, String menuname, String abstracts, String mainmaterial, String menuid, String typeid, String likes) {
        this.spic = spic;
        this.assistmaterial = assistmaterial;
        this.notlikes = notlikes;
        this.menuname = menuname;
        this.abstracts = abstracts;
        this.mainmaterial = mainmaterial;
        this.menuid = menuid;
        this.typeid = typeid;
        this.likes = likes;
    }

    public String getSpic() {
        return spic;
    }

    public void setSpic(String spic) {
        this.spic = spic;
    }

    public String getAssistmaterial() {
        return assistmaterial;
    }

    public void setAssistmaterial(String assistmaterial) {
        this.assistmaterial = assistmaterial;
    }

    public String getNotlikes() {
        return notlikes;
    }

    public void setNotlikes(String notlikes) {
        this.notlikes = notlikes;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getMainmaterial() {
        return mainmaterial;
    }

    public void setMainmaterial(String mainmaterial) {
        this.mainmaterial = mainmaterial;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
}
