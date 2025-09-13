package com.tvfootballhd.liveandstream.Model;

import java.io.Serializable;

public class NotificationData implements Serializable {
    private int fixtureId;
    private int id;
    private int isSelected;
    private int time;

    public NotificationData(int i, int i2, int i3, int i4) {
        this.id = i;
        this.fixtureId = i2;
        this.time = i3;
        this.isSelected = i4;
    }

    public NotificationData() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public int getFixtureId() {
        return this.fixtureId;
    }

    public void setFixtureId(int i) {
        this.fixtureId = i;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int i) {
        this.time = i;
    }

    public int getIsSelected() {
        return this.isSelected;
    }

    public void setIsSelected(int i) {
        this.isSelected = i;
    }
}
