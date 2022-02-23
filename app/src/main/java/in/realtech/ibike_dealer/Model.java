package in.realtech.ibike_dealer;

public class Model {
    String title;
    String desc,plan;
    //constructor
    public Model(String title, String desc,String plan) {
        this.title = title;
        this.desc = desc;
        this.plan = plan;

    }

    public String getTitle() {
        return this.title;
    }

    public String getDesc() {
        return this.desc;
    }
    public String getPlan() {
        return this.plan;
    }

}
