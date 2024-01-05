package com.kyougaru.service.activity.model;

public class Activity {
    public String activity;
    public String type;
    public String link;
    public String key;
    public Integer participants;
    public Double price;
    public Double accessibility;

    public Activity(String activity, String type, String link, String key, Integer participants, Double price, Double accessibility) {
        this.activity = activity;
        this.type = type;
        this.link = link;
        this.key = key;
        this.participants = participants;
        this.price = price;
        this.accessibility = accessibility;
    }
}
