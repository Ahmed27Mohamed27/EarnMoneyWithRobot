package com.example.robot.Admin;

public class Fetch {
    private String name,email, coins,password,mobile,id,image,referCode,uid,date,method,userName,none,earn_money,money,image_robot;
    public Fetch()
    {

    }

    public Fetch(String name, String email, String coins, String password, String mobile, String id, String image, String referCode, String uid, String date, String method, String userName, String none, String earn_money, String money, String image_robot) {
        this.name = name;
        this.email = email;
        this.coins = coins;
        this.password = password;
        this.mobile = mobile;
        this.id = id;
        this.image = image;
        this.referCode = referCode;
        this.uid = uid;
        this.date = date;
        this.method = method;
        this.userName = userName;
        this.none = none;
        this.earn_money = earn_money;
        this.money = money;
        this.image_robot = image_robot;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public String getImage_robot() {
        return image_robot;
    }

    public void setImage_robot(String image_robot) {
        this.image_robot = image_robot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNone() {
        return none;
    }

    public void setNone(String none) {
        this.none = none;
    }

    public String getEarn_money() {
        return earn_money;
    }

    public void setEarn_money(String earn_money) {
        this.earn_money = earn_money;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }
}
