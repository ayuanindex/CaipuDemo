package com.example.geq.caipudemo.vo;

public class Step {
    private String stepid;
    private String description;
    private String menuid;
    private String pic;

    public Step(String stepid, String description, String menuid, String pic) {
        this.stepid = stepid;
        this.description = description;
        this.menuid = menuid;
        this.pic = pic;
    }

    public Step() {
    }

    public String getStepid() {
        return stepid;
    }

    public void setStepid(String stepid) {
        this.stepid = stepid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

}
