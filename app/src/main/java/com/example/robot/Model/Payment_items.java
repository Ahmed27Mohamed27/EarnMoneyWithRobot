package com.example.robot.Model;

public class Payment_items {
    String date,method,userName,none,earn_money,money,image,numberWallet;

    int coins;

    public Payment_items(String date, String method, String userName, String none, String earn_money, String money, String image, String numberWallet, int coins) {
        this.date = date;
        this.method = method;
        this.userName = userName;
        this.none = none;
        this.earn_money = earn_money;
        this.money = money;
        this.image = image;
        this.numberWallet = numberWallet;
        this.coins = coins;
    }

    public String getNumberWallet() {
        return numberWallet;
    }

    public void setNumberWallet(String numberWallet) {
        this.numberWallet = numberWallet;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Payment_items()
    {

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

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
