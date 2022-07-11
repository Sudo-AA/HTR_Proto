package com.example.htr_proto;

public class RecordModel {

    private int id;
    private String datetime;
    private String tremor;
    private String vibrate;

    public RecordModel(int id, String datetime, String tremor, String vibrate) {
        this.id = id;
        this.datetime = datetime;
        this.tremor = tremor;
        this.vibrate = vibrate;
    }

    public RecordModel() {
    }

    @Override
    public String toString() {
        return "RecordModel{" +
                "id=" + id +
                ", datetime='" + datetime + '\'' +
                ", tremor='" + tremor + '\'' +
                ", vibrate='" + vibrate + '\'' +
                '}';
    }
// getter and setters lodi


    public int getId() {
        return id;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getTremor() {
        return tremor;
    }

    public String getVibrate() {
        return vibrate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setTremor(String tremor) {
        this.tremor = tremor;
    }

    public void setVibrate(String vibrate) {
        this.vibrate = vibrate;
    }
}
