package com.example.tourguide.helperclass;

public class countRate {
    String ppl,totalRate;

    public countRate(String ppl, String totalRate) {
        this.ppl = ppl;
        this.totalRate = totalRate;
    }

    public String getPpl() {
        return ppl;
    }

    public void setPpl(String ppl) {
        this.ppl = ppl;
    }

    public String getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(String totalRate) {
        this.totalRate = totalRate;
    }

    public countRate() {
    }
}
