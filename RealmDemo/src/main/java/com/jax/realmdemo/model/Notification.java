package com.jax.realmdemo.model;

import com.jax.realmdemo.bean.NotificationType;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created on 2017/3/30.
 */

public class Notification extends RealmObject {

    @Required
    private String id;
    @Required
    private String type;

    @PrimaryKey
    @Required
    private String primaryId;
    private long time;
    private RealmList<OperationUser> operator;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(@NotificationType.Type String type) {
        this.type = type;
    }

    public String getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(String primaryId) {
        this.primaryId = primaryId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public RealmList<OperationUser> getOperator() {
        return operator;
    }

    public void setOperator(RealmList<OperationUser> operator) {
        this.operator = operator;
    }

    public void addOperator(OperationUser user) {
        if (this.operator == null) {
            operator = new RealmList<>();
        }
        this.operator.add(user);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("primary_id : ").append(primaryId);
        builder.append(" id : ").append(id);
        builder.append(" type : ").append(type);
        builder.append(" operator : [ ");
        for (OperationUser user : operator) {
            builder.append(user.toString());
        }
        builder.append(" ] ").append(" time : ").append(time);
        return builder.toString();
    }
}
