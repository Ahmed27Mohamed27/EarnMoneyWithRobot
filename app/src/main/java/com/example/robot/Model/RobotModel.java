package com.example.robot.Model;

public class RobotModel {
    private String price_robot, time_robot, earn_price, daily_price, time_now;
    private int price, price_plus;

    public int getPrice_plus() {
        return price_plus;
    }

    public void setPrice_plus(int price_plus) {
        this.price_plus = price_plus;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTime_now() {
        return time_now;
    }

    public void setTime_now(String time_now) {
        this.time_now = time_now;
    }

    public String getPrice_robot() {
        return price_robot;
    }

    public void setPrice_robot(String price_robot) {
        this.price_robot = price_robot;
    }

    public String getTime_robot() {
        return time_robot;
    }

    public void setTime_robot(String time_robot) {
        this.time_robot = time_robot;
    }

    public String getEarn_price() {
        return earn_price;
    }

    public void setEarn_price(String earn_price) {
        this.earn_price = earn_price;
    }

    public String getDaily_price() {
        return daily_price;
    }

    public void setDaily_price(String daily_price) {
        this.daily_price = daily_price;
    }
}
