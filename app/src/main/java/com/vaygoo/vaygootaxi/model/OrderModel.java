package com.vaygoo.vaygootaxi.model;

/**
 * Created by Wizard on 19.05.2017.
 */

public final class OrderModel
{
    private String startPoint;
    private String endPoint;
    private String intermediatePoint;
    private String price;
    private String comment;
    private boolean child;
    private boolean minivan;
    private boolean finalPrice;

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getIntermediatePoint() {
        return intermediatePoint;
    }

    public void setIntermediatePoint(String intermediatePoint) {
        this.intermediatePoint = intermediatePoint;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isChild() {
        return child;
    }

    public void setChild(boolean child) {
        this.child = child;
    }

    public boolean isMinivan() {
        return minivan;
    }

    public void setMinivan(boolean minivan) {
        this.minivan = minivan;
    }

    public boolean isFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(boolean finalPrice) {
        this.finalPrice = finalPrice;
    }

    public OrderModel(String startPoint, String endPoint, String intermediatePoint, String price,
                      String comment, boolean child, boolean minivan, boolean finalPrice) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.intermediatePoint = intermediatePoint;
        this.price = price;
        this.comment = comment;
        this.child = child;
        this.minivan = minivan;
        this.finalPrice = finalPrice;
    }
}
