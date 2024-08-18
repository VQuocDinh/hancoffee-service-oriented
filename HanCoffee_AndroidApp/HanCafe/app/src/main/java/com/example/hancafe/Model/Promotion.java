package com.example.hancafe.Model;

public class Promotion {
    private String name;
    private String discount;
    private String description;
    private String startTime;
    private String endTime;
    private String code;


    public Promotion(String name, String discount, String description, String startTime, String endTime, String code) {
        this.name = name;
        this.discount = discount;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.code = code;
    }

    public Promotion() {
    }

    public String getName() {
        return name;
    }

    public String getDiscount() {
        return discount;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

