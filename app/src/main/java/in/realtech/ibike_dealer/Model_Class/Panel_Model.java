package in.realtech.ibike_dealer.Model_Class;

import java.io.Serializable;

public class Panel_Model implements Serializable {
    public String sno;
    public String username;
    public String vehicleno;
    public String contact1;
    public String contact2;
    public String imei;
    public String serialno;
    public String problem_category;
    public String Place;
    public String product_type;
    public String problem;
    public String vehicle_avail;
    public String dt;
    public String reassign_reason;
    public String reassign_date;
    public String reassign_count;
    public String fitter_name;
    public String fitter_accept_time;
    public String completion_time;
    public String compliant;
    public String solution;
    public String cash_collected;
    public String cancel_reason;
    public  boolean isBtnVisibled=false;

    public Panel_Model() {
    }

    public Panel_Model(String sno, String username, String vehicleno, String contact1, String contact2, String imei, String serialno, String problem_category, String place, String product_type, String problem, String vehicle_avail, String dt, String reassign_reason, String reassign_date, String reassign_count, String fitter_name, String fitter_accept_time, String completion_time, String compliant, String solution, String cash_collected, String cancel_reason, boolean isBtnVisibled) {
        this.sno = sno;
        this.username = username;
        this.vehicleno = vehicleno;
        this.contact1 = contact1;
        this.contact2 = contact2;
        this.imei = imei;
        this.serialno = serialno;
        this.problem_category = problem_category;
        Place = place;
        this.product_type = product_type;
        this.problem = problem;
        this.vehicle_avail = vehicle_avail;
        this.dt = dt;
        this.reassign_reason = reassign_reason;
        this.reassign_date = reassign_date;
        this.reassign_count = reassign_count;
        this.fitter_name = fitter_name;
        this.fitter_accept_time = fitter_accept_time;
        this.completion_time = completion_time;
        this.compliant = compliant;
        this.solution = solution;
        this.cash_collected = cash_collected;
        this.cancel_reason = cancel_reason;
        this.isBtnVisibled = isBtnVisibled;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVehicleno() {
        return vehicleno;
    }

    public void setVehicleno(String vehicleno) {
        this.vehicleno = vehicleno;
    }

    public String getContact1() {
        return contact1;
    }

    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }

    public String getContact2() {
        return contact2;
    }

    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public String getProblem_category() {
        return problem_category;
    }

    public void setProblem_category(String problem_category) {
        this.problem_category = problem_category;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getVehicle_avail() {
        return vehicle_avail;
    }

    public void setVehicle_avail(String vehicle_avail) {
        this.vehicle_avail = vehicle_avail;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getReassign_reason() {
        return reassign_reason;
    }

    public void setReassign_reason(String reassign_reason) {
        this.reassign_reason = reassign_reason;
    }

    public String getReassign_date() {
        return reassign_date;
    }

    public void setReassign_date(String reassign_date) {
        this.reassign_date = reassign_date;
    }

    public String getReassign_count() {
        return reassign_count;
    }

    public void setReassign_count(String reassign_count) {
        this.reassign_count = reassign_count;
    }

    public String getFitter_name() {
        return fitter_name;
    }

    public void setFitter_name(String fitter_name) {
        this.fitter_name = fitter_name;
    }

    public String getFitter_accept_time() {
        return fitter_accept_time;
    }

    public void setFitter_accept_time(String fitter_accept_time) {
        this.fitter_accept_time = fitter_accept_time;
    }

    public String getCompletion_time() {
        return completion_time;
    }

    public void setCompletion_time(String completion_time) {
        this.completion_time = completion_time;
    }

    public String getCompliant() {
        return compliant;
    }

    public void setCompliant(String compliant) {
        this.compliant = compliant;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getCash_collected() {
        return cash_collected;
    }

    public void setCash_collected(String cash_collected) {
        this.cash_collected = cash_collected;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    public boolean isBtnVisibled() {
        return isBtnVisibled;
    }

    public void setBtnVisibled(boolean btnVisibled) {
        isBtnVisibled = btnVisibled;
    }
}