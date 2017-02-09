package com.jax.reactivex.model;

import com.jax.reactivex.bean.FunItemType;

/**
 * Created on 2017/2/8.
 */

public class FunItemInfo {

    private int itemType;
    private String itemTitle;
    private String itemDes;
    private Class target;

    public FunItemInfo(@FunItemType.TYPE int itemType, String itemTitle, String itemDes) {
        this.itemType = itemType;
        this.itemTitle = itemTitle;
        this.itemDes = itemDes;
    }

    public FunItemInfo setTarget(Class target) {
        this.target = target;
        return this;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(@FunItemType.TYPE int itemType) {
        this.itemType = itemType;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemDes() {
        return itemDes;
    }

    public void setItemDes(String itemDes) {
        this.itemDes = itemDes;
    }

    public Class getTarget() {
        return target;
    }
}
