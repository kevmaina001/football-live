package com.tvfootballhd.liveandstream.Model;

public class Prediction {
    private String awayName;
    private String awayValue;
    private String drawName;
    private String drawValue;
    private String homeName;
    private String homeValue;
    private String name;

    public Prediction() {
    }

    public Prediction(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        this.name = str;
        this.homeName = str2;
        this.awayName = str3;
        this.homeValue = str4;
        this.awayValue = str5;
        this.drawName = str6;
        this.drawValue = str7;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getHomeName() {
        return this.homeName;
    }

    public void setHomeName(String str) {
        this.homeName = str;
    }

    public String getAwayName() {
        return this.awayName;
    }

    public void setAwayName(String str) {
        this.awayName = str;
    }

    public String getHomeValue() {
        return this.homeValue;
    }

    public void setHomeValue(String str) {
        this.homeValue = str;
    }

    public String getAwayValue() {
        return this.awayValue;
    }

    public void setAwayValue(String str) {
        this.awayValue = str;
    }

    public String getDrawName() {
        return this.drawName;
    }

    public void setDrawName(String str) {
        this.drawName = str;
    }

    public String getDrawValue() {
        return this.drawValue;
    }

    public void setDrawValue(String str) {
        this.drawValue = str;
    }
}
